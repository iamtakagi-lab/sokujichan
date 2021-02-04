package me.takagi.sokujichan

import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.util.KtorExperimentalAPI


@KtorExperimentalAPI
fun Application.module() {

    val bot by lazy {
        Bot(Env.TOKEN).start()
    }

    routing {
        getOverlay()
    }
}

