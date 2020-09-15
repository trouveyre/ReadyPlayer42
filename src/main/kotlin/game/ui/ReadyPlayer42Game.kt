package game.ui

import com.jme3.app.SimpleApplication
import core.game.Game


class ReadyPlayer42Game(game: Game) : SimpleApplication(InGameAppState(game)) {

    override fun simpleInitApp() {}


    companion object {
        const val LOCAL_PLAYERS_MAXIMUM_NUMBER: Int = 2
    }
}