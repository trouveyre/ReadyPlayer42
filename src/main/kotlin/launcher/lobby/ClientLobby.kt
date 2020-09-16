package launcher.lobby

import core.player.LocalPlayer
import core.player.PlayerData
import core.player.RemotePlayer
import java.net.Socket
import kotlin.concurrent.thread


object ClientLobby : Lobby {

    private val _players: MutableSet<PlayerData> = mutableSetOf()
    override val players: Set<PlayerData>
        get() = _players

    private var socket: Socket? = null
        set(value) {
            field?.close()
            field = value
        }

    val isJoined: Boolean
        get() = socket?.isClosed == false

    override var observer: LobbyObserver? = null
        set(value) {
            field = value
            field?.onPlayerChange(players)
        }


    fun join(ipAddress: String, local: LocalPlayer) {
        if (!isJoined) {
            socket = Socket(ipAddress, ServerLobby.PORT)
            thread(true) {
                socket?.use {
                    val input = it.getInputStream()
                    if (input != null) {
                        val buffer = ByteArray(LobbyMessage.SIZE_MAX)
                        var bufferSize = input.read(buffer)
                        while (isJoined) {
                            val components = String(buffer, 0, bufferSize).split(LobbyMessage.COMPONENTS_SEPARATOR)
                            when (LobbyMessage.valueOf(components.first())) {
                                LobbyMessage.RequireName -> {
                                    val name = components.getOrNull(1)
                                    if (name != null) {
                                        _players.add(RemotePlayer(name, it.inetAddress.hostAddress))
                                        observer?.onPlayerChange(players)
                                    }
                                    it.getOutputStream().apply {
                                        val response = "${LobbyMessage.RequireName.name}${LobbyMessage.COMPONENTS_SEPARATOR}${local.name}"
                                        write(response.toByteArray())
                                        flush()
                                    }
                                }
                                LobbyMessage.PlayerAdded -> {
                                    val name = components.getOrNull(1)
                                    if (local.name == name) {
                                        _players.add(local)
                                        observer?.onPlayerChange(players)
                                    } else if (name != null) {
                                        val address = components.getOrNull(2)
                                        if (address != null) {
                                            _players.add(RemotePlayer(name, address))
                                            observer?.onPlayerChange(players)
                                        }
                                    }
                                }
                                LobbyMessage.LaunchGame -> {}   //TODO
                            }
                            bufferSize = input.read(buffer)
                        }
                    }
                }
            }
        }
    }

    fun quit(): Set<PlayerData> {
        val result = players
        if (isJoined)
            socket?.close()
        _players.clear()
        return result
    }

    override fun leave(): Set<PlayerData> {
        return quit()
    }
}