package game.net

import core.character.Move
import core.character.Orientation
import core.player.RemotePlayer
import java.net.DatagramPacket
import java.net.DatagramSocket
import kotlin.concurrent.thread


object GameServer {

    const val PORT: Int = 42424
    const val BUFFER_SIZE: Int = 1024
    const val ORDERS_SEPARATOR: Char = ':'

    private var _socket: DatagramSocket? = null
    val socket: DatagramSocket
        get() {
            if (_socket?.isClosed != false)
                _socket = DatagramSocket(PORT)
            return _socket ?: DatagramSocket(PORT)
        }

    private val mapping: MutableMap<String, RemotePlayer> by lazy { mutableMapOf() }
    val players: Set<RemotePlayer>
        get() = mapping.values.toSet()

    private var _isRunning: Boolean = false
    val isRunning: Boolean
        get() = _isRunning


    fun buildKey(ipAddress: String) = "$ipAddress:$PORT"

    fun decode(message: String): Pair<Move, Orientation> {
        val (move, orientation) = message.split(ORDERS_SEPARATOR)
        return Move.valueOf(move) to Orientation.valueOf(orientation)
    }

    fun encode(move: Move, orientation: Orientation): String {
        return "${move.name}$ORDERS_SEPARATOR${orientation.name}"
    }

    fun newPlayer(player: RemotePlayer) {
        if (!isRunning)
            mapping[player.key] = player
    }

    fun start() {
        if (mapping.isNotEmpty()) {
            thread(true) {
                _socket?.use {
                    _isRunning = true
                    val buffer = ByteArray(BUFFER_SIZE)
                    lateinit var packet: DatagramPacket
                    lateinit var orders: Pair<Move, Orientation>
                    while (mapping.isNotEmpty()) {
                        packet = DatagramPacket(buffer, buffer.size)
                        it.receive(packet)
                        orders = decode(String(packet.data, packet.offset, packet.length))
                        mapping[buildKey(packet.address.hostAddress)]?.order(orders.first, orders.second)
                    }
                }
                _isRunning = false
            }
        }
    }

    fun stop() {
        mapping.clear()
    }
}