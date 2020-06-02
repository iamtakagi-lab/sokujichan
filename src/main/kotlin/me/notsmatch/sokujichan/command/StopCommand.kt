package me.notsmatch.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.notsmatch.sokujichan.service.SokujiService

class StopCommand(val sokujiService: SokujiService) : Command() {

    init {
        this.name = "stop"
        this.help = "即時集計を終了します"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            if(sokujiService.removeSokuji(guild.idLong, channel.idLong)){
                reply("即時集計を終了しました")
            }else{
                reply("即時集計は開始されていません")
            }
        }
    }
}