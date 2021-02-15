package me.takagi.sokujichan.command

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import kotlinx.coroutines.runBlocking
import me.takagi.sokujichan.common.Env
import me.takagi.sokujichan.model.Sokuji
import net.dv8tion.jda.api.EmbedBuilder

class OverlayCommand: Command() {

    init {
        this.name = "overlay"
        this.help = "配信用OverlayURL"
    }

    override fun execute(event: CommandEvent?) {
        event?.apply {
            runBlocking {
                val sokuji = Sokuji.find(guild.idLong, channel.idLong)?: return@runBlocking reply("即時集計は開始されていません")
                sokuji.apply {
                    reply(EmbedBuilder().apply {
                        setColor(Env.EMBED_COLOR)
                        setTitle("$teamA vs $teamB")
                        addField("Stream Overlay", getOverlayUrl(), false)
                        addField("Guide", "[Click to View](https://github.com/iam-takagi/sokujichan)", false)
                    }.build())
                }
            }
        }
    }
}