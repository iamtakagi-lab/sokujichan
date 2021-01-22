package me.takagi.sokujichan

import io.ktor.application.*
import io.ktor.server.cio.CIO
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
}
