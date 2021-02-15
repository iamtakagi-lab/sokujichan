package me.takagi.sokujichan.model

import me.takagi.sokujichan.util.ScoreUtils

/**
 * 順位管理、点数演算クラス
 * @param data 順位データ
 */
data class Spots(val data: List<Int>) {

    fun getScore(): Int {
        return ScoreUtils.getScore(data)
    }

    fun split(): String {
        val builder = StringBuilder()
        val it = data.iterator()
        while (it.hasNext()) {
            val spot = it.next()
            builder.append(spot)

            if (it.hasNext()) {
                builder.append(":")
            }
        }

        return builder.toString()
    }

    fun format(): String {
        val builder = StringBuilder()
        data.forEach { builder.append("${it}位 ") }
        return builder.toString()
    }
}