package core.player

import core.character.Character
import core.character.Move
import core.character.Orientation
import game.net.GameServer
import java.net.DatagramPacket
import java.net.InetAddress


class LocalPlayer(name: String, character: Character): Player(name, PlayerData.ManufacturedColors.random(), character) {

    override fun onOrder(move: Move, orientation: Orientation) {
        GameServer.players.forEach {
            val buffer = GameServer.encode(move, orientation).encodeToByteArray()
            val packet = DatagramPacket(buffer, buffer.size, InetAddress.getByName(it.ipAddress), GameServer.PORT)
            GameServer.socket.send(packet)
        }
    }
}