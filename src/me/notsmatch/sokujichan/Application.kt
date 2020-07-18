package me.notsmatch.sokujichan

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.util.KtorExperimentalAPI
import io.ktor.websocket.WebSockets
import me.notsmatch.sokujichan.controller.renderingController
import me.notsmatch.sokujichan.model.Sokuji
import me.notsmatch.sokujichan.service.BotService
import me.notsmatch.sokujichan.service.SokujiService

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.module() {

    install(DefaultHeaders)
    install(CallLogging)
    install(WebSockets)
    install(Locations)

    val sokujiService = SokujiService()
    val botService = BotService(System.getenv("SOKUJICHAN_TOKEN"), sokujiService,false).start()

    routing {
        renderingController(sokujiService)
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Post)
        method(HttpMethod.Patch)
        header(HttpHeaders.AccessControlAllowHeaders)
        header(HttpHeaders.ContentType)
        header(HttpHeaders.AccessControlAllowOrigin)
        allowCredentials = true
        anyHost()
    }

    install(ContentNegotiation) {
        gson {
            // Configure Gson here
        }
    }
}

