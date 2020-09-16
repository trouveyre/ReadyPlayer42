package core.player

import com.jme3.input.KeyInput
import core.character.Character
import core.character.Move
import core.character.Orientation
import game.net.GameServer
import java.net.DatagramPacket
import java.net.InetAddress


class LocalPlayer(
    name: String,
    character: Character,
    val runRightKey: Int = KeyInput.KEY_D,
    val runLeftKey: Int = KeyInput.KEY_Q,
    val jumpKey: Int = KeyInput.KEY_Z,
    val crouchKey: Int = KeyInput.KEY_S,
): Player(name, PlayerData.ManufacturedColors.random(), character) {

    override fun onOrder(move: Move, stopDoing: Boolean, orientation: Orientation) {
        if (GameServer.isRunning)
            GameServer.players.forEach {
                val buffer = GameServer.encode(move, stopDoing, orientation).encodeToByteArray()
                val packet = DatagramPacket(buffer, buffer.size, InetAddress.getByName(it.ipAddress), GameServer.PORT)
                GameServer.socket.send(packet)
            }
    }
}