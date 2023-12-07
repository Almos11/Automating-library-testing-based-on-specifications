import org.jeasy.random.EasyRandom
import org.jetbrains.research.libsl.nodes.Function
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.reflect.full.functions

class ReflectTest {
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
        assertThrows<RuntimeException> {
            myFunction?.call(myClass, *listOfArgs.toTypedArray())
        }

    }

    private fun myTest(myClass: MyClass, index: Int, nodes: MutableList<Node>, used: Array<Boolean>) {
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

    @Test
    fun generalTest() {
        val path  = "./src/test/testdata/lsl/test.lsl";
        val nodes = getGraph(path)
        val used = Array(nodes.size) {false}
        val myClass = MyClass("test")
        myTest(myClass, 0, nodes, used)
    }

}