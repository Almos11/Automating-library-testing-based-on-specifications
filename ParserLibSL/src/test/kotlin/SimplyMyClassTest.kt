import data.SimplyMyClass
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class SimplyMyClassTest {

    @Test
    fun testInitialState() {
        val myClass = SimplyMyClass("Test")
        assertEquals(0, myClass.getState())
    }

    @Test
    fun testNextTransition() {
        val myClass = SimplyMyClass("Test")
        myClass.next()
        assertEquals(1, myClass.getState())
    }

    @Test
    fun testSumCalculation() {
        val myClass = SimplyMyClass("Test")
        myClass.next()
        val result = myClass.getSum(2, 3)
        assertEquals(5, result)
    }

    @Test
    fun testDiffCalculation() {
        val myClass = SimplyMyClass("Test")
        myClass.next()
        myClass.next()
        val result = myClass.getDiff(5, 2)
        assertEquals(3, result)
    }

    @Test
    fun testDivisionCalculation() {
        val myClass = SimplyMyClass("Test")
        myClass.next()
        myClass.next()
        myClass.next()
        val result = myClass.getDivision(6, 2)
        assertEquals(3f, result)
    }

    @Test
    fun testToStartTransition() {
        val myClass = SimplyMyClass("Test")
        myClass.next()
        myClass.toStart()
        assertEquals(0, myClass.getState())
    }

    @Test
    fun testEndTransition() {
        val myClass = SimplyMyClass("Test")
        myClass.next()
        myClass.next()
        myClass.next()
        myClass.end()
        assertEquals(4, myClass.getState())
    }

    @Test
    fun testBack() {
        val myClass = SimplyMyClass("Test")
        myClass.next()
        myClass.next()
        myClass.next()
        myClass.back()
        assertEquals(2, myClass.getState())
    }
}