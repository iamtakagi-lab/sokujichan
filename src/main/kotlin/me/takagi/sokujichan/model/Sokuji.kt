package me.takagi.sokujichan.model

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import kotlinx.serialization.Serializable
import me.takagi.sokujichan.bot.Bot
import me.takagi.sokujichan.collection
import me.takagi.sokujichan.common.Env
import me.takagi.sokujichan.util.ScoreUtils
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import org.litote.kmongo.eq
import java.util.*

@Serializable
class Sokuji(
    val guildId: Long,
    val channelId: Long,
    val teamA: String,
    val teamB: String,
    val races: MutableList<Race> = mutableListOf(),
    var addedScoreA: Int = 0,
    var addedScoreB: Int = 0,
    var penaltyScoreA: Int = 0,
    var penaltyScoreB: Int = 0,
    var afterRace: Int = 0,
    var raceSize: Int = 12) {

    /**
     * レース毎の順位データ
     * @param spots 順位
     */
    @Serializable
    data class Race(val spotsA: Spots, val spotsB: Spots)

    /**
     * 順位管理、点数演算クラス
     * @param data 順位データ
     */
    @Serializable
    data class Spots(val data: List<Int>) {

        fun getScore(): Int {
            return ScoreUtils.getScore(data)
        }

        fun split(): String {
            val builder = StringBuilder()
            val it = data.iterator()
            while (it.hasNext()) {
                val spot = it.next()
                builder.append(spot)

                if (it.hasNext()) {
                    builder.append(":")
                }
            }

            return builder.toString()
        }

        fun format(): String {
            val builder = StringBuilder()
            data.forEach { builder.append("${it}位 ") }
            return builder.toString()
        }
    }

    suspend fun save() {
        save(this)
    }


    fun start() {
        val channel = getTextChannel() ?: return
        channel.sendMessage(EmbedBuilder().apply {
            setColor(Env.EMBED_COLOR)
            setAuthor("即時集計を開始しました")
            setTitle("$teamA vs $teamB")
            addField("Stream Overlay", getOverlayUrl(), false)
            addField("Guide", "[Click to View](https://github.com/iam-takagi/sokujichan)", false)
        }.build()).queue()
    }

    private val host: String
        get() = buildString {
            append("http")
            if (Env.HOSTNAME != null) {
                append("s")
            }
            append("://")

            if (Env.HOSTNAME != null) {
                append(Env.HOSTNAME)
            } else {
                append("localhost")
            }

            if(Env.PORT != 80) {
                append(":" + Env.PORT)
            }
        }

    fun getOverlayUrl(): String {
        return "${host}/overlay/$guildId/$channelId"
    }

    fun getTextChannel(): TextChannel? {
        val guild = Bot.instance.jda.getGuildById(guildId) ?: return null
        return guild.getTextChannelById(channelId) ?: return null
    }

    fun sendMessage(message: String) {
        val channel = getTextChannel() ?: return
        channel.sendMessage(message).queue()
    }

    fun sendMessage(vararg messages: String) {
        val channel = getTextChannel() ?: return
        messages.forEach { channel.sendMessage(it).queue() }
    }

    fun sendMessage(vararg messages: MessageEmbed) {
        val channel = getTextChannel() ?: return
        messages.forEach { channel.sendMessage(it).queue() }
    }

    /**
     * 集計をチャンネルに送信します
     */
    fun send() {
        val race = races.get(races.size - 1)
        sendMessage(
            buildEmbed("$teamA vs $teamB", fields =
            mapOf("レース" to getRaceScore(race),
                  "合計" to getTotalScore(),
                  "履歴" to getRaceScores(),
                  "状況" to getRaceContext(race)
            )
        ))
    }

    fun getRacesLeft(): Int {
        return raceSize - races.size
    }

    fun getDifSign(dif: Int): String {
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
     * 勝利確定かどうか
     */
    fun isWinDetermine(): Boolean {
        val possibleMaxDif = getRacesLeft() * 40
        val dif = getScoreA().minus(getScoreB())
        if (dif > possibleMaxDif) {
            return true
        }
        return false
    }

    /**
     * 敗北確定かどうか
     */
    fun isLoseDetermine(): Boolean {
        val possibleMaxDif = getRacesLeft() * 40
        val dif = getScoreB().minus(getScoreA())
        if (dif > possibleMaxDif) {
            return true
        }
        return false
    }

    fun buildEmbed(title: String? = null, author: String? = null, text: String? = null, fields: Map<String, String>) : MessageEmbed {
        return EmbedBuilder().apply {
            setColor(Env.EMBED_COLOR)
            setTitle(title)
            setAuthor(author)
            setDescription(text)
            fields.forEach { addField(it.key, it.value, false) }
            setTimestamp(Date().toInstant())
            setFooter("sokujichan")
        }.build()
    }

    fun getRaceContext(race: Race): String {
        race.apply {
            val a = spotsA.getScore()
            val b = spotsB.getScore()
            return "```$a-$b (${getDifSign(a - b)}) / 合計: ${teamA} ${getScoreA()}-${getScoreB()} ${teamB} / 点差: ${getDifSign(
                    getScoreA().minus(getScoreB())
                )} / @${getRacesLeft()} " +
                        "${if (isWinDetermine()) "勝利確定" else ""}  ${if (isLoseDetermine()) "敗北確定" else ""}```"
        }
    }

    /**
     * @return レースのスコア
     */
    fun getRaceScore(race: Race): String {
        race.apply {
            val a = spotsA.getScore()
            val b = spotsB.getScore()
            return buildString {
                append("```\n")
                append("レース順位: " + spotsA.format())
                append("\n\n")
                append("$teamA $a - $b $teamB\n")
                append("点差: ${getDifSign(a.minus(b))}\n")
                append("```").toString()
            }
        }
    }

    /**
     * @return 全レースのスコア
     */
    fun getTotalScore(): String {

        val a = getScoreA()
        val b = getScoreB()

        return buildString {
            append("```\n")
            append("${if (afterRace == 0) races.size else afterRace} / $raceSize レース終了\n")
            append("$teamA: ${a}\n")
            append("$teamB: ${b}\n")
            append("点差: ${getDifSign(a.minus(b))}\n")

            if (isWinDetermine()) {
                append("勝利確定")
            } else if (isLoseDetermine()) {
                append("敗北確定")
            }

            append("```").toString()
        }
    }

    /**
     * @return スコア履歴
     */
    fun getRaceScores(): String {
        return buildString {
            append("```\n")

            for (i in races.indices) {
                races[i].apply {
                    val a = spotsA.getScore()
                    val b = spotsB.getScore()
                    append("${i + 1} | $a - $b (${getDifSign(a.minus(b))})\n")
                }
            }
            append("```")
        }

    }

    /**
     * @return Aチーム 全てのスコア
     */
    fun getScoreA(): Int {
        var score = 0
        races.forEach {
            score += it.spotsA.getScore()
        }
        return score + addedScoreA - penaltyScoreA
    }

    /**
     * @return Bチーム 全てのスコア
     */
    fun getScoreB(): Int {
        var score = 0
        races.forEach {
            score += it.spotsB.getScore()
        }
        return score + addedScoreB - penaltyScoreB
    }

    fun getTeam(team: String): String {
        if (teamA.equals(team, true)) {
            return "a"
        } else if (teamB.equals(team, true)) {
            return "b"
        }
        return ""
    }

    fun containsTeam(team: String): Boolean {
        if (team.equals(teamA, true) || team.equals(teamB, true)) {
            return true
        }
        return false
    }

    companion object {

        suspend fun getListByGuild(guildId: Long) : List<Sokuji> {
            return collection.find(Sokuji::guildId eq guildId).toList()
        }

        suspend fun find(guildId: Long, channelId: Long) : Sokuji? {
            return collection.findOne(Sokuji::guildId eq guildId, Sokuji::channelId eq channelId)
        }

        suspend fun save(sokuji: Sokuji) : Sokuji{
            collection.replaceOne(
                Filters.and(Filters.eq("guildId", sokuji.guildId), Filters.eq("channelId", sokuji.channelId)),
                sokuji,
                ReplaceOptions().upsert(true)
            )
            return sokuji
        }

        suspend fun remove(guildId: Long, channelId: Long): Boolean {
            return collection.deleteOne(Filters.and(Filters.eq("guildId", guildId), Filters.eq("channelId", channelId))).wasAcknowledged()
        }

        suspend fun removeAll(guildId: Long) {
            collection.deleteMany(Sokuji::guildId eq guildId)
        }
    }
}