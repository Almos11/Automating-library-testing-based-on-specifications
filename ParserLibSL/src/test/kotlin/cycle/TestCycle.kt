package cycle

import org.junit.jupiter.api.Test

class TestCycle {

    @Test
    fun myTest() {
        val graph = Cycle(6)
        graph.addEdge(0, 1)
        graph.addEdge(0, 2)
        graph.addEdge(1, 3)
        graph.addEdge(2, 4)
        graph.addEdge(4, 5)
        graph.addEdge(5, 3)
        graph.addEdge(3, 0)
        println(graph.findBestCycle())
    }
}