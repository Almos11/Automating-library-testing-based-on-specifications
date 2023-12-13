package process

import data.SimplyMyClass
import org.jetbrains.research.libsl.LibSL
import org.junit.jupiter.api.Test
import java.io.File

class TestGraphMyClass {
    @Test
    fun generalTest() {
        val path  = "./src/test/testdata/lsl/MyClass.lsl"
        val libSL = LibSL("")
        val library = libSL.loadFromFile(File(path))
        println(library.automata[0])
    }
}