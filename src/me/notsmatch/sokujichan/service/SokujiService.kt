package me.notsmatch.sokujichan.service

import com.mongodb.client.model.Filters
import me.notsmatch.sokujichan.model.Sokuji
import java.util.*

class SokujiService {

    fun getSokuji(guildId: Long, channelId: Long) : Sokuji? {
        val document = BotService.mongoService.findSokuji(guildId, channelId) ?: return null
        return Sokuji.fromDocument(document)
    }

    fun getSokujiListByGuildId(guildId: Long) : List<Sokuji>?{
        val toReturn = arrayListOf<Sokuji>()
        val documents = BotService.mongoService.findSokuji(guildId) ?: return null
        documents.forEach { document -> toReturn.add(Sokuji.fromDocument(document))}
        return toReturn
    }

    fun addSokuji(guildId: Long, channelId: Long, teamA: String, teamB: String) : Sokuji? {
        if (getSokuji(guildId, channelId) != null) return null

        return Sokuji(guildId, channelId, mutableListOf(), teamA, teamB, 0, 0, 0, 0, 0, 12).apply {
            BotService.mongoService.replaceSokuji(guildId, channelId, toDocument())
        }
    }

    fun removeSokuji(guildId: Long, channelId: Long) : Boolean{
        val sokuji = getSokuji(guildId, channelId)
        if(sokuji === null)return false
        return BotService.mongoService.sokuji_collection.deleteOne(Filters.and(Filters.eq("guildId", guildId), Filters.eq("channelId", channelId))).wasAcknowledged()
    }
}