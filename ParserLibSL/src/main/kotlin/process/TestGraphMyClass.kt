package process

import cycle.Cycle
import data.classes.InfoText
import data.classes.MyClass
import graph.Graph
import node.Node
import org.jeasy.random.EasyRandom
import org.jetbrains.research.libsl.nodes.Function
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.full.functions

class TestGraphMyClass {
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
            "InfoText" -> easyRandom.nextObject(InfoText::class.java)
            else -> throw RuntimeException("Не получилось сгенерировать случайное значение")
        }
    }

    private fun checkReturnType(targetType : String, value : Any) : Boolean {
        val type = value::class.toString().substring(6, 10)
        val typeInLine = if (type == "data") {
            value::class.toString().substring(11)
        } else {
            value::class.toString().substring(13)
        }
        return targetType == typeInLine
    }

    private fun testFunction(myClass: MyClass, function: Function) {
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

    private fun testUnavailableFunction(myClass: MyClass, function: Function) {
        val nameFunction = function.name
        val myClassKClass = myClass::class
        val myFunction = myClassKClass.functions.find { it.name == nameFunction }
        assert(myFunction != null)
        val listOfArgs: MutableList<Any> = mutableListOf()
        for (arg in function.args) {
            listOfArgs.add(getRandomValue(arg.typeReference.name))
        }
        println(myClass.state)
        println(myFunction)
        assertThrows<java.lang.reflect.InvocationTargetException> {
            myFunction?.call(myClass, *listOfArgs.toTypedArray())
        }

    }

    private fun myEasyTest(myClass: MyClass, index: Int, nodes: MutableList<Node>, used: Array<Boolean>) {
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
                myEasyTest(newMyClass, indexTo, nodes, used)
            }
        }
        for (function in node.unavailableFunctions) {
            testUnavailableFunction(myClass, function)
        }
    }

    private fun printArray(array: Array<Int>) {
        for (el in array) {
            print(el)
            println(" ")
        }
        println()
    }

    private fun myHardTest(myClass: MyClass, index: Int, nodes: MutableList<Node>, used: Array<Int>) {
        if (used[index] == 0) {
            return
        }
        used[index] -= 1
        val node = nodes[index]
        for (function in node.functions) {
            val indexTo = node.functionsAndIndex[function.name]
            val newMyClass = myClass.copy()
            testFunction(newMyClass, function)
            if (indexTo != null) {
                myHardTest(newMyClass, indexTo, nodes, used)
            }
        }

    }

    private fun getLargestCycle(countVertexes: Int, edges: MutableList<Pair<Int, Int>>): List<Int> {
        val graph = Cycle(countVertexes)
        for (edge in edges) {
            graph.addEdge(edge.first, edge.second)
        }
        return graph.findBestCycle()
    }

    private fun getGraph(): Graph {
        val path = "./src/test/testdata/lsl/MyClass.lsl"
        val graph = Graph(path)
        graph.process()
        return graph
    }

    private fun isSimplyCycle(path: List<Int>): Boolean {
        val distinctCombinedList = path.distinct()

        return path.size == distinctCombinedList.size + 1
    }

    private fun printAvailableFunctions(graph: Graph) {
        for (node in graph.nodes) {
            println(node.nameState)
            for (function in node.functions) {
                print(function.name + " ")
            }
            println()
        }
    }

    private fun printUnavailableFunctions(graph: Graph) {
        for (node in graph.nodes) {
            println(node.nameState)
            for (function in node.unavailableFunctions) {
                print(function.name + " ")
            }
            println()
        }
    }

    @Test
    fun easyTestProcess() {
        val graph = getGraph()
        //printUnavailableFunctions(graph)

        val nodes = graph.nodes
        val used = Array(nodes.size) {false}
        val myClass = MyClass("Created", "")
        val root = 0
        myEasyTest(myClass, root, nodes, used)


    }

    @Test
    fun hardTestProcess() {
        val numberOfTests = 100
        val graph = getGraph()
        val nodes = graph.nodes
        val edges = graph.edges
        val cycle = getLargestCycle(nodes.size, edges)
        val used = Array(nodes.size) {1}
        val myClass = MyClass("Created", "")
        val cycleSize = cycle.size
        if (isSimplyCycle(cycle)) {
            val numberOfTestOnOneVertex = (numberOfTests / cycleSize) + 1
            for (vertex in cycle) {
                used[vertex] = numberOfTestOnOneVertex
            }
        } else {
            val frequent = Array(nodes.size) {0}
            for (vertex in cycle) {
                frequent[vertex] += 1
            }
            val numberOfTestOnOneVertex = (numberOfTests / cycleSize) + 1
            for (vertex in cycle) {
                used[vertex] = frequent[vertex] * numberOfTestOnOneVertex
            }
        }
        val root = 0
        myHardTest(myClass, root, nodes, used)
    }

}