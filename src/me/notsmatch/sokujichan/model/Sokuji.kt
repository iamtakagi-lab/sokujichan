package me.notsmatch.sokujichan.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import me.notsmatch.sokujichan.Config
import me.notsmatch.sokujichan.service.BotService
import me.notsmatch.sokujichan.util.JsonUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.TextChannel
import org.bson.Document
import java.util.*

class Sokuji(val guildId: Long,
             val channelId: Long,
             val races: MutableList<Race>,
             val teamA: String,
             val teamB: String,
             var addedScoreA: Int,
             var addedScoreB: Int,
             var penaltyScoreA: Int,
             var penaltyScoreB: Int,
             var afterRace: Int,
             var raceSize: Int = 12){


    fun start() {
        val channel = getTextChannel()?: return
        channel.sendMessage(EmbedBuilder().apply {
            setColor(Config.EMBED_COLOR)
            setAuthor("即時集計を開始しました")
            setTitle("$teamA vs $teamB")
            setDescription(
                "配信用 Overlay (OBSでご利用可能です): ${getOverlayUrl()}\n")
        }.build()).queue()
    }

    fun getOverlayUrl() : String{
        return "https://sokujichan.herokuapp.com/$guildId-$channelId"
    }

    fun getTextChannel() : TextChannel? {
        val guild = BotService.instance.jda.getGuildById(guildId) ?: return null
        return guild.getTextChannelById(channelId) ?: return null
    }

    fun sendMessage(message: String) {
        val channel = getTextChannel() ?:return
        channel.sendMessage(message).queue()
    }

    fun sendMessage(vararg messages: String) {
        val channel = getTextChannel() ?:return
        messages.forEach { channel.sendMessage(it).queue() }
    }

    /**
     * 集計をチャンネルに送信します
     */
    fun send() {
        val race = races.get(races.size-1)
        sendMessage(getRaceScore(race), getTotalScore(), getRaceScores())
    }

    fun getRacesLeft() : Int {
        return raceSize - races.size
    }

    fun getDifSign(dif: Int) : String {
        return when {
            dif == 0 -> {
                "±0"
            }
            dif < 0 -> {
                "$dif"
            }
            dif > 0 -> {
                "+" + dif
            }
            else -> ""
        }
    }

    /**
     * @return レースのスコア
     */
    fun getRaceScore(race: Race) : String {
        race.apply {
            val a = spotsA.getScore()
            val b = spotsB.getScore()
            return StringBuilder()
                .append("```\n")
                .append("レース順位: " + spotsA.format())
                .append("\n\n")
                .append("$teamA $a - $b $teamB\n")
                .append("点差: ${getDifSign(a.minus(b))}\n")
                .append("```").toString()
        }
    }

    /**
     * @return 全レースのスコア
     */
    fun getTotalScore() : String {

        val a = getScoreA()
        val b = getScoreB()

        return StringBuilder()
             .append("```\n")
             .append("${if(afterRace == 0) races.size else afterRace}レース終了: 得点\n")
             .append("$teamA: ${a}\n")
             .append("$teamB: ${b}\n")
             .append("点差: ${getDifSign(a.minus(b))}\n")
             .append("```").toString()
    }

    /**
     * @return スコア履歴
     */
    fun getRaceScores() : String {
        val s = StringBuilder()
        s.append("```\n")

        for(i in races.indices){
            races[i].apply {
                val a = spotsA.getScore()
                val b = spotsB.getScore()
                s.append("${i+1} | $a - $b (${getDifSign(a.minus(b))})\n")
            }
        }

        s.append("```")

        return s.toString()
    }

    /**
     * 保存します
     */
    fun save() {
        BotService.mongoService.replaceSokuji(guildId, channelId, toDocument())
    }

    /**
     * @return Aチーム 全てのスコア
     */
    fun getScoreA() : Int {
        var score = 0
        races.forEach {
            score+=it.spotsA.getScore()
        }
        return score+addedScoreA-penaltyScoreA
    }

    /**
     * @return Bチーム 全てのスコア
     */
    fun getScoreB() : Int {
        var score = 0
        races.forEach {
            score+=it.spotsB.getScore()
        }
        return score+addedScoreB-penaltyScoreB
    }

    fun getTeam(team: String) : String{
        if(teamA.equals(team, true)){
            return "a"
        }
        else if(teamB.equals(team, true)){
            return "b"
        }
        return ""
    }

    fun containsTeam(team: String) : Boolean {
        if(team.equals(teamA, true) || team.equals(teamB, true)){
            return true
        }
        return false
    }


    fun toDocument() : Document {
        return Document().apply {
            put("guildId", guildId)
            put("channelId", channelId)
            put("teamA", teamA)
            put("teamB", teamB)
            put("addedScoreA", addedScoreA)
            put("addedScoreB", addedScoreB)
            put("penaltyScoreA", penaltyScoreA)
            put("penaltyScoreB", penaltyScoreB)
            put("afterRace", afterRace)
            put("raceSize", raceSize)

            val racesArray = JsonArray()
            races.forEach {
                racesArray.add(it.toJsonObject())
            }

            put("races", racesArray.toString())
        }
    }

    companion object {

        fun fromDocument(document: Document) : Sokuji {
            document.apply {

                val races = mutableListOf<Race>()

                if(getString("races") != null) {
                    val raceList: JsonArray = JsonUtils.JSON_PARSER.parse(getString("races")).asJsonArray
                    for (raceData in raceList) {
                        val race = Race.fromJson(raceData as JsonObject)
                        races.add(race)
                    }
                }

                return Sokuji(
                    getLong("guildId"),
                    getLong("channelId"),
                    races,
                    getString("teamA"),
                    getString("teamB"),
                    getInteger("addedScoreA"),
                    getInteger("addedScoreB"),
                    getInteger("penaltyScoreA"),
                    getInteger("penaltyScoreB"),
                    getInteger("afterRace"),
                    getInteger("raceSize")
                )
            }
        }
    }
}