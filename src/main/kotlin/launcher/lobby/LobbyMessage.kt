package launcher.lobby

enum class LobbyMessage {

    RequireName,
    PlayerAdded,
    LaunchGame;


    companion object {
        const val COMPONENTS_SEPARATOR: Char = ':'
        const val SIZE_MAX: Int = 1024
    }
}