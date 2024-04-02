package snake.domain

import java.util.zip.*

class ComplexityService {

    var bytes = ByteArray(1024)

    companion object{
        val complexityMap = mutableMapOf<List<List<Int>>, Int>()
    }

    // higher means higher complexity
    fun getComplexity(grid: List<List<Int>>): Int{

        if(complexityMap.contains(grid))
            return complexityMap.get(grid)!!

        val complexity: Int
        val bytesOfGrid = grid.map { row -> row.joinToString {
            it.toString()
        } + "\n" }.joinToString {
            it
        }.toByteArray(Charsets.UTF_8)

          val compresser = Deflater()
          compresser.setInput(bytesOfGrid)
          compresser.finish()
          complexity = compresser.deflate(bytes)
          compresser.end()

        complexityMap[grid] = complexity

        return complexity
    }

}
