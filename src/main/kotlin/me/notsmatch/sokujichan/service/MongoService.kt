package me.notsmatch.sokujichan.service

import com.mongodb.*
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import org.bson.conversions.Bson


class MongoService {

    val client: MongoClient
    val database: MongoDatabase
    val sokuji_collection: MongoCollection<Document>
    val guild_settings_collection: MongoCollection<Document>


    init {

        this.client = MongoClient(MongoClientURI(System.getenv("MONGO_URI")))
        this.database = this.client.getDatabase("sokujichan")


        this.sokuji_collection = this.database.getCollection("sokuji")
        this.guild_settings_collection = this.database.getCollection("settings")
    }

    fun findSokujiByGuildAndChannel(guildId: Long, channelId: Long): Document? {
        return this.sokuji_collection.find(Filters.and(Filters.eq("guildId", guildId), Filters.eq("channelId", channelId))).first() ?: return null
    }


    fun findSokujiByGuild(guildId: Long): FindIterable<Document>? {
        return this.sokuji_collection.find(Filters.eq("guildId", guildId)) ?: return null
    }


    fun findGuildSettingsById(guildId: Long): Document? {
        return this.guild_settings_collection.find(Filters.and(Filters.eq("guildId", guildId))).first() ?: return null
    }


    fun replaceSokuji(guildId: Long, channelId: Long, document: Document) {
        this.sokuji_collection.replaceOne(
            Filters.and(Filters.eq("guildId", guildId), Filters.eq("channelId", channelId)), document,
            ReplaceOptions().upsert(true)
        )
    }

    fun replaceGuildSettings(guildId: Long, document: Document) {
        this.guild_settings_collection.replaceOne(
            Filters.and(Filters.eq("guildId", guildId)), document,
            ReplaceOptions().upsert(true)
        )
    }
}