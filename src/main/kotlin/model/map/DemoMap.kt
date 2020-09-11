package model.map


class DemoMap : Map {

    private var _isChunk1 = false
    private val chunk1 = Chunk(
        1000f,
        setOf(
                Platform(0f, 0f, 20f),
                Platform(35f, 10f, 10f),
                Platform(10f, 20f, 5f),
                Platform(-25f, 25f, 15f),
                Platform(30f, 30f, 5f),
                Platform(0f, 40f, 25f)
        )
    )
    private val chunk2 = Chunk(
        500f,
        setOf(
                Platform(0f, 10f, 15f)
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