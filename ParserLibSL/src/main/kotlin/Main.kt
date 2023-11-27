import org.jetbrains.research.libsl.LibSL
import org.jetbrains.research.libsl.nodes.Shift
import org.jetbrains.research.libsl.nodes.State
import org.jetbrains.research.libsl.nodes.StateKind
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
    val functions: MutableList<FunctionReference> = mutableListOf()
)

fun dfs(used: HashMap<String, Boolean>, shifts: MutableList<Shift>, nodes: MutableList<Node>, queueNodes: Queue<State>) {
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
                node.functions.add(function)
            }
            if (used[shift.to.name] == false) {
                queueNodes.offer(shift.to)
            }
        }
    }
    nodes.add(node);
    dfs(used, shifts, nodes, queueNodes);

}

fun getGraph(path: String) {
    val libSL = LibSL("")
    val library = libSL.loadFromFile(File(path))
    val automata = library.automata[0]
    // automata_size == 1
    val states = automata.states
    val shifts = automata.shifts

    val used = fillUsed(states);
    val initStateIndex = getInitState(states);
    if (initStateIndex == -1) {
        throw RuntimeException("do not find init state");
    }
    val nodes: MutableList<Node> = mutableListOf();
    val queueNodes: Queue<State> = LinkedList();
    queueNodes.offer(states[initStateIndex]);
    dfs(used, shifts, nodes, queueNodes);
    for (node in nodes) {
        println(node);
    }
}

fun main(args: Array<String>) {
    val path  = "./src/test/testdata/lsl/test.lsl";
    getGraph(path);
}