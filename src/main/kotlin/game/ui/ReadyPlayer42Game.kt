package game.ui

import com.jme3.app.SimpleApplication
import core.character.Character
import core.game.Game
import core.game.LocalGame
import core.map.Chunk
import core.map.ChunkCollection
import core.map.RandomMap
import core.player.LocalPlayer
import core.player.PlayerData
import core.rule.FirstScoreWinRule
import javafx.application.Platform
import launcher.ui.FrameView
import launcher.ui.MenuView
import launcher.ui.ResultsView
import tornadofx.find
import tornadofx.launch


class ReadyPlayer42Game(private val game: Game) : SimpleApplication(InGameAppState(game)) {

    override fun simpleInitApp() {}

    override fun destroy() {
        super.destroy()
        Platform.runLater {
            find<FrameView>().content = ResultsView(game.history, MenuView()).root
        }
        game.cleanUp()
    }


    companion object {
        const val LOCAL_PLAYERS_MAXIMUM_NUMBER: Int = 2
    }
}


fun main(vararg args: String) {

    ReadyPlayer42Game(LocalGame(
        RandomMap(setOf(Chunk.load(args[0]), ChunkCollection.OnePlatformChunk.chunk)),
        setOf(
            LocalPlayer(PlayerData.ManufacturedNames.random(), Character()),
            LocalPlayer(PlayerData.ManufacturedNames.random(), Character())
        ),
        FirstScoreWinRule(args[1].toInt())
    )).start()
}