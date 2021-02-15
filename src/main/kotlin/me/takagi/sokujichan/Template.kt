package me.takagi.sokujichan

import io.ktor.html.*
import kotlinx.html.*

class OverlayTemplate(val headTitle: String) : Template<HTML> {

    val flow = Placeholder<FlowContent>()

    override fun HTML.apply() {
        head {
            title { +"$headTitle / sokujichan" }
            styleLink("/static/css/main.css")
            link(rel = "icon", href = "/static/icon/favicon.ico", type = "image/x-icon")
            link(rel = "shortcut icon", href = "/static/icon/favicon.ico", type = "image/x-icon")

            meta {
                httpEquiv = "refresh"
                content = "1"
            }
        }

        body {
            div("overlay") {
                insert(flow)
            }
        }
    }
}