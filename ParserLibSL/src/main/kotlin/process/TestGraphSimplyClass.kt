import cycle.Cycle
import data.SimplyMyClass
import graph.Graph
import node.Node
import org.jeasy.random.EasyRandom
import org.jetbrains.research.libsl.nodes.Function
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.functions

class TestGraphSimplyClass {
    private fun getRandomValue(type : String) : Any {
        val easyRandom = EasyRandom()
        return when (type) {
            "Int" -> easyRandom.nextObject(Int::class.java)
            "Long" -> easyRandom.nextObject(Long::class.java)
            "Float" -> easyRandom.nextObject(Float::class.java)
            "Double" -> easyRandom.nextObject(Double::class.java)
            "Char" -> easyRandom.nextObject(Char::class.java)
            "Boolean" -> easyRandom.nextObject(Boolean::class.java)
            "String" -> easyRandom.nextObject(String::class.java)
            else -> throw RuntimeException("Не получилось сгенерировать случайное значение")
        }
    }

    private fun checkReturnType(targetType : String, value : Any) : Boolean {
        val typeInLine = value::class.toString().substring(13)
        return targetType == typeInLine
    }

    private fun testFunction(myClass: SimplyMyClass, function: Function) {
        val nameFunction = function.name
        val myClassKClass = myClass::class
        val myFunction = myClassKClass.functions.find { it.name == nameFunction }
        assert(myFunction != null)
        val listOfArgs: MutableList<Any> = mutableListOf()
        for (arg in function.args) {
            listOfArgs.add(getRandomValue(arg.typeReference.name))
        }
        assertDoesNotThrow ("Exception must not throw") {
            if (function.returnType != null) {
                val returnValue = myFunction?.call(myClass, *listOfArgs.toTypedArray())
                returnValue?.let { checkReturnType(function.returnType!!.name, it) }?.let { assert(it) }
            } else {
                myFunction?.call(myClass, *listOfArgs.toTypedArray())
            }
        }

    }

    private fun testUnavailableFunction(myClass: SimplyMyClass, function: Function) {
        val nameFunction = function.name
        val myClassKClass = myClass::class
        val myFunction = myClassKClass.functions.find { it.name == nameFunction }
        assert(myFunction != null)
        val listOfArgs: MutableList<Any> = mutableListOf()
        for (arg in function.args) {
            listOfArgs.add(getRandomValue(arg.typeReference.name))
        }
        assertThrows<RuntimeException> {
            myFunction?.call(myClass, *listOfArgs.toTypedArray())
        }

    }

    private fun myTest(myClass: SimplyMyClass, index: Int, nodes: MutableList<Node>, used: Array<Boolean>) {
        if (used[index]) {
            return
        }
        used[index] = true
        val node = nodes[index]
        for (function in node.functions) {
            val indexTo = node.functionsAndIndex[function.name]
            val newMyClass = myClass.copy()
            testFunction(newMyClass, function)
            if (indexTo != null) {
                myTest(newMyClass, indexTo, nodes, used)
            }
        }

    }

    private fun testCycle(countVertexes: Int, edges: MutableList<Pair<Int, Int>>) {
        val graph = Cycle(countVertexes)
        for (edge in edges) {
            graph.addEdge(edge.first, edge.second)
        }
        println(graph.findBestCycle())
    }

    @Test
    fun generalTest() {
        val path  = "./src/test/testdata/lsl/SimplyMyClass.lsl";
        val graph = Graph(path)
        graph.process()
        val nodes = graph.nodes
        val edges = graph.edges
        testCycle(nodes.size, edges)
        //val used = Array(nodes.size) {false}
        //val myClass = SimplyMyClass("test")
        //myTest(myClass, 0, nodes, used)
    }

}