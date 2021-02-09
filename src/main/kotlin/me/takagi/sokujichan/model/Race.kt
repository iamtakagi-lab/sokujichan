package me.takagi.sokujichan.model


/**
 * レース毎の順位データ
 * @param spots 順位
 */
data class Race(val spotsA: Spots, val spotsB: Spots)