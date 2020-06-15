package me.notsmatch.sokujichan

import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.mongodb.client.model.Filters
import me.notsmatch.sokujichan.command.*
import me.notsmatch.sokujichan.service.GuildSettingsService
import me.notsmatch.sokujichan.service.MongoService
import me.notsmatch.sokujichan.service.SokujiService
import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import java.util.*

class Bot (private val token: String) {

    companion object {
        @JvmStatic
        lateinit var instance: Bot

        @JvmStatic
        val mongoService: MongoService = MongoService()
    }

    lateinit var jda: JDA

    val sokujiService = SokujiService()
    val settingsService = GuildSettingsService(mongoService)
    val eventWaiter = EventWaiter()

    fun start() {
        instance = this
        jda = JDABuilder(AccountType.BOT).setToken(token).setStatus(OnlineStatus.ONLINE).build()

        val builder = CommandClientBuilder()

        builder.setOwnerId("695218967173922866")
        builder.setPrefix("_")

        builder.addCommands(
            StartCommand(sokujiService),
            StopCommand(sokujiService),
            RaceCommand(sokujiService),
            RevertScoreCommand(sokujiService),
            SetScoreCommand(sokujiService),
            PenaltyCommand(sokujiService),
            AboutCommand(Color.GREEN, "https://github.com/notsmatch/sokujichan", Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VIEW_CHANNEL),
            GuildlistCommand(eventWaiter)
        )

        builder.setHelpWord("sokujichan")

        val client = builder.build()
        jda.addEventListener(Listener())
        jda.addEventListener(client)
    }
}

class Listener : ListenerAdapter() {

    override fun onReady(event: ReadyEvent) {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                event.jda.apply {
                    presence.setPresence(OnlineStatus.ONLINE, Activity.watching("_scabout | ${guilds.size} servers"))
                }
            }
        }, 0, 1000*300)
    }

    override fun onGuildLeave(event: GuildLeaveEvent) {
        event.apply {
            Bot.mongoService.sokuji_collection.deleteMany(Filters.eq("guildId", guild.idLong))
            Bot.mongoService.guild_settings_collection.deleteMany(Filters.eq("guildId", guild.idLong))
        }
    }
}
