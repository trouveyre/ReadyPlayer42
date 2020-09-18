package launcher.ui

import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundImage
import javafx.scene.layout.BackgroundRepeat
import javafx.stage.StageStyle
import launcher.ReadyPlayer42Launcher
import tornadofx.*
import java.net.URI

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
//        val back = Image("D:\\Programs\\IntelliJ\\ReadyPlayer42\\src\\main\\resources\\Backgrounds\\launcher.png")
//        background = Background(BackgroundImage(back, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, ))
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
