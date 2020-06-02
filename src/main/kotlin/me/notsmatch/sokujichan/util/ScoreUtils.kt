package me.notsmatch.sokujichan.util

import sun.reflect.generics.tree.Tree
import java.util.*
import javax.annotation.Nonnull
import javax.annotation.Nullable
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

object ScoreUtils {

    val SCORE_MAP = mapOf(
        1 to 15,
        2 to 12,
        3 to 10,
        4 to 9,
        5 to 8,
        6 to 7,
        7 to 6,
        8 to 5,
        9 to 4,
        10 to 3,
        11 to 2,
        12 to 1
    )

    /**
     * @return 順位を点数に変換して返します
     */
    fun getScore(data: Array<Int>): Int {
        var score = 0
        data.forEach {
            score += SCORE_MAP[it]!!
        }
        return score
    }
}