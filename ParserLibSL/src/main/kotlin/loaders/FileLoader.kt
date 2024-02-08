package loaders

import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.full.functions

class FileLoader {
    fun loadFromJar(jarFilePath: String, className: String): Any {
        val classLoader = URLClassLoader(arrayOf(File(jarFilePath).toURI().toURL()))
        val loadedClass = classLoader.loadClass(className)
        val instance = loadedClass.getDeclaredConstructor().newInstance()
//        val myClassKClass = instance::class
//        val myFunction = myClassKClass.functions.find { it.name == "next" }
//
//        // Проверка, найдена ли функция
//        assert(myFunction != null) { "Функция toStart не найдена в классе $className" }
//
//        // Вызываем найденную функцию
//        val result = myFunction?.call(instance)
//        //  val method = loadedClass.getDeclaredMethod("myMethod")
//        //  method.invoke(instance)
        return instance
    }

}