package me.notsmatch.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.notsmatch.sokujichan.Config
import me.notsmatch.sokujichan.service.SokujiService
import net.dv8tion.jda.api.EmbedBuilder

class UrlCommand (val sokujiService: SokujiService): Command() {

    init {
        this.name = "url"
        this.help = "配信用OverlayURL"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val sokuji = sokujiService.getSokuji(guild.idLong, channel.idLong)?: return reply("即時集計は開始されていません")
            reply(EmbedBuilder().apply {
                setColor(Config.EMBED_COLOR)
                setTitle("配信用 Overlay")
                setDescription(sokuji.getOverlayUrl())
            }.build())
        }
    }
}