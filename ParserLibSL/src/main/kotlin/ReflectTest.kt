import org.junit.jupiter.api.Test
import kotlin.reflect.full.*
import org.jetbrains.research.libsl.nodes.references.FunctionReference
import kotlin.random.Random
import org.junit.jupiter.api.assertDoesNotThrow

class ReflectTest {

    private fun testFunction(myClass: MyClass, functionReference: FunctionReference) {
        val nameFunction = functionReference.name
        val myClassKClass = myClass::class
        val myFunction = myClassKClass.functions.find { it.name == nameFunction }
        var countArgs = 0
        if (myFunction != null) {
            countArgs = myFunction.parameters.size - 1
        }
        assert(myFunction != null)
        val listOfArgs: MutableList<Any> = mutableListOf()
        for (i in 1..countArgs) {
            listOfArgs.add(Random.nextInt())
        }
        assertDoesNotThrow ("Exception must not throw") {
            myFunction?.call(myClass, *listOfArgs.toTypedArray())
        }

    }

    private fun testNode(node: Node, state: Int) {
        for (functionRef in node.functions) {
            val myClass = MyClass("Test")
            myClass.changeState(state)
            testFunction(myClass, functionRef)
        }
    }

    @Test
    fun generalTest() {
        val path  = "./src/test/testdata/lsl/test.lsl";
        val nodes = getGraph(path);
        val myClass = MyClass("Test")
        val node = nodes[0]
        val state = 1
        myClass.changeState(1)
        testNode(nodes[1], state)
        for (i in 0..<nodes.size) {
            testNode(nodes[i], i)
        }
    }

}