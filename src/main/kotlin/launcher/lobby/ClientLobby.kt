package launcher.lobby

import core.player.LocalPlayer
import core.player.PlayerData
import core.player.RemotePlayer
import java.net.Socket


object ClientLobby : Lobby {

    private val _players: MutableSet<PlayerData> = mutableSetOf<PlayerData>()
    override val players: Set<PlayerData>
        get() = _players

    private var socket: Socket? = null
        set(value) {
            field?.close()
            field = value
        }

    val hasJoined: Boolean
        get() = socket?.isClosed == false

    override var observer: LobbyObserver? = null
        set(value) {
            field = value
            field?.onPlayerChange(ServerLobby.players)
        }


    fun join(ipAddress: String, local: LocalPlayer) {
        if (!hasJoined) {
            socket = Socket(ipAddress, ServerLobby.PORT)
            val input = socket?.getInputStream()
            if (input != null) {
                val buffer = ByteArray(LobbyMessage.SIZE_MAX)
                var bufferSize = input.read(buffer)
                while (bufferSize != -1) {
                    val components = String(buffer, 0, bufferSize).split(LobbyMessage.COMPONENTS_SEPARATOR)
                    when (LobbyMessage.valueOf(components.first())) {
                        LobbyMessage.RequireName -> {
                            socket?.getOutputStream()?.apply {
                                val response = "${LobbyMessage.RequireName.name}${LobbyMessage.COMPONENTS_SEPARATOR}${local.name}"
                                write(response.toByteArray())
                                flush()
                            }
                        }
                        LobbyMessage.PlayerAdded -> {
                            val name = components.getOrNull(1)
                            if (local.name == name) {
                                _players.add(local)
                                ServerLobby.observer?.onPlayerChange(ServerLobby.players)
                            }
                            else if (name != null) {
                                val address = components.getOrNull(2)
                                if (address != null) {
                                    _players.add(RemotePlayer(name, address))
                                    ServerLobby.observer?.onPlayerChange(ServerLobby.players)
                                }
                            }
                        }
                    }
                    bufferSize = input.read(buffer)
                }
            }
        }
    }

    fun quit(): Set<PlayerData> {
        val result = players
        if (hasJoined)
            socket?.close()
        _players.clear()
        return result
    }

    override fun leave(): Set<PlayerData> {
        return quit()
    }
}