package me.takagi.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import kotlinx.coroutines.runBlocking
import me.takagi.sokujichan.model.Sokuji
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils

class RevertScoreCommand: Command() {

    init {
        this.name = "revertscore"
        this.help = "得点を取り消します"
        this.arguments = "<race>"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)

            runBlocking {
                val sokuji = Sokuji.find(guild.idLong, channel.idLong) ?: return@runBlocking reply("即時集計は開始されていません")

                if (args.isNotEmpty()) {

                    if (!NumberUtils.isNumber(args[0])) {
                        return@runBlocking reply("整数で入力してください")
                    }

                    if (args[0].toInt() < 1) {
                        return@runBlocking reply("1以上の整数で入力してください")
                    }

                    val i = args[0].toInt() - 1

                    if (sokuji.races.size < args[0].toInt()) {
                        return@runBlocking reply("レースが存在しません")
                    }

                    sokuji.races.removeAt(i)
                    sokuji.save()

                    reply("レース: ${args[0]}の得点を取り消しました")
                } else {
                    reply("``_revertscore <race>``")
                }
            }
        }
    }
}