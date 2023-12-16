libsl "1.0.0";
library MyClass;

typealias Char = char;
typealias Int = int32;
typealias String = java.lang.String;

type InfoText {
    var countWords: Int;
    var countLetters: Int;
    var mostFrequentWord: String;
    var mostFrequentChar: Char;
}

type Pair {
    var first: Char;
    var second: Int;
}

automaton MyAutomat: String {
    initstate Created;
    state Started;
    state ReadyToProcessText;
    state CharacterProcessing;
    state WordProcessing;
    state TextProcessing;
    state EndProcessing;
    state Printed;
    finishstate Closed;

    shift Created -> Started by toStart;

    shift Started -> ReadyToProcessText by sendText;

    shift ReadyToProcessText -> CharacterProcessing by toCharacters;
    shift ReadyToProcessText -> WordsProcessing by toWords;
    shift ReadyToProcessText -> TextProcessing by toText;
    shift ReadyToProcessText -> Printed by [printToFile, printToConsole];

    shift CharacterProcessing -> self by [deleteCharacters, replaceCharacters];
    shift CharacterProcessing -> EndProcessing by stopProcess;

   shift WordsProcessing -> self by [deleteWords, replaceWords];
   shift WordsProcessing -> EndProcessing by stopProcessing;

   shift TextProcessing -> self by [reverseText, countWords, countLetters, findMostFrequentWord, findMostFrequentChar, getInfoText];
   shift TextProcessing -> EndProcessing by stopProcess;

   shift EndProcessing -> Printed by [printToConsole, printToFile];

   shift Printed -> Started by toStart;
   shift Printed -> Closed  by toEnd;

/*
all functions:
1. toCharacters
2. toWords
3. toText
4 .printToFile
5. printToConsole
6. deleteCharacters
7. replaceCharacters
8. deleteWords
9. replaceWords
10. stopProcess
11. reverseText
12. countWords
13. countLetters
14. findMostFrequentWord
15. findMostFrequentChar
16. getInfoText
17. toEnd
*/

fun toStart() {}

fun sendText(text: String) {}

fun toWords() {}

fun toText() {}

fun printToFile(path: String) {}

fun printToConsole() {}

fun deleteCharacters(ch: Char) {}

fun replaceCharacters(chOld: Char, chNew: Char) {}

fun deleteWords(word: String) {}

fun replaceWords(wordOld: String, wordNew: String) {}

fun stopProcess() {}

fun reverseText() : String {}

// fun countWords() : Int {}

// fun countLetters() : Int {}

// fun findMostFrequentWord() : Pair {}

// fun findMostFrequentChar() : Pair {}

fun getInfoText(info: InfoText) : InfoText {}

fun toEnd() {}

}