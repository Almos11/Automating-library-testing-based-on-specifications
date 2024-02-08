package classes

import data.classes.MyClass
import org.junit.jupiter.api.Test

class MyClassTest {

    @Test
    fun `toStart should set state to Created when state is Created or Printed`() {
        val myClass = MyClass("Created", "sample text")
        myClass.toStart()
    }
}
