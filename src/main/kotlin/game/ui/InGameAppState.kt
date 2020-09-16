package game.ui

import com.jme3.app.Application
import com.jme3.app.SimpleApplication
import com.jme3.app.state.BaseAppState
import com.jme3.font.BitmapText
import com.jme3.input.controls.InputListener
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Spatial
import com.jme3.scene.shape.Box
import core.character.CharacterData
import core.game.Game
import core.player.LocalPlayer
import core.player.PlayerData
import core.history.Chronicle
import core.history.GameHistory
import game.net.GameServer


class InGameAppState(val game: Game) : BaseAppState() {

    private lateinit var simpleApp: SimpleApplication

    private val colors: Map<PlayerData, ColorRGBA> = game.history.players.associateWith { ColorRGBA.randomColor() }
    private val characters: Map<PlayerData,Spatial> by lazy {
        game.history.players.associateWith {
            Geometry().apply {
                name = it.javaClass.simpleName
                mesh = Box(CharacterData.WIDTH.toFloat() / 2, CharacterData.HEIGHT.toFloat() / 2, CharacterData.WIDTH.toFloat() / 2)
                material = Material(application.assetManager, "Common/MatDefs/Misc/Unshaded.j3md").apply {
                    setColor("Color", colors[it])
                }
                localTranslation = Vector3f(it.character.x.toFloat(), it.character.y.toFloat(), Z)
            }
        }
    }
    private val scores: Map<PlayerData, BitmapText> by lazy {
        var x = SCORES_OFFSET_X
        game.history.players.associateWith {
            BitmapText(application.assetManager.loadFont("Interface/Fonts/Console.fnt")).apply {
                size = SCORES_SIZE
                color = colors[it]
                text = "${it.name}: ${game.scores[it]}"
                setLocalTranslation(x, lineHeight + SCORES_OFFSET_Y,0f)
                x += SCORES_SPACE
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
        GameServer.start()
        game.history.start()
    }

    override fun cleanup(app: Application) {
        val simpleApp = app as SimpleApplication
        clearChunk()
        simpleApp.guiNode.detachAllChildren()
        GameServer.stop()
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
//            simpleApp.rootNode.attachChild(simpleApp.assetManager.loadModel("Models/satelite.obj").apply {
            simpleApp.rootNode.attachChild(Geometry().apply {
                name = "floor(${it.x}, ${it.y})"
                mesh = Box(it.width.toFloat(), it.height.toFloat(), FLOOR_DEPTH)
                setMaterial(Material(simpleApp.assetManager, "Common/MatDefs/Misc/Unshaded.j3md").apply {
//                    setTexture("satelite_getho_01", simpleApp.assetManager.loadTexture("Textures/satelite_getho_01.png"))
                    setColor("Color", ColorRGBA.Brown)
                })
//                scale(0.1f, 0.1f, 0.1f)
//                scale(it.width.toFloat(), it.height.toFloat(), Z)
//                rotate(0f, 90f, 0f)
                localTranslation = Vector3f(it.x.toFloat(), it.y.toFloat(), Z)
            })
        }
    }

    private fun initPlayers() {
        game.chronicle.playersRemaining.forEach {
            simpleApp.rootNode.attachChild(characters[it])
            if (it is LocalPlayer)
                listeners.add(ShortcutsListener(it, simpleApp))
        }
    }

    private fun initScores() {
        game.history.players.forEach {
            simpleApp.guiNode.attachChild(scores[it])
        }
    }

    override fun update(tpf: Float) {
        when (val history = game.nextChronicle(tpf.toDouble())){
            is Chronicle -> {
                clearChunk()
                initCamera()
                initFloors()
                initPlayers()
                game.chronicle.start()
            }
            is GameHistory -> {
                application.stop()
            }
            null -> {
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
            }
        }
    }


    companion object {
        const val Z: Float = 0f
        const val FLOOR_DEPTH: Float = 5f
        const val SCORES_SIZE: Float = 50f
        const val SCORES_OFFSET_X: Float = 300f
        const val SCORES_OFFSET_Y: Float = 20f
        const val SCORES_SPACE: Float = 500f
    }
}