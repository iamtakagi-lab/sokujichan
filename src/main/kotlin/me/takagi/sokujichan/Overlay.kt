package me.takagi.sokujichan

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.css.*
import kotlinx.html.*
import me.takagi.sokujichan.model.Sokuji
import org.apache.commons.lang3.math.NumberUtils

fun Route.getOverlay() {

    static ("static") {
        static("css"){
            resources("css")
        }
        static("icon"){
            resources("icon")
        }
    }
    
    get {
        call.respondRedirect("https://github.com/riptakagi/sokujichan")
    }

    get("status") {
        call.respond(HttpStatusCode.OK, mapOf("status" to "ok"))
    }

    get("{id}") {
        val id = call.parameters["id"]!!.toString().split("-")

        if(!NumberUtils.isNumber(id[0]) || !NumberUtils.isNumber(id[1])) {
            return@get call.respondHtml(HttpStatusCode.NotFound) {
                head {
                    title { +"データが見つかりません / 即時ちゃん(6vs6)" }
                    link(rel = "icon", href = "/static/icon/favicon.ico", type = "image/x-icon")
                    link(rel = "shortcut icon", href = "/static/icon/favicon.ico", type = "image/x-icon")
                    styleLink("/static/css/main.css")

                    meta {
                        name = "viewport"
                        content = "width=device-width, initial-scale=1"
                    }
                }
                body {
                    div("overlay") {
                        p("error") {
                            +"[Error] 404 Not Found: データが見つかりません"
                        }
                    }
                }
            }
        }

        val guildId = id[0].toLong()
        val channelId = id[1].toLong()

        val sokuji: Sokuji = Sokuji.get(guildId, channelId) ?: return@get call.respondHtml(HttpStatusCode.NotFound) {
            head {
                title { +"データが見つかりません / 即時ちゃん(6vs6)" }
                link ( rel = "icon", href = "/static/icon/favicon.ico", type = "image/x-icon")
                link ( rel = "shortcut icon", href = "/static/icon/favicon.ico", type = "image/x-icon")
                styleLink("/static/css/main.css")

                meta {
                    name = "viewport"
                    content = "width=device-width, initial-scale=1"
                }
            }

            body {
                div("overlay") {
                    p("error") {
                        +"[Error] 404 Not Found: データが見つかりません"
                    }
                }
            }
        }

        sokuji.apply {
            return@get call.respondHtml {
                head {
                    title { +"$teamA vs $teamB / 即時ちゃん(6vs6)" }
                    styleLink("/static/css/main.css")
                    link ( rel = "icon", href = "/static/icon/favicon.ico", type = "image/x-icon")
                    link ( rel = "shortcut icon", href = "/static/icon/favicon.ico", type = "image/x-icon")

                    meta {
                        httpEquiv = "refresh"
                        content = "1"
                    }

                    meta {
                        name = "viewport"
                        content = "width=device-width, initial-scale=1"
                    }
                }
                body {
                    div("overlay"){
                         div("header") {
                            p("dif " + if(getScoreA().minus(getScoreB()) < 0) "minus" else if(getScoreA().minus(getScoreB()) > 0) "plus" else "plus-minus") {
                                +getDifSign(getScoreA().minus(getScoreB()))
                            }
                            p("races-left") {
                                +"残レース:${getRacesLeft()}"
                            }
                            if (isWinDetermine()) {
                                p("win-determine") {
                                    +" WIN"
                                }
                            } else if (isLoseDetermine()) {
                                p("lose-determine") {
                                    +" LOSE"
                                }
                            }
                        }
                        div("body") {
                            p("team-a") {
                                +teamA
                            }
                            p("score-a") {
                                +"${getScoreA()}"
                            }
                            p("split") {
                                +"-"
                            }
                            p("score-b") {
                                +"${getScoreB()}"
                            }
                            p("team-b") {
                                +teamB
                            }
                        }
                    }
                }
            }
        }
    }
}

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style(type = ContentType.Text.CSS.toString()) {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
