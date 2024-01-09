package process

import data.InfoText
import graph.Graph
import loaders.FileLoader
import node.Node
import org.jeasy.random.EasyRandom
import org.jetbrains.research.libsl.nodes.Function
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.random.Random
import kotlin.reflect.full.functions

class TestGraphSimplyMyClassLoader {
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

    private fun getGraph(pathToLslFile : String): Graph {
        val graph = Graph(pathToLslFile)
        graph.process()
        return graph
    }

    private fun testFunction(myClass: Any, function: Function) {
        val nameFunction = function.name
        val myClassKClass = myClass::class
        val myFunction = myClassKClass.functions.find { it.name == nameFunction }
        println(nameFunction)
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

    private fun randomWalks(myClass: Any, index: Int, nodes: MutableList<Node>, used: Array<Int>) {
        if (used[index] == 0) {
            return
        }
        used[index] -= 1
        val node = nodes[index]
        if (node.functions.size == 0) {
            return
        }
        val indexFunction = Random.nextInt(0, node.functions.size)
        val function = node.functions[indexFunction]
        val indexTo = node.functionsAndIndex[function.name]
        testFunction(myClass, function)
        if (indexTo != null) {
            randomWalks(myClass, indexTo, nodes, used)
        }

    }

    fun test(pathToLslFile : String = "./src/test/testdata/lsl/SimplyMyClass.lsl", pathToJarFile : String = "/home/vlad/Sasha/NIR/TestJar/SimplyMyClass.jar") {
        val graph = getGraph(pathToLslFile)
        val nodes = graph.nodes
        val edges = graph.edges
        val fileLoader = FileLoader()
        try {
            var countWays = 10
            while (countWays > 0) {
                val myClass = fileLoader.loadFromJar()
                val used = Array(nodes.size) { 1 }
                val start = 0
                randomWalks(myClass, start, nodes, used)
                countWays -= 1
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

    }

    @Test
    fun testLoader() {
        test()
    }

}