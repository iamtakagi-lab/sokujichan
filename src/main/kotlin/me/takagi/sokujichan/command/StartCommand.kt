package me.takagi.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.takagi.sokujichan.model.Sokuji
import org.apache.commons.lang3.StringUtils

class StartCommand: Command() {

    init {
        this.name = "start"
        this.help = "集計を開始します | teamAに自分のチームを入力してください"
        this.arguments = "<teamA> <teamB>"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val args = StringUtils.split(args)
            if(args.size >= 2) {

                if(args[0].equals(args[1], true)){
                    return reply("teamAとteamBの名前は別のものにしてください")
                }

                val sokuji = Sokuji.add(Sokuji(guild.idLong, channel.idLong, args[0], args[1])) ?: return reply("既に即時集計が開始されています")
                sokuji.start()
            }else{
                reply("``_start <teamA> <teamB>``\nteamAに自分のチームを入力してください")
            }
        }
    }
}