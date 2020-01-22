package com.f3401pal.mathwiz

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.webjars.*
import io.ktor.routing.*
import io.ktor.locations.*
import io.ktor.server.engine.*
import io.ktor.jackson.*
import io.ktor.html.*
import java.io.File
import java.util.TimeZone
import org.slf4j.LoggerFactory
import com.f3401pal.mathwiz.QuizGenerator
import com.f3401pal.mathwiz.Operator

private val logger = LoggerFactory.getLogger("Application");

// Entry Point of the application as defined in resources/application.conf.
// @see https://ktor.io/servers/configuration.html#hocon-file
fun Application.main() {
    // This adds Date and Server headers to each response, and allows custom additional headers
    install(DefaultHeaders)
    // This uses the logger to log every call (request/response)
    install(CallLogging)
    install(Locations)
    install(Webjars) {
        zone = TimeZone.getTimeZone("MST").toZoneId()
    }
    install(ContentNegotiation) {
        jackson {

        }
    }
    // Registers routes
    routing {
        // Here we use a DSL for building HTML on the route "/"
        // @see https://github.com/Kotlin/kotlinx.html
        get("/demo") {
            call.respondText("Hello World!", ContentType.Text.Plain)
        }
        serveHtml()
        serveApi()
    }
}

private fun Route.serveHtml() {
    
}

private fun Route.serveApi() {
    route("/api") {
        get<EquationQuizConfig> { config ->
            val result = QuizGenerator.generateEquationQuiz(config)
            call.respond(result.map {
                it.presentation
            })
        }
    }
}

@Location("/quiz")
data class EquationQuizConfig(
    val max: Int = Integer.MAX_VALUE,
    val size: Int = 50,
    val operators: List<String> = Operator.values().map { it.toString() }
)
