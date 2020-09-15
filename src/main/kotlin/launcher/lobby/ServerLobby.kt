package launcher.lobby

import core.player.LocalPlayer
import core.player.PlayerData
import core.player.RemotePlayer
import game.ui.ReadyPlayer42Game
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread


object ServerLobby : Lobby {

    const val PORT: Int = 42423

    private var socket: ServerSocket? = null
        set(value) {
            field?.close()
            field = value
        }

    private val _locals: MutableSet<LocalPlayer> = mutableSetOf()
    private val _remotes: MutableMap<Socket, RemotePlayer> = mutableMapOf()
    override val players: Set<PlayerData>
        get() = _locals + _remotes.values

    val isOpened: Boolean
        get() = socket?.isClosed == false

    override var observer: LobbyObserver? = null
        set(value) {
            field = value
            field?.onPlayerChange(players)
        }


    private fun treat(message: String, socket: Socket) {
        val components = message.split(LobbyMessage.COMPONENTS_SEPARATOR)
        when (LobbyMessage.valueOf(components.first())) {
            LobbyMessage.RequireName -> {
                val name = components.getOrNull(1)
                if (name != null) {
                    _remotes[socket] = RemotePlayer(name, socket.inetAddress.hostAddress)
                    observer?.onPlayerChange(players)
                    socket.getOutputStream().apply {
                        val response = """
                            ${LobbyMessage.PlayerAdded.name}
                            ${LobbyMessage.COMPONENTS_SEPARATOR}
                            ${_remotes[socket]?.name}
                            ${LobbyMessage.COMPONENTS_SEPARATOR}
                            ${_remotes[socket]?.ipAddress}
                            """.trimIndent()
                        write(response.toByteArray())
                        flush()
                    }
                }
            }
            LobbyMessage.PlayerAdded -> {}
        }
    }

    fun open(vararg locals: LocalPlayer) {
        if (!isOpened) {
            _locals.addAll(locals.take(ReadyPlayer42Game.LOCAL_PLAYERS_MAXIMUM_NUMBER))
            observer?.onPlayerChange(players)
            socket = ServerSocket(PORT)
            thread(true) {
                while (isOpened) {
                    try {
                        val client = socket?.accept()
                        if (client != null) {
                            thread(true) {
                                client.getOutputStream().apply {
                                    write(LobbyMessage.RequireName.name.toByteArray())
                                    flush()
                                }
                                val input = client.getInputStream()
                                val buffer = ByteArray(LobbyMessage.SIZE_MAX)
                                var bufferSize = input.read(buffer)
                                while (isOpened) {
                                    treat(String(buffer, 0, bufferSize), client)
                                    bufferSize = input.read(buffer)
                                }
                                client.close()
                            }
                        }
                    }
                    catch (exception: Exception) {}
                }
            }
        }
    }

    fun close(): Set<PlayerData> {
        val result = players
        if (isOpened)
            socket?.close()
        _remotes.clear()
        _locals.clear()
        return result
    }

    override fun leave(): Set<PlayerData> {
        return close()
    }
}