package game

import com.jme3.input.KeyInput
import com.jme3.system.AppSettings
import core.character.Character
import core.game.Game
import core.game.LocalGame
import core.map.Chunk
import core.map.ChunkCollection
import core.map.RandomMap
import core.player.LocalPlayer
import core.player.PlayerData
import game.ui.ReadyPlayer42Game
import java.awt.Toolkit


fun playFullscreen(game: Game) {
    val app = ReadyPlayer42Game(game).apply {
        isShowSettings = true
        setSettings(AppSettings(true).apply {
            isFullscreen = true
            val size = Toolkit.getDefaultToolkit().screenSize
            width = size.width
            height = size.height
            frequency = 75
        })
    }
    app.start()
}


fun main(vararg args: String) {

    val map = if (args.isEmpty())
        RandomMap(ChunkCollection.values().map { it.chunk })
    else
        RandomMap(setOf(Chunk.load(args[0]), ChunkCollection.OnePlatformChunk.chunk))
    ReadyPlayer42Game(LocalGame(
            map,
            setOf(
                    LocalPlayer(PlayerData.ManufacturedNames.random(), Character()),
                    LocalPlayer(PlayerData.ManufacturedNames.random(), Character(),
                            KeyInput.KEY_RIGHT, KeyInput.KEY_LEFT, KeyInput.KEY_UP, KeyInput.KEY_DOWN)
            )
    )).start()
}