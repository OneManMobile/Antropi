package usecase

import model.*
import kotlin.math.*

class GetNextSnakeWorldUseCase {

    companion object{
        const val SAMPLE_PATHS = 100
        const val STEPS = 200
        const val HUNGER = 100

        var lastEntropy = 0.0
    }

    /**
     * An Ant can move around freely on the grid, and it can choose to carry the color it stand on
     * And swap it with any adjecent color
     *
     * To see the chaos available to the Ant:
     * 1. Take a ChaosAnt
     * 2. Make a list of any possible move from position
     * 3. From those moves, determine the move that allows maximum access to future entropy
     * 4. Determine maximum access by: Moving ChaosAnt
     */
    fun execute(input: SnakeWorld): SnakeWorld{

        val possibleNextWorlds = getListOfNextPossibleSnakeWorlds(input)

        // given step t+1, what is the access to entropy at Tau
        val bestWorld = possibleNextWorlds.maxBy { possibleNextWorld ->

            val accessibleEntropy = GetAccessibleEntropyFromWorld(possibleNextWorld)

            if(accessibleEntropy > lastEntropy){
                println("BETTER ENTROPY: " + accessibleEntropy)
                lastEntropy = accessibleEntropy
            }

            accessibleEntropy
        }

        if(bestWorld.snake.body.first().x > bestWorld.worldSize || bestWorld.snake.body.first().y > bestWorld.worldSize)
            throw Exception("")

        println("Best world: " + bestWorld.snake.body.first().x + " | "+ bestWorld.snake.body.first().y + " food: " + bestWorld.food.x + ", " + bestWorld.food.y)


        return bestWorld

    }

    private fun GetAccessibleEntropyFromWorld(possibleNextWorld: SnakeWorld): Double {
        val paths = mutableListOf<List<SnakeWorld>>()

        for (i in 0..SAMPLE_PATHS) {

            val newPath = mutableListOf<SnakeWorld>()
            var nextWorld = getListOfNextPossibleSnakeWorlds(possibleNextWorld, 1).get(0)

            for (j in 0..STEPS) {
                newPath.add(nextWorld)
                val nextWorldCandidate = getListOfNextPossibleSnakeWorlds(nextWorld, 1).getOrNull(0)
                if(nextWorldCandidate == null)
                    break
                nextWorld = nextWorldCandidate
            }

            paths.add(newPath)
        }

        val occurenceMap = mutableMapOf<SnakeWorld, Int>()
        paths.forEach {
            it.forEach {
                val currentCount = occurenceMap.get(it) ?: 0
                occurenceMap.put(it, currentCount + 1)
            }
        }

        val allWorldOccurences = occurenceMap.values.reduce { acc, i -> acc + i }

        val entropyAvailable = -1 * occurenceMap.keys.fold(0.0) { acc, world ->

            val probability = occurenceMap.get(world)!!.toDouble() / allWorldOccurences.toDouble()

            acc + (probability * ln(probability))
        }

        // println("ENTROPY: " + entropyAvailable + " unique worlds: " + occurenceMap.size + " allWorldOccurences: " + allWorldOccurences)

        return entropyAvailable
    }

    fun getListOfNextPossibleSnakeWorlds(currentSnakeWorld: SnakeWorld, sampleSize: Int = Int.MAX_VALUE): List<SnakeWorld> {
        val possibleWorlds = mutableListOf<SnakeWorld>()

        val currentSnake = currentSnakeWorld.snake.body

        val directions = mutableListOf<BodyPoint>(
            // top, left, right, down
            BodyPoint(-1, 0),
            BodyPoint(1, 0),
            BodyPoint(0, -1),
            BodyPoint(0, 1)
        )

        for (direction in directions) {
            val newHead = BodyPoint(currentSnake.first().x + direction.x, currentSnake.first().y + direction.y)

            if(newHead.y >= 0 && newHead.y < currentSnakeWorld.worldSize
                && newHead.x >= 0 && newHead.x < currentSnakeWorld.worldSize && !currentSnake.contains(newHead)){

            var food = currentSnakeWorld.food
            var newBody = currentSnake
            var hunger: Int

            // if the snake lands on food with its next move then the food becomes the head and new food is placed randomly
            if (newHead == food) {
                newBody = listOf(newHead) + currentSnake
                food = BodyPoint((0 until currentSnakeWorld.worldSize).random(), (0 until currentSnakeWorld.worldSize).random())
                hunger = HUNGER
            } else {
                hunger = currentSnakeWorld.snake.hunger -1
                newBody = listOf(newHead) + currentSnake.dropLast(1)
            }

            if(hunger < 0){
                newBody = newBody.dropLast(1)
                hunger = HUNGER
            }

            if(newBody.isNotEmpty())
            possibleWorlds.add(
                SnakeWorld(
                    snake = Snake(newBody, hunger),
                    food = food,
                    worldSize = currentSnakeWorld.worldSize
                )
            )
            }
        }

        possibleWorlds.forEach {
            if(it.snake.body.first().x > it.worldSize || it.snake.body.first().y > it.worldSize)
                throw Exception("")
        }

        return possibleWorlds.shuffled().take(sampleSize)
    }

    private fun randomFoodPosition(body: List<BodyPoint>, worldSize: Int): BodyPoint {

        while (true) {
            val x = (0 until worldSize).random()
            val y = (0 until worldSize).random()

            if (!body.contains(BodyPoint(x, y))) {
                return BodyPoint(x, y)
            }
        }
    }

}
