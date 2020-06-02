package me.notsmatch.sokujichan.util

object NumberUtils {

    fun isInteger(`val`: String): Boolean {
        try {
            Integer.parseInt(`val`)
            return true
        } catch (nfex: NumberFormatException) {
            return false
        }
    }

    fun isLong (`val`: String): Boolean {
        try {
            java.lang.Long.parseLong(`val`)
            return true
        } catch (nfex: NumberFormatException) {
            return false
        }

    }

    fun isDouble(`val`: String): Boolean {
        try {
            java.lang.Double.parseDouble(`val`)
            return true
        } catch (nfex: NumberFormatException) {
            return false
        }

    }
}
