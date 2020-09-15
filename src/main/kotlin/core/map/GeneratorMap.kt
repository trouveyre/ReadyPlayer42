package core.map

class GeneratorMap(private val chunkLength: IntRange) : Map {

    override fun nextChunk(): Chunk {
        val length = chunkLength.random()
        val startY = 10..50
        return TODO()
    }
}