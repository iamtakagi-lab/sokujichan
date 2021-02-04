package me.takagi.sokujichan

import io.ktor.application.*
import io.ktor.server.cio.CIO
import io.ktor.server.engine.*

fun main(args: Array<String>) {
    embeddedServer(CIO,
        port = Env.PORT,
        host = Env.HOST,
        module = Application::module).start(wait = true)
}
