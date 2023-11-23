import org.jetbrains.research.libsl.LibSL
import java.io.File

fun main(args: Array<String>) {
    val libSL = LibSL("./src/test/testdata/lsl/")
    val library = libSL.loadFromFile(File("./src/test/testdata/lsl/test.lsl"))
    val nameLibrary = library.metadata.name
    val lineLibrary = library.dumpToString()
    val automatons = library.automataReferences;
    assert(automatons.size == 1)
    val my_automaton = automatons[0]
    println(my_automaton.name)
}