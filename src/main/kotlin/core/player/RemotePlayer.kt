package core.player

import core.character.Character
import game.net.GameServer


class RemotePlayer(name: String, val ipAddress: String) : Player(name, PlayerData.ManufacturedColors.random(), Character()) {

    val key: String
        get() = GameServer.buildKey(ipAddress)


    init {
        GameServer.newPlayer(this)
    }
}