package me.takagi.sokujichan.endpoints

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.cio.websocket.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import kotlinx.css.*
import kotlinx.html.*
import me.takagi.sokujichan.OverlayTemplate
import me.takagi.sokujichan.common.createLogger
import me.takagi.sokujichan.model.Sokuji
import mu.KotlinLogging

private val logger = KotlinLogging.createLogger("sokujichan.endpoints")

fun Route.getOverlay() {

    get("/{guildId}/{channelId}") {
        val guildId: Long by call.parameters
        val channelId: Long by call.parameters

        val sokuji: Sokuji =
            Sokuji.find(guildId, channelId) ?: return@get call.respondHtmlTemplate(OverlayTemplate("Not Found")) {
                flow {
                    p("error") {
                        +"[Error] 404 Not Found: データが見つかりません"
                    }
                }
            }
        sokuji.apply {
            call.respondHtmlTemplate(OverlayTemplate("$teamA vs $teamB")) {
                sokuji.apply {
                    flow {
                        sokuji.apply {
                            div("header") {
                                p(
                                    "dif " + when {
                                        getScoreA().minus(getScoreB()) < 0 -> "minus"
                                        getScoreA().minus(
                                            getScoreB()
                                        ) > 0 -> "plus"
                                        else -> "plus-minus"
                                    }
                                ) {
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
}