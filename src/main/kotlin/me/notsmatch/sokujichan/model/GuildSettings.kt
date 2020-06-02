package me.notsmatch.sokujichan.model

import org.bson.Document

data class GuildSettings(val guildId: Long) {

    fun toDocument() : Document {
        return Document().apply {
            put("guildId", guildId)
        }
    }

    companion object {

        fun fromDocument(document: Document) : GuildSettings {
            document.apply {
                return GuildSettings(getLong("guildId"))
            }
        }
    }
}