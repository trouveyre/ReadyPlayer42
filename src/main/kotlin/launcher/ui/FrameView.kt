package launcher.ui

import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.stage.StageStyle
import launcher.ReadyPlayer42Launcher
import tornadofx.*

class FrameView : View(ReadyPlayer42Launcher.TITLE) {

    private val _tip: Label = Label(TIP_DEFAULT_VALUE)
    var tip: String
        get() = _tip.text
        set(value) {
            _tip.text = value
        }

    var content: Node
        get() = root.center
        set(value) {
            root.center = value
        }

    override val root = borderpane {
        top = hbox {
            alignment = Pos.CENTER
            add(_tip)
        }
        bottom = hbox {
            alignment = Pos.CENTER
            label("Imagined by the RAMenergy team")
        }
    }


    init {
        primaryStage.apply {
            initStyle(StageStyle.UNIFIED)
            width = WIDTH
            height = HEIGHT
        }
        root.center<MenuView>()
    }


    companion object {
        const val WIDTH: Double = 1100.0
        const val HEIGHT: Double = 700.0

        const val TIP_DEFAULT_VALUE: String = ""
    }
}
