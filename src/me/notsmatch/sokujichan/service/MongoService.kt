package me.notsmatch.sokujichan.service

import com.mongodb.*
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import org.bson.Document
import org.bson.conversions.Bson
import java.util.*


class MongoService(dev: Boolean) {

    val client: MongoClient
    val database: MongoDatabase
    val sokuji_collection: MongoCollection<Document>
    val guild_settings_collection: MongoCollection<Document>

    init {

        if(dev) {
            this.client = MongoClient(ServerAddress("localhost", 27017))
            this.database = this.client.getDatabase("sokujichan")
        } else {
            this.client = MongoClient(System.getenv("MONGO_URI"))
            this.database = this.client.getDatabase("sokujichan")
        }

        this.sokuji_collection = this.database.getCollection("sokuji")
        this.guild_settings_collection = this.database.getCollection("settings")
    }

    fun findSokuji(guildId: Long, channelId: Long): Document? {
        return this.sokuji_collection.find(Filters.and(Filters.eq("guildId", guildId), Filters.eq("channelId", channelId))).first() ?: return null
    }

    fun findSokuji(guildId: Long): FindIterable<Document>? {
        return this.sokuji_collection.find(Filters.eq("guildId", guildId)) ?: return null
    }

    fun findGuildSettings(guildId: Long): Document? {
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