package ui

import com.jme3.app.SimpleApplication
import controler.Game
import controler.LivingLocalPlayer
import model.character.Character
import model.map.ChunkCollection
import model.map.RandomMap
import model.rule.FirstScoreWinRule


class ReadyPlayer42Application : SimpleApplication(
        InGameAppState(Game(RandomMap(ChunkCollection.values().map { it.chunk }), setOf(
                LivingLocalPlayer("p1", Character()),
                LivingLocalPlayer("pp2", Character())
        ),
                winRule = FirstScoreWinRule(20)
        ))
) {

    override fun simpleInitApp() {}
}


fun main() {
    val app = ReadyPlayer42Application()
    app.start()
}