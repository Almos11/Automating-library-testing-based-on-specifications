package easy_random

import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.Test

data class InfoText(val countWords: Int, val countLetters: Int, val mostFrequentWord: String, val mostFrequentChar: Char) {}

class TestEasyRandom {
    @Test
    fun myTest() {
        val easyRandom = EasyRandom()
        val infoText = easyRandom.nextObject(InfoText::class.java)
        println(infoText)
        val array = easyRandom.nextObject(Array<Int>::class.java)
    }
}