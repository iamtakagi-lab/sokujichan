package me.notsmatch.sokujichan.controller

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.*
import io.ktor.response.respondRedirect
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import kotlinx.css.*
import kotlinx.css.properties.*
import kotlinx.html.*
import me.notsmatch.sokujichan.model.Sokuji
import me.notsmatch.sokujichan.service.SokujiService
import org.apache.commons.lang3.math.NumberUtils
import java.io.File
import java.time.format.TextStyle
import java.util.*

fun Route.renderingController(sokujiService: SokujiService) {

    static ("static") {
        staticRootFolder = File("static")
        files(".")
    }
    
    get {
        call.respondRedirect("https://github.com/riptakagi/sokujichan")
    }

    get("{id}") {
        val id = call.parameters["id"]!!.toString().split("-")

        if(!NumberUtils.isNumber(id[0]) || !NumberUtils.isNumber(id[1])){
            return@get call.respondHtml(HttpStatusCode.InternalServerError) {
                head {
                    title { +"正しいIDを入力してください / 即時ちゃん(6vs6)" }
                    styleLink("/static/styles.css")
                }
                body {
                    div {
                        section {
                            p {
                                +"[Error] 500 Internal Server Error: 正しいIDを入力してください"
                            }
                        }
                    }
                }
            }
        }

        val guildId = id[0].toLong()
        val channelId = id[1].toLong()

        val sokuji: Sokuji = sokujiService.getSokuji(guildId, channelId) ?: return@get call.respondHtml(HttpStatusCode.NotFound) {
            head {
                title { +"データが見つかりません / 即時ちゃん(6vs6)" }
                styleLink("/static/styles.css")
            }
            body {
                div {
                    section {
                        p {
                            +"[Error] 404 Not Found: データが見つかりません"
                        }
                    }
                }
            }
        }

        sokuji.apply {
            return@get call.respondHtml {
                head {
                    title { +"$teamA vs $teamB / 即時ちゃん(6vs6)" }
                    styleLink("/static/styles.css")
                    link ( rel = "icon", href = "/static/favicon.ico", type = "image/x-icon")
                    link ( rel = "shortcut icon", href = "/static/favicon.ico", type = "image/x-icon")
                    meta {
                        httpEquiv = "refresh"
                        content = "5"
                    }
                }
                body {
                    div {
                        section {
                            p {
                                +"$teamA ${getScoreA()} - ${getScoreB()} $teamB (${getDifSign(getScoreA().minus(getScoreB()))} 残レース:${getRacesLeft()})"
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
