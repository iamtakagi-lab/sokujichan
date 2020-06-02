package me.notsmatch.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.notsmatch.sokujichan.service.SokujiService
import me.notsmatch.sokujichan.util.NumberUtils
import org.apache.commons.lang3.StringUtils

class SetScoreCommand (val sokujiService: SokujiService): Command() {

    init {
        this.name = "setscore"
        this.help = "得点をセットします"
        this.arguments = "<scoreA> <scoreB> <race>"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)
            val sokuji = sokujiService.getSokuji(guild.idLong, channel.idLong) ?: return reply("即時集計は開始されていません")

            if(args.size >= 3) {

                if(!NumberUtils.isInteger(args[0]) || !NumberUtils.isInteger(args[1]) || !NumberUtils.isInteger(args[2])){
                    return reply("整数で入力してください")
                }

                if(args[0].toInt() < 0 || args[1].toInt() < 0){
                    return reply("点数は、0以上の整数で入力してください")
                }

                if(args[2].toInt() < 1){
                    return reply("レース数は1以上の整数で入力してください")
                }

                sokuji.addedScoreA = args[0].toInt()
                sokuji.addedScoreB = args[1].toInt()
                sokuji.afterRace = args[2].toInt()

                sokuji.save()

                sokuji.sendMessage(sokuji.getTotalScore())

            }else{
                reply("``_setscore <scoreA> <scoreB> <race>``")
            }
        }
    }
}