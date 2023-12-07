import org.jetbrains.research.libsl.LibSL
import org.jetbrains.research.libsl.nodes.*
import org.jetbrains.research.libsl.nodes.Function
import org.jetbrains.research.libsl.nodes.references.FunctionReference
import java.io.File
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashMap

fun getInitState(states: MutableList<State>) : Int {
    for (index in states.indices) {
        if (states[index].kind == StateKind.INIT) {
            return index;
        }
    }
    return -1;
}

fun fillUsed(states: MutableList<State>) : HashMap<String, Boolean> {
    val used = HashMap<String, Boolean>();
    for (state in states) {
        used[state.name] = false;
    }
    return used;
}

data class Node(
    val nameState: String,
    val isInitState: Boolean,
    val to: MutableList<Pair<State, MutableList<FunctionReference>>> = mutableListOf(),
    val functions: MutableList<Function> = mutableListOf(),
    val functionsAndStates: HashMap<String, String> = hashMapOf(),
    val functionsAndIndex: HashMap<String, Int> = hashMapOf()
)

fun dfs(used: HashMap<String, Boolean>, shifts: MutableList<Shift>, nodes: MutableList<Node>, queueNodes: Queue<State>,
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
    dfs(used, shifts, nodes, queueNodes, functionMap, statesAndNodes);

}

fun getFunctionMap(automata : Automaton) : HashMap<String, Function> {
    val myMap = HashMap<String, Function>()
    for (function in automata.functions) {
        myMap[function.name] = function
    }
    return myMap
}

fun getGraph(path: String) : MutableList<Node> {
    val libSL = LibSL("")
    val library = libSL.loadFromFile(File(path))
    val automata = library.automata[0]
    val states = automata.states
    val shifts = automata.shifts
    val functionMap = getFunctionMap(automata)
    val used = fillUsed(states);
    val initStateIndex = getInitState(states);
    if (initStateIndex == -1) {
        throw RuntimeException("do not find init state");
    }
    val nodes: MutableList<Node> = mutableListOf()
    val queueNodes: Queue<State> = LinkedList()
    queueNodes.offer(states[initStateIndex])
    val statesAndNodes = HashMap<String, Int>()
    dfs(used, shifts, nodes, queueNodes, functionMap, statesAndNodes)
    for (node in nodes) {
        for (function in node.functions) {
            val state = node.functionsAndStates[function.name]
            val index = statesAndNodes[state]
            if (index != null) {
                node.functionsAndIndex[function.name] = index
            }
        }
    }
    return nodes
}

fun printNodes(nodes: MutableList<Node>) {
    for (node in nodes) {
        println(node.functionsAndIndex);
    }
}

fun testPrint() {
    val path  = "./src/test/testdata/lsl/test.lsl";
    val libSL = LibSL("")
    val library = libSL.loadFromFile(File(path))
    val automata = library.automata[0]
    val shifts = automata.shifts
    for (shift in shifts) {
        println(shift.functions)
    }
    val myMap = HashMap<String, Function>()
    for (function in automata.functions) {
        myMap[function.name] = function
    }

}





fun main(args: Array<String>) {
    val path  = "./src/test/testdata/lsl/test.lsl";
    val nodes = getGraph(path);
    printNodes(nodes)
    // testPrint()
}