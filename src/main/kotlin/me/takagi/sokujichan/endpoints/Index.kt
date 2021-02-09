package me.takagi.sokujichan.endpoints

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getIndex() {

    get {
        call.respondRedirect("https://github.com/iam-takagi/sokujichan")
    }
}