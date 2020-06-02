package me.notsmatch.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.notsmatch.sokujichan.service.SokujiService
import me.notsmatch.sokujichan.util.NumberUtils
import org.apache.commons.lang3.StringUtils

class PenaltyCommand(val sokujiService: SokujiService)  : Command() {

    init {
        this.name = "penalty"
        this.help = "ペナルティを与えます"
        this.arguments = "<team> <amount>"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)
            val sokuji = sokujiService.getSokuji(guild.idLong, channel.idLong) ?: return reply("即時集計は開始されてません")

            if(args.size >= 2) {

                if(!sokuji.containsTeam(args[0])){
                    return reply("正しいチーム名を入力してください")
                }

                if(!NumberUtils.isInteger(args[1])){
                    return reply("整数で入力してください")
                }

                if(args[1].toInt() < 1){
                    return reply("1以上の整数で入力してください")
                }

                when(sokuji.getTeam(args[0])){
                    "a" -> {
                        sokuji.penaltyScoreA+=args[1].toInt()
                        reply("チーム ${sokuji.teamA} にペナルティ -${args[1]} を与えました")
                    }
                    "b" -> {
                        sokuji.penaltyScoreB += args[1].toInt()
                        reply("チーム ${sokuji.teamB} にペナルティ -${args[1]} を与えました")
                    }
                }

                sokuji.save()

                sokuji.sendMessage(sokuji.getTotalScore())

            }else{

                reply("``_penalty <team> <amount>``")
            }
        }
    }
}