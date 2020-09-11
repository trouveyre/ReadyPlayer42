package ui

import com.jme3.app.SimpleApplication
import controler.LivingLocalPlayer
import model.Game
import model.character.MutableCharacter

import model.map.DemoMap


class ReadyPlayer42Application : SimpleApplication(
    InGameAppState(Game(DemoMap(), setOf(
        LivingLocalPlayer(MutableCharacter(0.0, 10.0))
    )))
) {

    override fun simpleInitApp() {}
}


fun main() {
    val app = ReadyPlayer42Application()
    app.start()
}