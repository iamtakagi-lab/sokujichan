package me.takagi.sokujichan

import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.mongodb.client.model.Filters
import me.takagi.sokujichan.command.*
import me.takagi.sokujichan.model.Sokuji
import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.Activity.watching
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import java.util.*

class Bot (val token: String, val dev: Boolean) {

    companion object {
        @JvmStatic
        lateinit var instance: Bot
    }

    lateinit var jda: JDA
    val eventWaiter = EventWaiter()
    val WEBSITE = System.getenv("WEBSITE")

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

    override fun onReady(event: ReadyEvent) {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                event.jda.apply {
                    presence.setPresence(OnlineStatus.ONLINE, watching("${Bot.instance.WEBSITE} | ${guilds.size} servers"))
                }
            }
        }, 0, 1000*300)
    }

    override fun onGuildLeave(event: GuildLeaveEvent) {
        event.apply {
            Sokuji.removeAll(Sokuji.filter(guild.idLong))
        }
    }
}
