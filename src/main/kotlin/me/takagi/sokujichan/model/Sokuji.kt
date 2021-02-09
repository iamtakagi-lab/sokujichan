package me.takagi.sokujichan.model

import me.takagi.sokujichan.bot.Bot
import me.takagi.sokujichan.common.Env
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel

class Sokuji(val guildId: Long,
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

    companion object {

        private val list = mutableListOf<Sokuji>()

        fun find(guildId: Long, channelId: Long): Sokuji? {
            return list.find { it.guildId == guildId && it.channelId == channelId }
        }

        fun filter (guildId: Long): List<Sokuji> {
            return list.filter { it.guildId == guildId }
        }

        fun add(sokuji: Sokuji): Sokuji {
            list.add(sokuji)
            return sokuji
        }

        fun remove(sokuji: Sokuji?) : Boolean {
            return list.remove(sokuji)
        }

        fun removeAll(sokuji: List<Sokuji>) : Boolean {
            return list.removeAll(sokuji)
        }
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

    fun getOverlayUrl(): String {
        return "http://localhost:${Env.PORT}/overlay/$guildId/$channelId"
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
            getRaceScore(race),
            getTotalScore(),
            getRaceScores(),
            getRaceInfo(race)
        )
    }

    fun getRaceInfo(race: Race): String {
        race.apply {
            val a = spotsA.getScore()
            val b = spotsB.getScore()
            return "```$a-$b (${getDifSign(a - b)}) / 合計: ${teamA} ${getScoreA()}-${getScoreB()} ${teamB} / 点差: ${getDifSign(
                getScoreA().minus(getScoreB())
            )} / @${getRacesLeft()} " +
                    "${if (isWinDetermine()) "勝利確定" else ""}  ${if (isLoseDetermine()) "敗北確定" else ""}```"
        }
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

    /**
     * @return レースのスコア
     */
    fun getRaceScore(race: Race): String {
        race.apply {
            val a = spotsA.getScore()
            val b = spotsB.getScore()
            return StringBuilder().apply {
                append("```\n")
                append("レース順位: " + spotsA.format())
                append("\n\n")
                append("$teamA $a - $b $teamB\n")
                append("点差: ${getDifSign(a.minus(b))}\n")
                append("```").toString()
            }.toString()
        }
    }

    /**
     * @return 全レースのスコア
     */
    fun getTotalScore(): String {

        val a = getScoreA()
        val b = getScoreB()

        return StringBuilder().apply {
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
        }.toString()
    }

    /**
     * @return スコア履歴
     */
    fun getRaceScores(): String {
        val s = StringBuilder()
        s.append("```\n")

        for (i in races.indices) {
            races[i].apply {
                val a = spotsA.getScore()
                val b = spotsB.getScore()
                s.append("${i + 1} | $a - $b (${getDifSign(a.minus(b))})\n")
            }
        }

        s.append("```")

        return s.toString()
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

}