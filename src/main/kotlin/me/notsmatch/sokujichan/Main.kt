package me.notsmatch.sokujichan

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        Bot(System.getenv("SOKUJICHAN_TOKEN")).start()
    }
}