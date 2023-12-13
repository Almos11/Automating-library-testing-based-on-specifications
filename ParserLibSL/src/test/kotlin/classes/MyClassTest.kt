package classes

import data.MyClass
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows

class MyClassTest {

    @Test
    fun `toStart should set state to Created when state is Created or Printed`() {
        val myClass = MyClass("Created", "sample text")
        myClass.toStart()
    }
}
