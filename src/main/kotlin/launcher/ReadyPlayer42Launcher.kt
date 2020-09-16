package launcher

import launcher.lobby.ClientLobby
import launcher.lobby.ServerLobby
import launcher.ui.FrameView
import tornadofx.App
import tornadofx.launch


class ReadyPlayer42Launcher: App(FrameView::class) {

    override fun stop() {
        ClientLobby.quit()
        ServerLobby.close()
        super.stop()
    }


    companion object {
        const val TITLE: String = "The best game you've ever seen"
    }
}


fun main() {
    launch<ReadyPlayer42Launcher>()
}