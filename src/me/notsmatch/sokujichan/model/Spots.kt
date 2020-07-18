package me.notsmatch.sokujichan.model

import me.notsmatch.sokujichan.util.ScoreUtils

/**
 * 順位管理、点数演算用クラス
 * @param data 順位データ
 */
data class Spots(val data: Array<Int>) {

    fun getScore() : Int {
        return ScoreUtils.getScore(data)
    }

    fun split() : String {
        val builder = StringBuilder()
        val it = data.iterator()
        while (it.hasNext()){
            val spot = it.next()
            builder.append(spot)

            if(it.hasNext()){
                builder.append(":")
            }
        }

        return builder.toString()
    }

    fun format() : String {
        val builder = StringBuilder()
        data.forEach { builder.append("${it}位 ") }
        return builder.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Spots

        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}