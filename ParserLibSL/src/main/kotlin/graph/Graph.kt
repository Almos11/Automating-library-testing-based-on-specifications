package graph

import org.jetbrains.research.libsl.LibSL
import org.jetbrains.research.libsl.nodes.*
import org.jetbrains.research.libsl.nodes.Function
import java.io.File
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashMap
import node.Node

class Graph(val path: String) {
    var edges: MutableList<Pair<Int, Int>> = mutableListOf()
    var nodes: MutableList<Node> = mutableListOf()
    private var allFunctions: MutableList<Function> = mutableListOf()

    fun process() {
        getNodes()
        getEdges()
    }

    private fun getInitState(states: MutableList<State>) : Int {
        for (index in states.indices) {
            if (states[index].kind == StateKind.INIT) {
                return index;
            }
        }
        return -1;
    }

    private fun fillUsed(states: MutableList<State>) : HashMap<String, Boolean> {
        val used = HashMap<String, Boolean>();
        for (state in states) {
            used[state.name] = false;
        }
        return used;
    }

    private fun dfs(used: HashMap<String, Boolean>, shifts: MutableList<Shift>, queueNodes: Queue<State>,
                    functionMap: HashMap<String, Function>, statesAndNodes: HashMap<String, Int>) {
        if (queueNodes.isEmpty()) {
            return;
        }
        val curState = queueNodes.poll()
        val node = Node(curState.name, curState.kind == StateKind.INIT)
        used[curState.name] = true;
        for (indexes in shifts.indices) {
            val shift = shifts[indexes]
            if (shift.from.name == curState.name) {
                node.to.add(Pair(shift.to, shift.functions))
                for (function in shift.functions) {
                    functionMap[function.name]?.let { node.functions.add(it) }
                    if (shift.to.name == "self") {
                        node.functionsAndStates[function.name] = node.nameState
                    } else {
                        node.functionsAndStates[function.name] = shift.to.name
                    }
                }
                if (used[shift.to.name] == false) {
                    queueNodes.offer(shift.to)
                }
            }
        }
        statesAndNodes[curState.name] = nodes.size
        nodes.add(node);
        dfs(used, shifts, queueNodes, functionMap, statesAndNodes);

    }

    private fun getFunctionMap(automata : Automaton) : HashMap<String, Function> {
        val myMap = HashMap<String, Function>()
        for (function in automata.functions) {
            myMap[function.name] = function
        }
        return myMap
    }

    private fun getUnavailableFunctions() {
        for (node in nodes) {
            node.unavailableFunctions = (allFunctions - node.functions.toSet()).toMutableList()
        }
    }

    private fun getNodes() {
        val libSL = LibSL("")
        val library = libSL.loadFromFile(File(path))
        val automata = library.automata[0]
        allFunctions = automata.functions.toMutableList()
        val states = automata.states
        val shifts = automata.shifts
        val functionMap = getFunctionMap(automata)
        val used = fillUsed(states);
        val initStateIndex = getInitState(states);
        if (initStateIndex == -1) {
            throw RuntimeException("do not find init state");
        }
        val queueNodes: Queue<State> = LinkedList()
        queueNodes.offer(states[initStateIndex])
        val statesAndNodes = HashMap<String, Int>()
        dfs(used, shifts, queueNodes, functionMap, statesAndNodes)
        for (node in nodes) {
            for (function in node.functions) {
                val state = node.functionsAndStates[function.name]
                val index = statesAndNodes[state]
                if (index != null) {
                    node.functionsAndIndex[function.name] = index
                }
            }
        }
        getUnavailableFunctions()
    }

    private fun getEdges() {
        val edges: MutableList<Pair<Int, Int>> = mutableListOf()
        for ((index, node) in nodes.withIndex()) {
            for (function in node.functions) {
                val indexTo = node.functionsAndIndex[function.name]
                if (indexTo != null) {
                    edges.add(Pair(index, indexTo))
                }
            }
        }
        this.edges = edges
    }

    fun printNodes() {
        for (node in nodes) {
            println(node);
        }
    }

    fun printEdges() {
        for (edge in edges) {
            println(edge)
        }
    }
}