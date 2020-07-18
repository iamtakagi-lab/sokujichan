package me.notsmatch.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.notsmatch.sokujichan.service.SokujiService
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.math.NumberUtils

class SetRaceSizeCommand (val sokujiService: SokujiService): Command() {

    init {
        this.name = "setracesize"
        this.help = "レース数を設定します"
        this.arguments = "<race>"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)
            val sokuji = sokujiService.getSokuji(guild.idLong, channel.idLong) ?: return reply("即時集計は開始されていません")

            if(args.isNotEmpty()) {

                if(!NumberUtils.isNumber(args[0])){
                    return reply("レース数は整数で入力してください")
                }

                if(args[0].toInt() < 1){
                    return reply("レース数は1以上の整数で入力してください")
                }

                sokuji.raceSize = args[0].toInt()

                sokuji.save()

                sokuji.sendMessage("レース数を設定しました: ${sokuji.raceSize}")

            }else{
                reply("``_setracesize <race>``")
            }
        }
    }
}