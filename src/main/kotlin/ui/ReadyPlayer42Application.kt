package ui

import com.jme3.app.SimpleApplication
import controler.LocalPlayer
import model.character.Character

import model.map.DemoMap


class ReadyPlayer42Application : SimpleApplication(
    InGameAppState(DemoMap(), LocalPlayer(Character(0f, 5f)))
) {

    override fun simpleInitApp() {

    }

    override fun simpleUpdate(tpf: Float) {

    }
}


fun main() {
    val app = ReadyPlayer42Application()
    app.start()
}