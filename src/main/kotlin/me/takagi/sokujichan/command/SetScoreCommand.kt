package me.takagi.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import kotlinx.coroutines.runBlocking
import me.takagi.sokujichan.model.Sokuji
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils

class SetScoreCommand: Command() {

    init {
        this.name = "setscore"
        this.help = "得点をセットします"
        this.arguments = "<scoreA> <scoreB> <race>"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)

            runBlocking {
                val sokuji = Sokuji.find(guild.idLong, channel.idLong) ?: return@runBlocking reply("即時集計は開始されていません")

                if (args.size >= 3) {

                    if (!NumberUtils.isNumber(args[0]) || !NumberUtils.isNumber(args[1]) || !NumberUtils.isNumber(args[2])) {
                        return@runBlocking reply("整数で入力してください")
                    }

                    if (args[0].toInt() < 0 || args[1].toInt() < 0) {
                        return@runBlocking reply("点数は、0以上の整数で入力してください")
                    }

                    if (args[2].toInt() < 1) {
                        return@runBlocking reply("レース数は1以上の整数で入力してください")
                    }

                    sokuji.addedScoreA = args[0].toInt()
                    sokuji.addedScoreB = args[1].toInt()
                    sokuji.afterRace = args[2].toInt()

                    sokuji.sendMessage(sokuji.getTotalScore())

                } else {
                    reply("``_setscore <scoreA> <scoreB> <race>``")
                }
            }
        }
    }
}