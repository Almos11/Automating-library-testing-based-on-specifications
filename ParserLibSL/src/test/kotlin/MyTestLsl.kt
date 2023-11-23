import org.jetbrains.research.libsl.LibSL
import org.jetbrains.research.libsl.nodes.references.builders.VariableReferenceBuilder
import org.jetbrains.research.libsl.type.ArrayType
import org.jetbrains.research.libsl.utils.QualifiedAccessUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class MyTestLsl {
    @Test
    fun test() {
        val libSL = LibSL("./src/test/testdata/lsl/")
        val library = libSL.loadFromFile(File("./src/test/testdata/lsl/test.lsl"))
        val nameLibrary = library.metadata.name
        val lineLibrary = library.dumpToString()
        val automatons = library.automataReferences;
        assert(automatons.size == 1)
        val my_automaton = automatons[0]
        println(my_automaton.name)

        //println(lineLibrary)
    }
}