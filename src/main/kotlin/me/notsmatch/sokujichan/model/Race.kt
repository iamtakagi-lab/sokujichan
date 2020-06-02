package me.notsmatch.sokujichan.model

import com.google.gson.JsonObject

/**
 * レース毎の順位データ
 * @param spots 順位
 */
data class Race(val spotsA: Spots, val spotsB: Spots) {

    fun toJsonObject() : JsonObject {
        return JsonObject().apply {
            addProperty("spotsA", spotsA.split())
            addProperty("spotsB", spotsB.split())
        }
    }

    companion object {

        fun fromJson(json: JsonObject) : Race {
            json.apply {
                val spotsA = mutableListOf<Int>()
                json.get("spotsA").asString.split(":").forEach { spotsA.add(it.toInt()) }
                val spotsB = mutableListOf<Int>()
                json.get("spotsB").asString.split(":").forEach { spotsB.add(it.toInt()) }
                return Race(Spots(spotsA.toTypedArray()), Spots(spotsB.toTypedArray()))
            }
        }
    }
}