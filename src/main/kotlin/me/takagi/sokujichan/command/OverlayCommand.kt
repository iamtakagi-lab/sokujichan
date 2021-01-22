package me.takagi.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.takagi.sokujichan.Config
import me.takagi.sokujichan.model.Sokuji
import net.dv8tion.jda.api.EmbedBuilder

class OverlayCommand: Command() {

    init {
        this.name = "overlay"
        this.help = "配信用OverlayURL"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val sokuji = Sokuji.get(guild.idLong, channel.idLong)?: return reply("即時集計は開始されていません")
            sokuji.apply {
                reply(EmbedBuilder().apply {
                    setColor(Config.EMBED_COLOR)
                    setTitle("$teamA vs $teamB")
                    addField("Stream Overlay", getOverlayUrl(), false)
                    addField("Guide", "[Click to View](${Config.GUIDE_URL})", false)
                }.build())
            }
        }
    }
}