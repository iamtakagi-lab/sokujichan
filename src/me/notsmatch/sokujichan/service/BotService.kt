package me.notsmatch.sokujichan.service

import com.jagrosh.jdautilities.command.CommandClientBuilder
import com.jagrosh.jdautilities.commons.waiter.EventWaiter
import com.mongodb.client.model.Filters
import me.notsmatch.sokujichan.command.*
import net.dv8tion.jda.api.*
import net.dv8tion.jda.api.entities.Activity.watching
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color
import java.util.*

class BotService (val token: String, val sokujiService: SokujiService, val dev: Boolean) {

    companion object {
        @JvmStatic
        lateinit var instance: BotService

        @JvmStatic
        lateinit var mongoService: MongoService
    }

    lateinit var jda: JDA
    lateinit var settingsService: GuildSettingsService
    val eventWaiter = EventWaiter()
    val WEBSITE = System.getenv("WEBSITE")

    fun start() : BotService {
        instance = this
        mongoService = MongoService(dev)
        settingsService = GuildSettingsService(mongoService)
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
            SetRaceSizeCommand(sokujiService),
            UrlCommand(sokujiService),
            AboutCommand(Color.GREEN, "https://github.com/riptakagi/sokujichan", Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VIEW_CHANNEL),
            GuildlistCommand(eventWaiter)
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
                    presence.setPresence(OnlineStatus.ONLINE, watching("${BotService.instance.WEBSITE} | ${guilds.size} servers"))
                }
            }
        }, 0, 1000*300)
    }

    override fun onGuildLeave(event: GuildLeaveEvent) {
        event.apply {
            BotService.mongoService.sokuji_collection.deleteMany(Filters.eq("guildId", guild.idLong))
            BotService.mongoService.guild_settings_collection.deleteMany(Filters.eq("guildId", guild.idLong))
        }
    }
}
