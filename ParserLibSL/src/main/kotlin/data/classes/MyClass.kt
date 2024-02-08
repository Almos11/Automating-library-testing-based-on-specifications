package data.classes

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.*
data class MyClass(var state: String, private var text: String) {
    constructor() : this("Created", "")
    fun toStart() {
        if (state == "Created" || state == "Printed") {
            state = "Started"
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'Created', 'Printed'")
        }
    }

    fun sendText(text: String) {
        if (state == "Started") {
            this.text = text
            state = "ReadyToProcessText"
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'Started'")
        }
    }

    fun toEnd() {
        if (state == "Printed") {
            state = "Closed"
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'Printed'")
        }
    }

    fun toCharacters() {
        if (state == "ReadyToProcessText") {
            state = "CharacterProcessing"
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'ReadyToProcessText'")
        }
    }

    fun toWords() {
        if (state == "ReadyToProcessText") {
            state = "WordsProcessing"
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'ReadyToProcessText'")
        }
    }

    fun toText() {
        if (state == "ReadyToProcessText") {
            state = "TextProcessing"
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'ReadyToProcessText'")
        }
    }

    fun stopProcess() {
        if (state == "CharacterProcessing" || state == "WordsProcessing" || state == "TextProcessing") {
            state = "EndProcessing"
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'CharactersProcessing', 'WordsProcessing', 'TextProcessing'")
        }
    }

    fun printToConsole() {
        if (state == "ReadyToProcessText" || state == "EndProcessing") {
           // println(text)
            state = "Printed"
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'ReadyToProcessText', 'EndProcessing'")
        }
    }

    fun printToFile(filePath: String) {
        if (state == "ReadyToProcessText" || state == "EndProcessing") {
            try {
                val file = File(filePath)
                val fileWriter = FileWriter(file)
                val bufferedWriter = BufferedWriter(fileWriter)
                bufferedWriter.write(text)
                bufferedWriter.close()
                fileWriter.close()
                state = "Printed"
            } catch (e: Exception) {
                println("Ошибка при записи в файл: ${e.message}, попробойти другой файл")
            }
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'ReadyToProcessText', 'EndProcessing'")
        }
    }

    fun deleteCharacters(ch: Char) {
        if (state == "CharacterProcessing") {
            text = text.replace(ch.toString(), "")
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'CharacterProcessing'")
        }
    }

    fun replaceCharacters(chOld: Char, chNew: Char) {
        if (state == "CharacterProcessing") {
            text = text.replace(chOld.toString(), chNew.toString())
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'CharacterProcessing'")
        }
    }

    fun deleteWords(word: String) {
        if (state == "WordsProcessing") {
            text = text.replace(word, "")
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'WordProcessing'")
        }
    }

    fun replaceWords(wordOld: String, wordNew: String) {
        if (state == "WordsProcessing") {
            text = text.replace(wordOld, wordNew)
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'WordProcessing'")
        }
    }

    fun reverseText(): String {
        if (state == "TextProcessing") {
            text = text.reversed()
            return text
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'TextProcessing'")
        }
    }

    private fun countLetters(): Int {
        if (state == "TextProcessing") {
            return text.count { it.isLetter() }
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'TextProcessing'")
        }
    }

    private fun countWords(): Int {
        if (state == "TextProcessing") {
            val words = text.split("\\s+".toRegex())
            val nonEmptyWords = words.filter { it.isNotEmpty() }
            return nonEmptyWords.size
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'TextProcessing'")
        }
    }

    private fun findMostFrequentWord(): Pair<String?, Int?> {
        if (state == "TextProcessing") {
            val words = text.split("\\s+".toRegex())
            val wordCountMap = mutableMapOf<String, Int>()
            for (word in words) {
                val normalizedWord = word.lowercase(Locale.getDefault())
                val count = wordCountMap.getOrDefault(normalizedWord, 0)
                wordCountMap[normalizedWord] = count + 1
            }
            val mostFrequentWord = wordCountMap.maxByOrNull { it.value }
            return Pair(mostFrequentWord?.key, mostFrequentWord?.value)
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'TextProcessing'")
        }
    }

    private fun findMostFrequentChar(): Pair<Char?, Int?> {
        val charCountMap = text.groupingBy { it }.eachCount()
        val mostFrequentEntry = charCountMap.maxByOrNull { it.value }
        return Pair(mostFrequentEntry?.key, mostFrequentEntry?.value)
    }

    fun getInfoText(infoText: InfoText): Any? {
        if (state == "TextProcessing") {
            val mostFrequentWord = findMostFrequentWord().first
            val mostFrequentChar = findMostFrequentChar().first
            val countLetters = countLetters()
            val countWords = countWords()
            return mostFrequentWord?.let {
                if (mostFrequentChar != null) {
                    infoText.countWords = countWords
                    infoText.countLetters = countLetters
                    infoText.mostFrequentWord = it
                    infoText.mostFrequentChar = mostFrequentChar
                    return infoText
                }
            }
        } else {
            throw RuntimeException("Вы не можете вызывать эту функцию не из состояния 'TextProcessing'")
        }
    }
}