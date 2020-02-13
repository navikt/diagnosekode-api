package no.nav.k9

import io.ktor.application.*
import io.ktor.request.httpMethod
import io.ktor.request.uri
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.util.KtorExperimentalAPI
import org.slf4j.LoggerFactory

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private val logger = LoggerFactory.getLogger("no.nav.k9.DiagnosekodeApi")

@KtorExperimentalAPI
fun Application.DiagnosekodeApi() {
    install(Routing) {
        get("/test") {
            logger.info("${call.request.httpMethod.value}@${call.request.uri}")
            call.respondText { "test" }
        }
    }
}
