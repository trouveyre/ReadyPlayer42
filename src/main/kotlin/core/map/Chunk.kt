package core.map

import java.io.File


data class Chunk(
    val length: Double,
    val platforms: Set<Platform>,
    val startPointX: Double,
    val startPointY: Double,
    val cameraStart: Double
) {

    companion object {

        const val FILE_SYMBOL_PLATFORM: Char = '_'
        const val FILE_SYMBOL_END: Char = '#'
        const val FILE_SYMBOL_START: Char = '*'
        const val FILE_SYMBOL_CAMERA: Char = 'C'
        const val UNIT_PER_CHARACTER: Double = 2.5

        fun load(file: String): Chunk {
            var length = 150.0
            val platforms = mutableSetOf<Platform>()
            var startPointX = 0.0
            var startPointY = 20.0
            var cameraStart = 20.0
            val reader = File(file).bufferedReader()
            var line = reader.readLine()
            var numberOfLines = 20
            var cursorX = 0
            var char: Char?
            var platformStart: Int? = null
            while (line != null) {
                println(line)
                numberOfLines--
                char = line.getOrNull(cursorX)
                while (char != null) {
                    when (char) {
                        FILE_SYMBOL_CAMERA -> cameraStart = cursorX * UNIT_PER_CHARACTER
                        FILE_SYMBOL_END -> length = cursorX * UNIT_PER_CHARACTER
                        FILE_SYMBOL_START -> {
                            startPointX = cursorX * UNIT_PER_CHARACTER
                            startPointY = numberOfLines * UNIT_PER_CHARACTER
                        }
                        FILE_SYMBOL_PLATFORM -> {
                            if (platformStart == null)
                                platformStart = cursorX
                        }
                        else -> {}
                    }
                    if (char != FILE_SYMBOL_PLATFORM) {
                        if (platformStart != null) {
                            platforms.add(StaticPlatform(
                                (platformStart + cursorX / 2.0) * UNIT_PER_CHARACTER,
                                numberOfLines * UNIT_PER_CHARACTER,
                                (cursorX - platformStart) * UNIT_PER_CHARACTER
                            ))
                            platformStart = null
                        }
                    }
                    cursorX++
                    char = line.getOrNull(cursorX)
                }
                if (platformStart != null) {
                    platforms.add(StaticPlatform(
                        (platformStart + cursorX / 2.0) * UNIT_PER_CHARACTER,
                        numberOfLines * UNIT_PER_CHARACTER,
                        (cursorX - platformStart) * UNIT_PER_CHARACTER
                    ))
                    platformStart = null
                }
                cursorX = 0
                line = reader.readLine()
            }
            return Chunk(length, platforms, startPointX, startPointY, cameraStart)
        }
    }
}