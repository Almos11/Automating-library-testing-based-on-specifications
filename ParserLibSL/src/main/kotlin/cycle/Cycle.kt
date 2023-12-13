package cycle

import java.util.*

class Cycle(val vertices: Int) {
    private val adjacencyListStraight: MutableList<MutableList<Int>> = MutableList(vertices) { mutableListOf() }
    private val adjacencyListReverse: MutableList<MutableList<Int>> = MutableList(vertices) { mutableListOf() }
    private var distanceReverseEdges: IntArray = intArrayOf()
    private var distanceStraightEdges: IntArray = intArrayOf()

    fun addEdge(source: Int, destination: Int) {
        adjacencyListReverse[destination].add(source)
        adjacencyListStraight[source].add(destination)
    }

    private fun bfsOnReverseEdges(startVertex: Int) {
        val visited = BooleanArray(vertices)
        val queue: Queue<Int> = LinkedList()
        val distance = IntArray(vertices)

        // Инициализация расстояний
        Arrays.fill(distance, Int.MAX_VALUE)

        queue.add(startVertex)
        visited[startVertex] = true
        distance[startVertex] = 0

        while (queue.isNotEmpty()) {
            val currentVertex = queue.poll()

            for (neighbor in adjacencyListReverse[currentVertex]) {
                if (!visited[neighbor]) {
                    queue.add(neighbor)
                    visited[neighbor] = true
                    distance[neighbor] = distance[currentVertex] + 1
                }
            }
        }

        distanceReverseEdges = distance
    }

    private fun bfsOnStraightEdges(startVertex: Int) {
        val visited = BooleanArray(vertices)
        val queue: Queue<Int> = LinkedList()
        val distance = IntArray(vertices)

        // Инициализация расстояний
        Arrays.fill(distance, Int.MAX_VALUE)

        queue.add(startVertex)
        visited[startVertex] = true
        distance[startVertex] = 0

        while (queue.isNotEmpty()) {
            val currentVertex = queue.poll()

            for (neighbor in adjacencyListStraight[currentVertex]) {
                if (!visited[neighbor]) {
                    queue.add(neighbor)
                    visited[neighbor] = true
                    distance[neighbor] = distance[currentVertex] + 1
                }
            }
        }

        distanceStraightEdges = distance
    }

    private fun findBestVertex(): Int {
        var bestDistance = -1
        var bestIndex = -1
        for (index in distanceStraightEdges.indices) {
            val distance = distanceStraightEdges[index] + distanceReverseEdges[index]
            if (distance > bestDistance) {
                bestDistance = distance
                bestIndex = index
            }
        }
        return bestIndex
    }

    private fun findLongestPath(adjacencyList: MutableList<MutableList<Int>>, start: Int, end: Int): MutableList<Int> {
        val visited = BooleanArray(adjacencyList.size)
        val currentPath = mutableListOf<Int>()
        val longestPath = mutableListOf<Int>()

        fun dfs(node: Int) {
            visited[node] = true
            currentPath.add(node)

            if (node == end && currentPath.size > longestPath.size) {
                longestPath.clear()
                longestPath.addAll(currentPath)
            }

            for (neighbor in adjacencyList[node]) {
                if (!visited[neighbor]) {
                    dfs(neighbor)
                }
            }

            currentPath.removeAt(currentPath.size - 1)
            visited[node] = false
        }

        dfs(start)

        return longestPath
    }

    fun isSimplyCycle(path: MutableList<Int>): Boolean {
        val distinctCombinedList = path.distinct()

        return path.size == distinctCombinedList.size + 1
    }

    private fun findMaxCycle(start: Int): List<Int> {
        bfsOnReverseEdges(start)
        bfsOnStraightEdges(start)
        val end = findBestVertex()
        val path1 = findLongestPath(adjacencyListStraight, start, end)
        val path2 = findLongestPath(adjacencyListStraight, end, start)
        path1.removeAt(path1.size - 1)
        val path = path1 + path2
        return path
    }

    fun findBestCycle(): List<Int> {
        var bestCycle = listOf<Int>()
        for (vertex in 0..<vertices) {
            val cycle = findMaxCycle(vertex)
            if (cycle.size > bestCycle.size) {
                bestCycle = cycle
            }
        }
        return bestCycle
    }
}