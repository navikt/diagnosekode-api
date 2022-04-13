package no.nav.k9

import io.ktor.application.*
import io.ktor.http.ContentType
import io.ktor.request.httpMethod
import io.ktor.request.uri
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import no.nav.k9.utils.DiagnosekodeUtil
import org.slf4j.LoggerFactory
import com.google.gson.Gson
import io.ktor.features.CORS
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import no.nav.k9.extensions.safeSubList
import no.nav.k9.extensions.getMatchingEntries
import no.nav.syfo.sm.Diagnosekoder
import java.net.URI

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

private val logger = LoggerFactory.getLogger("no.nav.k9.DiagnosekodeApi")

val diagnosekoder = DiagnosekodeUtil.transformValues(Diagnosekoder.icd10)

fun Application.DiagnosekodeApi() {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Get)

        environment.config.property("nav.cors_allow_list").getString().split(',').map { URI.create(it) }.forEach {
            log.info("Legger til host {} med scheme {}", it.host, it.scheme)
            host(host = it.authority, schemes = listOf(it.scheme))
        }
    }

    install(Routing) {
        get("/diagnosekoder") {
            logger.info("${call.request.httpMethod.value}@${call.request.uri}")

            val query = call.request.queryParameters["query"]
            var max = call.request.queryParameters["max"]?.toIntOrNull()

            if (query != null) {
                val matches = diagnosekoder.getMatchingEntries(query)
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

        get("/isAlive") {
            logger.debug("alive")
            call.respondText("ALIVE")
        }

        get("/isReady") {
            logger.debug("ready")
            call.respondText("READY")
        }
    }
}
