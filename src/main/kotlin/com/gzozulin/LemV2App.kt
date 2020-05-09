package com.gzozulin

import com.gzozulin.kotlin.KotlinLexer
import com.gzozulin.kotlin.KotlinParser
import com.gzozulin.kotlin.KotlinParserBaseVisitor
import com.gzozulin.statements.StatementsBaseVisitor
import com.gzozulin.statements.StatementsLexer
import com.gzozulin.statements.StatementsParser
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.data.MutableDataSet
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import java.io.File
import kotlin.system.measureTimeMillis

// region --------------------------- ToDo ---------------------------

// todo: command to insert a badge (about author) after pic, before first words
// todo: code width equals to container width
// todo: render only text (to check commentaries with grammarly)

// endregion --------------------------- ToDo ---------------------------

private val         inputFolder = File("scenarios")
private val         outputFolder = File("output")

private val         whitespacePattern = "\\s+".toPattern()

private const val   HOME_DIR = "src/main/kotlin"

private const val   COMMAND_PREFIX = "@"
private const val   COMMAND_DECL = "decl"
private const val   COMMAND_DEF = "def"

private const val   CODE_TEMPLATE = "```kotlin\n%s\n```"
private const val   HTML_LINK_TEMPLATE = "[%s](%s)"
private const val   H5_TEMPLATE = "##### %s"

private val         flexmarkOptions = MutableDataSet()
private val         flexmarkParser = Parser.builder(flexmarkOptions).build()
private val         flexmarkRenderer = HtmlRenderer.builder(flexmarkOptions).build()

private data class ScenarioArguments(val root: File, val url: String, val lines: List<String>)

private data class Location(val root: File, val file: File, val identifier: String, val url: String)

private interface Snippet
private data class SnippetMarkdown(val markdown: String) : Snippet
private data class SnippetCommand(val command: String): Snippet
private data class SnippetHtml(val html: String): Snippet

private typealias AntlrParser = org.antlr.v4.runtime.Parser
private abstract class ParserCache<K, T : AntlrParser> {
    private data class CacheEntry<T : AntlrParser>(var parser: T? = null, val mutex: Mutex = Mutex())

    private val cache = mutableMapOf<K, CacheEntry<T>>()
    fun useParser(key: K, action: (parser: T, stream: CommonTokenStream) -> Unit) {
        var entry: CacheEntry<T>
        synchronized(cache) {
            if (!cache.containsKey(key)) {
                cache[key] = CacheEntry()
            }
            entry = cache[key]!!
        }
        return runBlocking {
            entry.mutex.withLock {
                if (entry.parser == null) {
                    entry.parser = createParser(key)
                }
                val parser = entry.parser!!
                val stream = parser.tokenStream as CommonTokenStream
                parser.reset()
                action.invoke(parser, stream)
            }
        }
    }

    protected abstract fun createParser(key: K): T
}

private val kotlinParserCache = object : ParserCache<File, KotlinParser>() {
    override fun createParser(key: File) =
        KotlinParser(CommonTokenStream(KotlinLexer(CharStreams.fromFileName(key.absolutePath))))
}

private val statementsParserCache = object : ParserCache<String, StatementsParser>() {
    override fun createParser(key: String): StatementsParser =
        StatementsParser(CommonTokenStream(StatementsLexer(CharStreams.fromString(key))))
}

// Everything is straightforward here - we just check if the line starts with **@** symbol
private fun identifyCommands(lines: List<String>) = lines
    .map { if (it.startsWith(COMMAND_PREFIX)) SnippetCommand(it) else SnippetMarkdown(it) }

// The first part of this routine will asynchronously apply commands one-by-one
private fun applyCommands(snippets: List<Snippet>, root: File, url: String): List<SnippetMarkdown> {
    val result = mutableListOf<SnippetMarkdown>()
    lateinit var handled: ArrayList<List<SnippetMarkdown>>
    runBlocking {
        val deferred = mutableListOf<Deferred<List<SnippetMarkdown>>>()
        for (snippet in snippets) {
            if (snippet is SnippetCommand) {
                deferred.add(async (Dispatchers.Default) { applyCommand(snippet, root, url) })
            }
        }
        handled = ArrayList(deferred.awaitAll())
    }
    /*
        When all of the results are available,
        we can assemble them into a list in the *same order* as they were started
     */
    for (snippet in snippets) {
        if (snippet is SnippetCommand) {
            result.addAll(handled.removeAt(0))
        } else {
            result.add(snippet as SnippetMarkdown)
        }
    }
    return result
}

// To apply the specific command I just switch by its label and call the appropriate method
private fun applyCommand(command: SnippetCommand, root: File, url: String): List<SnippetMarkdown> {
    try {
        val cmdBody = command.command.removePrefix(COMMAND_PREFIX)
        val split = cmdBody.split(whitespacePattern)
        val location = parseLocation(root, url, split[2])
        return when (split[1]) {
            COMMAND_DECL -> includeDecl(location)
            COMMAND_DEF -> includeDef(location)
            else -> TODO()
        }
        /*
            Since the commands can contain literally anything,
            one can assume that errors will also happen and prepare for that
         */
    } catch (th: Throwable) {
        error("Failed to handle command: ${command.command}")
    }
}

private fun includeDecl(location: Location): List<SnippetMarkdown> {
    lateinit var tokens: List<Token>
    var line = 0
    val file = File(location.root, location.file.path)
    /*
        To parse and understand Kotlin code, I am using a tool called [Antlr](https://www.antlr.org/).
        Their marketing team outlines the following advantages of the framework:
        "ANTLR (ANother Tool for Language Recognition) is a powerful parser generator for reading,
        processing, executing, or translating structured text or binary files."
        I do not want to go into too many details about parsing - grammar files are
        [available](https://github.com/Kotlin/kotlin-spec) for most of the modern languages.
        If the language of your preference is not in the long [list](https://github.com/antlr/grammars-v4/)
        reconsider its advantages.
     */
    kotlinParserCache.useParser(file) { parser, stream ->
        val declaration = locateKotlin(parser, location)
        line = declaration.start.line
        val firstToken = findFirstToken(stream, declaration)
        val lastToken = findLastToken(stream, declaration)
        tokens = stream.get(firstToken, lastToken)
    }
    val result = mutableListOf<SnippetMarkdown>()
    // At this point we can also create a convenient link, pointing to the Github repo.
    result.add(createHeaderLink(location, line))
    result.addAll(extractStatements(tokens))
    return result
}

private fun includeDef(location: Location): List<SnippetMarkdown> {
    lateinit var tokens: List<Token>
    var line = 0
    kotlinParserCache.useParser(location.file) { parser, stream ->
        val definition = locateKotlin(parser, location)
        line = definition.start.line
        val firstToken = findFirstToken(stream, definition)
        val lastToken = definition.stop.tokenIndex
        tokens = stream.get(firstToken, lastToken)
    }
    val result = mutableListOf<SnippetMarkdown>()
    result.add(createHeaderLink(location, line))
    result.addAll(extractStatements(tokens))
    return result
}

private fun createHeaderLink(location: Location, line: Int): SnippetMarkdown {
    val unixPath = location.file.toString().replace("\\", "/")
    val visiblePath = unixPath.removePrefix(HOME_DIR).removePrefix("/")
    return SnippetMarkdown(H5_TEMPLATE.format(HTML_LINK_TEMPLATE.format(
        "${visiblePath}::${location.identifier}", "${location.url}/${unixPath}#L$line")))
}

private typealias ClassContext = KotlinParser.ClassDeclarationContext
private typealias FunctionContext = KotlinParser.FunctionDeclarationContext
private typealias PropertyContext = KotlinParser.PropertyDeclarationContext

private fun locateKotlin(parser: KotlinParser, location: Location): ParserRuleContext {
    val declarations = mutableListOf<ParserRuleContext>()
    val visitor = object : KotlinParserBaseVisitor<Unit>() {
        override fun visitClassDeclaration(ctx: ClassContext?) {
            super.visitClassDeclaration(ctx)
            if (ctx!!.simpleIdentifier().text == location.identifier) {
                declarations.add(ctx)
            }
        }

        override fun visitFunctionDeclaration(ctx: FunctionContext?) {
            super.visitFunctionDeclaration(ctx)
            if (ctx!!.identifier().text == location.identifier) {
                declarations.add(ctx)
            }
        }

        override fun visitPropertyDeclaration(ctx: PropertyContext?) {
            super.visitPropertyDeclaration(ctx)
            val variableDeclaration = ctx!!.variableDeclaration() ?: return
            if (variableDeclaration.simpleIdentifier().text == location.identifier) {
                declarations.add(ctx)
            }
        }
    }
    visitor.visitKotlinFile(parser.kotlinFile())
    check(declarations.size != 0) { "Location not found: $location" }
    return declarations.first()
}

private fun findFirstToken(tokenStream: CommonTokenStream, context: ParserRuleContext): Int {
    val prevDecl = findPrevDeclaration(tokenStream, context)
    return if (prevDecl != null) {
        prevDecl.tokenIndex + 1
    } else {
        context.start.tokenIndex
    }
}

private fun findLastToken(tokenStream: CommonTokenStream, decl: ParserRuleContext): Int {
    return when (decl) {
        is ClassContext -> {
            if (decl.classBody() != null) {
                decl.classBody().start.tokenIndex - 1
            } else {
                decl.stop.tokenIndex // class have no body
            }
        }
        is FunctionContext -> {
            if (decl.functionBody() != null) {
                decl.functionBody().start.tokenIndex - 1
            } else {
                decl.stop.tokenIndex // function have no body
            }
        }
        is PropertyContext -> {
            if (decl.text.contains("object")) {
                findBodyStart(tokenStream, decl).tokenIndex - 2 // for objects before body begins
            } else {
                decl.stop.tokenIndex // full declaration
            }
        }
        else -> throw UnsupportedOperationException("Unknown type of member!")
    }
}

// todo: sloppy and innacurate
private fun findBodyStart(tokenStream: CommonTokenStream, context: ParserRuleContext): Token {
    val (from, to) = context.start.tokenIndex to context.stop.tokenIndex
    var current = from + 1
    while (current < to) {
        val token = tokenStream.get(current)
        if (token.text == "{") {
            return token
        }
        current++
    }
    throw IllegalStateException("Body of this object is not found! ${context.text}")
}

// todo: sloppy and innacurate
private fun findPrevDeclaration(tokenStream: CommonTokenStream, member: ParserRuleContext): Token? {
    var current = member.start.tokenIndex - 1
    while(current >= 0) {
        val token = tokenStream.get(current)
        // not hidden, not blank, not new line
        if (token.channel != 1 && !token.text.isBlank()) {
            return token
        }
        current--
    }
    return null
}

private fun extractStatements(tokens: List<Token>): List<SnippetMarkdown> {
    val result = mutableListOf<SnippetMarkdown>()
    val statementsText = tokens.joinToString(separator = "") { it.text }
    lateinit var statements: List<ParserRuleContext>
    statementsParserCache.useParser(statementsText) { parser, _ ->
        statements = locateStatements(parser)
    }
    for (statement in statements) {
        if (statement.text.isBlank() || statement.text.contains("todo:")) {
            continue
        }
        when (statement) {
            is StatementsParser.DelimitedCommentContext ->
                result.add(SnippetMarkdown(statement.text.removePrefix("/*").removeSuffix("*/").trim()))
            is StatementsParser.LineCommentContext ->
                result.add(SnippetMarkdown(statement.text.removePrefix("//").trim()))
            is StatementsParser.CodeContext -> {
                var code = statement.text
                while (code[0] == '\n' || code[0] == '\r') code = code.removeRange(0..1)
                result.add(SnippetMarkdown(CODE_TEMPLATE.format(code.trimEnd())))
            }
            else -> TODO()
        }
    }
    return result
}

private fun locateStatements(parser: StatementsParser): List<ParserRuleContext> {
    val result = mutableListOf<ParserRuleContext>()
    object : StatementsBaseVisitor<Unit>() {
        override fun visitLineComment(ctx: StatementsParser.LineCommentContext?) {
            result.add(ctx!!)
        }

        override fun visitDelimitedComment(ctx: StatementsParser.DelimitedCommentContext?) {
            result.add(ctx!!)
        }

        override fun visitCode(ctx: StatementsParser.CodeContext?) {
            result.add(ctx!!)
        }
    }.visitStatements(parser.statements())
    return result
}

private fun parseLocation(root: File, url: String, location: String): Location {
    val noDots = location.replace(".", "/")
    val withHome = noDots.replace("~", HOME_DIR)
    val (filename, identifier) = withHome.split("::")
    val file = File("$filename.kt")
    check(file.exists()) { "File do not exists: $location" }
    return Location(root, file, identifier, url)
}

private fun renderHtml(markdown: SnippetMarkdown) =
    SnippetHtml(flexmarkRenderer.render(flexmarkParser.parse(markdown.markdown)))

private fun extractArguments(scenario: File): ScenarioArguments {
    val lines = ArrayList(scenario.readLines())
    val root = File(lines.removeAt(0))
    check(root.exists()) { "Root doesn't exists: $root" }
    val url = lines.removeAt(0)
    while (lines[0].isBlank()) lines.removeAt(0)
    return ScenarioArguments(root, url, lines)
}

// Scenario file starts with a set of arguments: github url, repo path, etc.
private fun renderScenario(scenario: File, output: File) {
    val (root, url, lines) = extractArguments(scenario)
    // Next, we want to identify and apply the meta commands
    val withCommands = identifyCommands(lines)
    val onlyMarkdown = applyCommands(withCommands, root, url)
    lateinit var htmlSnippets: List<SnippetHtml>
    // When we have a final markdown, it can be rendered to html in parallel manner
    runBlocking {
        val deferred = mutableListOf<Deferred<SnippetHtml>>()
        for (snippetMarkdown in onlyMarkdown) {
            deferred.add(async(Dispatchers.Default) { renderHtml(snippetMarkdown) })
        }
        htmlSnippets = deferred.awaitAll()
    }
    // And flushed into an output file
    renderFile(output, htmlSnippets)
}

private fun renderFile(output: File, snippets: List<SnippetHtml>) {
    if (output.exists()) {
        output.delete()
    }
    for (snippet in snippets) {
        output.appendText(snippet.html)
    }
}

// Main function starts with the measurement
fun main() {
    val millis = measureTimeMillis {
        runBlocking {
            // We list all the scenarios in the input folder and launch tasks asynchronously:
            val scenarios = inputFolder.list()!!
            val deferred = mutableListOf<Deferred<Unit>>()
            for (scenario in scenarios) {
                val scenarioFile = File(inputFolder, scenario)
                val outputFile = File(outputFolder, "$scenario.html")
                check(scenarioFile.exists() && scenarioFile.isFile)
                deferred.add(async(Dispatchers.Default) { renderScenario(scenarioFile, outputFile) })
            }
            // Joining the fork for launched tasks:
            deferred.awaitAll()
        }
    }
    // Here I usually notice that my optimizations are futile
    println("Finished in %.2f seconds".format(millis / 1000f))
}