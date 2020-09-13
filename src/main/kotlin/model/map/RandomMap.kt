package model.map


class RandomMap(private val chunks: Collection<Chunk>) : Map {

    private var currentChunk: Chunk = ChunkCollection.OnePlatformChunk.chunk

    override fun nextChunk(): Chunk {
        currentChunk = chunks.filter { it != currentChunk }.random()
        return currentChunk
    }
}