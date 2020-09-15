package launcher

import launcher.ui.FrameView
import tornadofx.App
import tornadofx.launch


class ReadyPlayer42Launcher: App(FrameView::class) {

    companion object {
        const val TITLE: String = "The best game you've ever seen"
    }
}


fun main() {
    launch<ReadyPlayer42Launcher>()
}