package me.takagi.sokujichan

import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import me.takagi.sokujichan.command.*
import me.takagi.sokujichan.model.Sokuji
import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.Activity.watching
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import java.util.*

class Bot (val token: String) {

    companion object {
        @JvmStatic
        lateinit var instance: Bot
    }

    lateinit var jda: JDA
    val eventWaiter = EventWaiter()

    fun start() : Bot {
        instance = this
        jda = JDABuilder(AccountType.BOT).setToken(token).setStatus(OnlineStatus.ONLINE).build()

        val builder = CommandClientBuilder()

        builder.setOwnerId("695218967173922866")
        builder.setPrefix("_")

        builder.addCommands(
            StartCommand(),
            StopCommand(),
            RaceCommand(),
            RevertScoreCommand(),
            SetScoreCommand(),
            PenaltyCommand(),
            SetRaceSizeCommand(),
            OverlayCommand()
        )

        builder.setHelpWord("sokujichan")

        val client = builder.build()
        jda.addEventListener(Listener())
        jda.addEventListener(client)
        return this
    }
}

class Listener : ListenerAdapter() {

    override fun onGuildLeave(event: GuildLeaveEvent) {
        event.apply {
            Sokuji.removeAll(Sokuji.filter(guild.idLong))
        }
    }
}
