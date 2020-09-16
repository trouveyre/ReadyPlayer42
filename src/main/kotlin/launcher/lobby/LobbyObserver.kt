package launcher.lobby

import core.player.PlayerData

interface LobbyObserver {

    fun onPlayerChange(players: Set<PlayerData>)

    fun onLaunch(players: Set<PlayerData>)
}