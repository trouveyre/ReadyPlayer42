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
    },
    TwoLongWays {
        override val chunk = Chunk(
                271 * Chunk.UNIT_PER_CHARACTER,
                setOf(
                        StaticPlatform(x=145.0, y=37.5, width=27.5, height=1.0),
                        StaticPlatform(x=442.5, y=37.5, width=15.0, height=1.0),
                        StaticPlatform(x=495.0, y=37.5, width=15.0, height=1.0),
                        StaticPlatform(x=800.0, y=37.5, width=10.0, height=1.0),
                        StaticPlatform(x=822.5, y=37.5, width=2.5, height=1.0),
                        StaticPlatform(x=843.75, y=37.5, width=7.5, height=1.0),
                        StaticPlatform(x=211.25, y=35.0, width=10.0, height=1.0),
                        StaticPlatform(x=393.75, y=35.0, width=7.5, height=1.0),
                        StaticPlatform(x=245.0, y=32.5, width=2.5, height=1.0),
                        StaticPlatform(x=293.75, y=30.0, width=10.0, height=1.0),
                        StaticPlatform(x=63.75, y=27.5, width=7.5, height=1.0),
                        StaticPlatform(x=108.75, y=27.5, width=22.5, height=1.0),
                        StaticPlatform(x=981.25, y=27.5, width=27.5, height=1.0),
                        StaticPlatform(x=10.0, y=25.0, width=20.0, height=1.0),
                        StaticPlatform(x=366.25, y=25.0, width=20.0, height=1.0),
                        StaticPlatform(x=778.75, y=25.0, width=5.0, height=1.0),
                        StaticPlatform(x=910.0, y=22.5, width=20.0, height=1.0),
                        StaticPlatform(x=562.5, y=20.0, width=15.0, height=1.0),
                        StaticPlatform(x=598.75, y=20.0, width=12.5, height=1.0),
                        StaticPlatform(x=635.0, y=20.0, width=17.5, height=1.0),
                        StaticPlatform(x=300.0, y=17.5, width=22.5, height=1.0),
                        StaticPlatform(x=240.0, y=15.0, width=7.5, height=1.0),
                        StaticPlatform(x=805.0, y=15.0, width=5.0, height=1.0),
                        StaticPlatform(x=168.75, y=12.5, width=15.0, height=1.0),
                        StaticPlatform(x=55.0, y=10.0, width=20.0, height=1.0),
                        StaticPlatform(x=115.0, y=10.0, width=20.0, height=1.0),
                        StaticPlatform(x=207.5, y=7.5, width=10.0, height=1.0),
                        StaticPlatform(x=760.0, y=5.0, width=35.0, height=1.0),
                        StaticPlatform(x=698.75, y=2.5, width=10.0, height=1.0),
                        StaticPlatform(x=303.75, y=0.0, width=7.5, height=1.0),
                        StaticPlatform(x=531.25, y=0.0, width=20.0, height=1.0),
                        StaticPlatform(x=220.0, y=-2.5, width=5.0, height=1.0),
                        StaticPlatform(x=242.5, y=-2.5, width=5.0, height=1.0),
                        StaticPlatform(x=346.25, y=-2.5, width=10.0, height=1.0),
                        StaticPlatform(x=421.25, y=-2.5, width=10.0, height=1.0),
                        StaticPlatform(x=443.75, y=-2.5, width=10.0, height=1.0),
                        StaticPlatform(x=663.75, y=-2.5, width=7.5, height=1.0),
                        StaticPlatform(x=196.25, y=-5.0, width=10.0, height=1.0),
                        StaticPlatform(x=286.25, y=-5.0, width=10.0, height=1.0),
                        StaticPlatform(x=387.5, y=-5.0, width=10.0, height=1.0),
                        StaticPlatform(x=486.25, y=-5.0, width=12.5, height=1.0),
                        StaticPlatform(x=598.75, y=-5.0, width=27.5, height=1.0)
                ),
                2.0,
                11 * Chunk.UNIT_PER_CHARACTER,
                21.0
        )
    };

    abstract val chunk: Chunk
}