import com.soywiz.korge.*
import com.soywiz.korge.scene.*
import com.soywiz.korim.color.*
import snake.*

const val WIDTH = 512
const val HEIGHT = 512


suspend fun main() = Korge(width = WIDTH, height = HEIGHT, bgcolor = Colors["#FFFFFF"], title = "Antropi") {
	val sceneContainer = sceneContainer()

	sceneContainer.changeTo({ SnakeScene() })
}

