package blocks.model

/**
 * A graph where:
 *
 * 1. The nodes are each a set of blocks
 * 2. The edges define how its possible to travel from a Node to another. No duplication of edges
 * 3. Each node also has two calculatable features: The amount of accessible nodes from this node and the amount of leaves from this node. Any node that leads to a cyclical path back to the origin node, only counts as 1 accessible node for the origin node.
 */
class WorldGraph {
    private val nodes = mutableMapOf<Int, NodeMetadata>() // Keyed by the hash of the Set<Block>
    private val edges = mutableMapOf<Int, MutableSet<Int>>() // Keyed by the hash of the Set<Block>

    data class NodeMetadata(var accessibleNodes: Int = 0, var leafNodes: Int = 0)

    fun addNode(from: Set<Block>, to: Set<Block>): Boolean {
        val fromHash = from.hashCode()
        val toHash = to.hashCode()

        // Ensure both nodes are added to the nodes map
        nodes.getOrPut(fromHash) { NodeMetadata() }
        nodes.getOrPut(toHash) { NodeMetadata() }

        // Add the edge
        val fromEdges = edges.getOrPut(fromHash) { mutableSetOf() }
        if (fromEdges.contains(toHash)) {
            // Edge already exists, return false indicating no new addition was made
            return false
        }
        fromEdges.add(toHash)

        return true
    }

    fun recalculateScoresFor(possibleNodes: List<Int>) {
        possibleNodes.forEach {
            nodes.get(it)?.apply {
                accessibleNodes = 0
                leafNodes = 0
            }
        }

        possibleNodes.forEach { nodeHash ->
            val visited = mutableSetOf<Int>()
            val leaves = mutableSetOf<Int>()
            depthFirstSearch(nodeHash, visited, leaves, nodeHash)
            nodes[nodeHash]?.apply {
                accessibleNodes = visited.size - 1 // Exclude self
                leafNodes = leaves.size
            }
        }
    }

    private fun depthFirstSearch(currentHash: Int, visited: MutableSet<Int>, leaves: MutableSet<Int>, startHash: Int, cameFromHash: Int? = null) {
        if (visited.contains(currentHash)) return
        visited.add(currentHash)

        val nextNodes = edges[currentHash]?.minus(cameFromHash ?: -1) ?: emptySet()
        if (nextNodes.isEmpty()) leaves.add(currentHash)
        else {
            nextNodes.forEach { nextHash ->
                depthFirstSearch(nextHash, visited, leaves, startHash, currentHash)
            }
        }
    }

    fun printGraph() {
        nodes.forEach { (nodeHash, metadata) ->
            println("Node Hash: $nodeHash, Accessible Nodes: ${metadata.accessibleNodes}, Leaf Nodes: ${metadata.leafNodes}")
        }
        edges.forEach { (fromHash, toHashes) ->
            println("From Node Hash $fromHash to $toHashes")
        }
    }


    // trials = All accessibleNodes, successes = leaves
    fun wilsonScoreLowerBound(trials: Int, successes: Int, confidenceLevel: Double = 0.95): Double {
        if (trials == 0) return 0.0

        val phat = successes.toDouble() / trials
        val z = zScore(confidenceLevel)
        val denominator = 1 + z * z / trials
        val radical = phat * (1 - phat) / trials + z * z / (4 * trials * trials)

        return (phat + z * z / (2 * trials) - z * kotlin.math.sqrt(radical)) / denominator
    }

    fun zScore(confidenceLevel: Double): Double {
        // Assuming a 95% confidence level
        return 1.96 // For other confidence levels, this value should be adjusted accordingly
    }
}
