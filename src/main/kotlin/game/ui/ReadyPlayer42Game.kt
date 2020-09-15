package game.ui

import com.jme3.app.SimpleApplication
import core.character.Character
import core.game.Game
import core.game.LocalGame
import core.map.ChunkCollection
import core.map.RandomMap
import core.player.LocalPlayer
import core.player.PlayerData
import core.rule.FirstScoreWinRule
import tornadofx.launch


class ReadyPlayer42Game(game: Game) : SimpleApplication(InGameAppState(game)) {

    override fun simpleInitApp() {}


    companion object {
        const val LOCAL_PLAYERS_MAXIMUM_NUMBER: Int = 2
    }
}


fun main() {

    ReadyPlayer42Game(LocalGame(
        RandomMap(ChunkCollection.values().map { it.chunk }),
        setOf(
            LocalPlayer(PlayerData.ManufacturedNames.random(), Character()),
            LocalPlayer(PlayerData.ManufacturedNames.random(), Character())
        ),
        FirstScoreWinRule(150)
    )).start()
}