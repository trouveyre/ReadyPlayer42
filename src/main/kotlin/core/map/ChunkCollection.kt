package core.map

enum class ChunkCollection {

    OnePlatformChunk {
        override val chunk = Chunk(
                100.0,
                setOf(
                        StaticPlatform(60.0, 10.0, 60.0)
                ),
                0.0,
                20.0,
                50.0
        )
    },
    ShortAndThick {
        override val chunk = Chunk(
                150.0,
                setOf(
                        StaticPlatform(-25.0, 25.0, 15.0),
                        StaticPlatform(0.0, 0.0, 20.0),
                        StaticPlatform(10.0, 20.0, 5.0),
                        StaticPlatform(30.0, 30.0, 5.0),
                        StaticPlatform(35.0, 10.0, 10.0),
                        StaticPlatform(0.0, 40.0, 25.0),
                        StaticPlatform(55.0, 5.0, 5.0),
                        StaticPlatform(70.0, 0.0, 2.5),
                        StaticPlatform(80.0, 20.0, 25.0),
                        StaticPlatform(63.0, 35.0, 3.0),
                        StaticPlatform(90.0, 2.0, 2.5),
                        StaticPlatform(100.0, 40.0, 5.0),
                        StaticPlatform(120.0, 5.0, 10.0),
                        StaticPlatform(145.0, 25.0, 15.0)
                ),
                -20.0,
                30.0,
                0.0
        )
    };

    abstract val chunk: Chunk
}