package me.takagi.sokujichan

import io.ktor.application.*
import io.ktor.config.ApplicationConfig
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.WebSockets


@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.module() {

    val bot = Bot(System.getenv("TOKEN"),false).start()

    routing {
        getOverlay()
    }
}

