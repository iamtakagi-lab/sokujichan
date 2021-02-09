package me.takagi.sokujichan

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.routing.*
import me.takagi.sokujichan.common.Env
import me.takagi.sokujichan.common.createLogger
import me.takagi.sokujichan.endpoints.getIndex
import me.takagi.sokujichan.endpoints.getOverlay
import mu.KotlinLogging

fun Application.module() {

    install(CallLogging) {
        logger = KotlinLogging.createLogger("sokujichan.server")
        format { call ->
            when (val status = call.response.status()) {
                HttpStatusCode.Found -> "$status: ${call.request.toLogString()} -> ${call.response.headers[HttpHeaders.Location]}"
                null -> ""
                else -> "$status: ${call.request.httpMethod.value} ${call.request.uri}"
            }
        }
    }

    routing {
        route(Env.BASE_URI) {

            getIndex()

            static("static") {
                static("css") {
                    resources("css")
                }
                static("icon") {
                    resources("icon")
                }
            }

            route("overlay") {
                getOverlay()
            }
        }
    }
}

