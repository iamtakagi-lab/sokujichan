package me.notsmatch.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.notsmatch.sokujichan.Config
import me.notsmatch.sokujichan.service.SokujiService
import net.dv8tion.jda.api.EmbedBuilder

class OverlayCommand (val sokujiService: SokujiService): Command() {

    init {
        this.name = "overlay"
        this.help = "配信用OverlayURL"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            val sokuji = sokujiService.getSokuji(guild.idLong, channel.idLong)?: return reply("即時集計は開始されていません")
            sokuji.apply {
                reply(EmbedBuilder().apply {
                    setColor(Config.EMBED_COLOR)
                    setTitle("$teamA vs $teamB")
                    addField("配信用 Overlay (OBSで利用可能)", getOverlayUrl(), false)
                    addField("使い方 / Guide", "[クリックして開く / Click to View](${Config.GUIDE_URL})", false)
                }.build())
            }
        }
    }
}