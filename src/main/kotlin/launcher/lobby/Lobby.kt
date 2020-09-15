package launcher.lobby

import core.player.PlayerData

interface Lobby {

    val players: Set<PlayerData>

    var observer: LobbyObserver?

    fun leave(): Set<PlayerData>
}