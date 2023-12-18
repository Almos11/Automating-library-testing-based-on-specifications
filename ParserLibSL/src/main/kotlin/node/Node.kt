package node

import org.jetbrains.research.libsl.nodes.Function
import org.jetbrains.research.libsl.nodes.State
import org.jetbrains.research.libsl.nodes.references.FunctionReference

data class Node(
    val nameState: String,
    val isInitState: Boolean,
    val to: MutableList<Pair<State, MutableList<FunctionReference>>> = mutableListOf(),
    val functions: MutableList<Function> = mutableListOf(),
    val functionsAndStates: HashMap<String, String> = hashMapOf(),
    val functionsAndIndex: HashMap<String, Int> = hashMapOf(),
    var unavailableFunctions: MutableList<Function> = mutableListOf()
)