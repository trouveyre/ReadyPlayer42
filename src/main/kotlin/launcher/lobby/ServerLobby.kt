package launcher.lobby

import core.player.LocalPlayer
import core.player.PlayerData
import core.player.RemotePlayer
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
                    socket.getOutputStream().apply {
                        _remotes.values.forEach {
                            val response = LobbyMessage.PlayerAdded.name +
                                    LobbyMessage.COMPONENTS_SEPARATOR +
                                    it.name +
                                    LobbyMessage.COMPONENTS_SEPARATOR +
                                    it.ipAddress
                            write(response.toByteArray())
                            flush()
                        }
                    }
                    _remotes[socket] = RemotePlayer(name, socket.inetAddress.hostAddress)
                    _remotes.keys.forEach {
                        it.getOutputStream().apply {
                            val response = LobbyMessage.PlayerAdded.name +
                                    LobbyMessage.COMPONENTS_SEPARATOR +
                                    _remotes[socket]?.name +
                                    LobbyMessage.COMPONENTS_SEPARATOR +
                                    _remotes[socket]?.ipAddress
                            write(response.toByteArray())
                            flush()
                        }
                    }
                    observer?.onPlayerChange(players)
                }
            }
            LobbyMessage.PlayerAdded -> {}
            LobbyMessage.LaunchGame -> {}
        }
    }

    fun open(local: LocalPlayer) {
        if (!isOpened) {
            _locals.add(local)
            observer?.onPlayerChange(players)
            socket = ServerSocket(PORT)
            thread(true) {
                socket?.use { server ->
                    while (isOpened) {
                        val client = server.accept()
                        if (client != null) {
                            thread(true) {
                                client.use {
                                    it.getOutputStream().apply {
                                        val message = "${LobbyMessage.RequireName.name}${LobbyMessage.COMPONENTS_SEPARATOR}${local.name}"
                                        write(message.toByteArray())
                                        flush()
                                    }
                                    val input = it.getInputStream()
                                    val buffer = ByteArray(LobbyMessage.SIZE_MAX)
                                    var bufferSize = input.read(buffer)
                                    while (isOpened) {
                                        treat(String(buffer, 0, bufferSize), it)
                                        bufferSize = input.read(buffer)
                                    }
                                }
                            }
                        }
                    }
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
        observer?.onPlayerChange(players)
        observer = null
        return result
    }

    fun launchGame() {
        if (isOpened) {
            _remotes.keys.forEach {
                it.getOutputStream().apply {
                    write(LobbyMessage.LaunchGame.name.toByteArray())
                    flush()
                }
            }
            close()
        }
    }
}