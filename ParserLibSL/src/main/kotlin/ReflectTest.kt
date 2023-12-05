import org.jetbrains.research.libsl.nodes.Function
import org.junit.jupiter.api.Test
import kotlin.reflect.full.*
import org.jetbrains.research.libsl.nodes.references.FunctionReference
import kotlin.random.Random
import org.junit.jupiter.api.assertDoesNotThrow

class ReflectTest {

    private fun testFunction(myClass: MyClass, functionReference: Function) {
        val nameFunction = functionReference.name
        val myClassKClass = myClass::class
        val myFunction = myClassKClass.functions.find { it.name == nameFunction }
        val countArgs = functionReference.args.size
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
        for (i in 0..<nodes.size) {
            testNode(nodes[i], i)
        }
    }

}