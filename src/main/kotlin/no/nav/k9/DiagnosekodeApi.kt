package no.nav.k9

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.k9.extensions.getMatchingEntries
import no.nav.k9.extensions.safeSubList
import no.nav.k9.utils.DiagnosekodeUtil
import no.nav.helse.diagnosekoder.Diagnosekoder
import org.slf4j.LoggerFactory
import org.slf4j.event.*

fun main(args: Array<String>): Unit = io.ktor.server.jetty.EngineMain.main(args)

private val logger = LoggerFactory.getLogger("no.nav.k9.DiagnosekodeApi")

private val diagnosekoder = DiagnosekodeUtil.transformValues(Diagnosekoder.icd10)
private val diagnoseKodePattern = ".\\d{3}"

fun Application.DiagnosekodeApi() {
    install(CallLogging) {
        logger = logger
        level = Level.INFO
        filter { call ->
            call.request.path().startsWith("/")
            !call.request.path().contains("internal")
        }
        disableDefaultColors()
    }

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowHost(
            host = "adeo.no",
            schemes = listOf("http", "https"),
            subDomains = listOf("app-q1", "app")
        )
    }

    install(Routing) {
        get("/diagnosekoder") {
            val query = call.request.queryParameters["query"]
            var max = call.request.queryParameters["max"]?.toIntOrNull()

            if (query != null) {
                val diagnoseKode = diagnoseKodePattern.toRegex().find(query)?.value

                val matches = diagnosekoder.getMatchingEntries(diagnoseKode ?: query)
                val diagnoseList = matches.values.toList().safeSubList(0, max)
                logger.info("Returnerer {} diagnosekoder", diagnoseList.size)
                call.respondText(
                    contentType = ContentType.Application.Json,
                    status = HttpStatusCode.OK,
                    text = Gson().toJson(diagnoseList)
                )
            } else {
                call.respondText(
                    status = HttpStatusCode.BadRequest,
                    text = "No query parameter was specified"
                )
            }
        }

        get("/internal/isAlive") {
            call.respondText("ALIVE")
        }

        get("/internal/isReady") {
            call.respondText("READY")
        }
    }
}
