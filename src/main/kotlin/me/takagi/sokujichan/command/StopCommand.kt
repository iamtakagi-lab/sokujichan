package me.takagi.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.takagi.sokujichan.model.Sokuji

class StopCommand : Command() {

    init {
        this.name = "stop"
        this.help = "即時集計を終了します"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            if(Sokuji.remove(Sokuji.find(guild.idLong, channel.idLong))){
                reply("即時集計を終了しました")
            }else{
                reply("即時集計は開始されていません")
            }
        }
    }
}