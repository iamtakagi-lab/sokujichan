
object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        /**
        val list = mutableListOf<Int>()
        val allSpots = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
        for(i in allSpots.indices){
            list.add(allSpots[i])
        }
        println(list.toTypedArray().contentToString())
        **/


        val spotsA = mutableListOf(1, 2, 3, 4, 5, 7)
        val spotsB = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

        spotsB.removeAll(spotsA)

        println(spotsB.toTypedArray().contentToString())
    }
}