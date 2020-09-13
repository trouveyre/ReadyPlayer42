package ui

import com.jme3.app.Application
import com.jme3.app.SimpleApplication
import com.jme3.app.state.BaseAppState
import com.jme3.font.BitmapText
import com.jme3.input.KeyInput
import com.jme3.input.controls.InputListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Spatial
import com.jme3.scene.shape.Box
import controler.Game
import controler.LivingLocalPlayer
import controler.Player
import model.history.ChunkHistory
import model.history.GameHistory
import ui.ShortcutsListener.Companion.ACTION_NAME_CROUCH
import ui.ShortcutsListener.Companion.ACTION_NAME_JUMP
import ui.ShortcutsListener.Companion.ACTION_NAME_RUN_LEFT
import ui.ShortcutsListener.Companion.ACTION_NAME_RUN_RIGHT


class InGameAppState(val game: Game) : BaseAppState() {

    private lateinit var simpleApp: SimpleApplication

    private val colors: Map<Player, ColorRGBA> = game.history.players.associateWith { ColorRGBA.randomColor() }
    private val characters: Map<Player,Spatial> by lazy {
        game.history.players.associateWith {
            Geometry().apply {
                name = it.javaClass.simpleName
                mesh = Box(1f, 3f, 1f)
                material = Material(application.assetManager, "Common/MatDefs/Misc/Unshaded.j3md").apply {
                    setColor("Color", colors[it])
                }
                localTranslation = Vector3f(it.character.x.toFloat(), it.character.y.toFloat(), Z)
            }
        }
    }
    private val scores: Map<Player, BitmapText> by lazy {
        var x = 0f
        game.history.players.associateWith {
            x += 300f
            BitmapText(application.assetManager.loadFont("Interface/Fonts/Console.fnt")).apply {
                size = 60f
                color = colors[it]
                text = "${it.name}: ${game.scores[it]}"
                setLocalTranslation(x, lineHeight,0f)
            }
        }
    }
    private val listeners: MutableList<InputListener> = mutableListOf()

    override fun initialize(app: Application) {
        simpleApp = app as SimpleApplication
        initCamera()
        initFloors()
        initPlayers()
        initScores()
        game.history.start()
    }

    override fun cleanup(app: Application) {
        val simpleApp = app as SimpleApplication
        clearChunk()
        simpleApp.guiNode.detachAllChildren()
    }

    override fun onEnable() {}

    override fun onDisable() {}

    private fun clearChunk() {
        simpleApp.inputManager.apply {
            clearMappings()
            listeners.forEach { removeListener(it) }
        }
        simpleApp.rootNode.detachAllChildren()
    }

    private fun initCamera() {
        simpleApp.camera.apply {
            val x = game.cameraX.toFloat()
            location = Vector3f(x, 40f, 70f)
            lookAt(Vector3f(x, 20f, 0f), Vector3f.UNIT_Y)
        }
    }

    private fun initFloors() {
        game.chronicle.chunk.platforms.forEach {
            simpleApp.rootNode.attachChild(Geometry().apply {
                name = "floor(${it.x}, ${it.y})"
                mesh = Box(it.width.toFloat(), it.height.toFloat(), FLOOR_DEPTH)
                material = Material(simpleApp.assetManager, "Common/MatDefs/Misc/Unshaded.j3md").apply {
                    setColor("Color", ColorRGBA.Brown)
                }
                localTranslation = Vector3f(it.x.toFloat(), it.y.toFloat(), Z)
            })
        }
    }

    private var firstLLP: Boolean? = null       //TODO TEMP
    private fun initPlayers() {
        game.chronicle.playersRemaining.forEach {
            simpleApp.rootNode.attachChild(characters[it])

            if (it is LivingLocalPlayer) {
                if (firstLLP == null) {
                    firstLLP = true
                    val shortcuts = ShortcutsListener(it)
                    listeners.add(shortcuts)
                    simpleApp.inputManager.apply {
                        addMapping(ACTION_NAME_RUN_RIGHT + "1", KeyTrigger(KeyInput.KEY_D))
                        addMapping(ACTION_NAME_RUN_LEFT + "1", KeyTrigger(KeyInput.KEY_Q))
                        addMapping(ACTION_NAME_JUMP + "1", KeyTrigger(KeyInput.KEY_Z))
                        addMapping(ACTION_NAME_CROUCH + "1", KeyTrigger(KeyInput.KEY_S))
                        addListener(
                                shortcuts,
                                ACTION_NAME_RUN_RIGHT + "1",
                                ACTION_NAME_RUN_LEFT + "1",
                                ACTION_NAME_JUMP + "1",
                                ACTION_NAME_CROUCH + "1"
                        )
                    }
                }
                else if (firstLLP == true) {
                    val shortcuts = ShortcutsListener(it)
                    listeners.add(shortcuts)
                    simpleApp.inputManager.apply {
                        addMapping(ACTION_NAME_RUN_RIGHT + "2", KeyTrigger(KeyInput.KEY_RIGHT))
                        addMapping(ACTION_NAME_RUN_LEFT + "2", KeyTrigger(KeyInput.KEY_LEFT))
                        addMapping(ACTION_NAME_JUMP + "2", KeyTrigger(KeyInput.KEY_UP))
                        addMapping(ACTION_NAME_CROUCH + "2", KeyTrigger(KeyInput.KEY_DOWN))
                        addListener(
                                shortcuts,
                                ACTION_NAME_RUN_RIGHT + "2",
                                ACTION_NAME_RUN_LEFT + "2",
                                ACTION_NAME_JUMP + "2",
                                ACTION_NAME_CROUCH + "2"
                        )
                    }
                }
            }
        }
        firstLLP = null
    }

    private fun initScores() {
        game.history.players.forEach {
            simpleApp.guiNode.attachChild(scores[it])
        }
    }

    override fun update(tpf: Float) {
        game.update(tpf.toDouble())
        game.playersRunning.forEach {
            characters[it]?.localTranslation = Vector3f(it.character.x.toFloat(), it.character.y.toFloat(), Z)
        }
        game.chronicle.playersDead.forEach {
            simpleApp.rootNode.detachChild(characters[it])
        }
        application.camera.apply {
            val x = game.cameraX.toFloat()
            location.x = x
            lookAt(Vector3f(x, 20f, 0f), Vector3f.UNIT_Y)
        }
        game.chronicle.playersRemaining.forEach {
            scores[it]?.text = "${it.name}: ${game.scores[it]}"
        }
        when (val history = game.nextChronicle()){
            is ChunkHistory -> {
                clearChunk()
                initCamera()
                initFloors()
                initPlayers()
                game.chronicle.start()
            }
            is GameHistory -> {
                println("${history.winner} has win !")  //TODO
            }
            null -> {}
        }
    }


    companion object {
        const val Z: Float = 0f
        const val FLOOR_DEPTH: Float = 5f
    }
}