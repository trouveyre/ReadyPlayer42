package model.map


class DemoMap : Map {

    private var _isChunk1 = false
    private val chunk1 = Chunk(
        1000.0,
        setOf(
                StaticPlatform(0.0, 0.0, 20.0),
                StaticPlatform(35.0, 10.0, 10.0),
                StaticPlatform(10.0, 20.0, 5.0),
                StaticPlatform(-25.0, 25.0, 15.0),
                StaticPlatform(30.0, 30.0, 5.0),
                StaticPlatform(0.0, 40.0, 25.0)
        )
    )
    private val chunk2 = Chunk(
        500.0,
        setOf(
                StaticPlatform(0.0, 10.0, 15.0)
        )
    )

    override fun nextChunk(): Chunk {
        return if (_isChunk1) {
            _isChunk1 = false
            chunk2
        }
        else {
            _isChunk1 = true
            chunk1
        }
    }
}