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
import com.jme3.scene.plugins.fbx.ContentTextureLocator
import com.jme3.scene.plugins.fbx.FbxLoader
import com.jme3.scene.plugins.fbx.SceneLoader
import com.jme3.scene.plugins.fbx.SceneWithAnimationLoader
import com.jme3.scene.plugins.fbx.file.FbxFile
import com.jme3.scene.plugins.gltf.GlbLoader
import com.jme3.scene.plugins.gltf.GltfLoader
import com.jme3.scene.shape.Box
import com.jme3.texture.Texture2D
import com.jme3.texture.plugins.AWTLoader
import com.jme3.ui.Picture
import core.character.CharacterData
import core.game.Game
import core.history.Chronicle
import core.history.GameHistory
import core.player.LocalPlayer
import core.player.PlayerData
import game.net.GameServer
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.min
import kotlin.random.Random


class InGameAppState(val game: Game) : BaseAppState() {

    private lateinit var simpleApp: SimpleApplication

    private val colors: Map<PlayerData, ColorRGBA> = game.history.players.associateWith { ColorRGBA.randomColor() }
    private val characters: Map<PlayerData,Spatial> by lazy {
        simpleApp.assetManager.apply {
            registerLoader(GlbLoader::class.java, "glb")
            registerLoader(FbxLoader::class.java, "fbx")
        }
        game.history.players.associateWith {
//            simpleApp.assetManager.loadModel("Models/astronaute_from_maya.fbx").apply {
            Geometry().apply {
                name = it.javaClass.simpleName
                mesh = Box(CharacterData.WIDTH.toFloat() / 2, CharacterData.HEIGHT.toFloat() / 2, CharacterData.WIDTH.toFloat() / 2)
                setMaterial(Material(application.assetManager, "Common/MatDefs/Misc/Unshaded.j3md").apply {
                    setColor("Color", colors[it])
                })
//                scale(100f, 100f, 100f)
//                scale(0.01f, 0.01f, 0.01f)
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
        initBackground()
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

    private fun initBackground() {
        val background = Picture("background").apply {
            val file = File(ReadyPlayer42Game::class.java.protectionDomain.codeSource.location.toURI()).parentFile.resolve("background.png")
//            val file = File("D:\\Programs\\IntelliJ\\ReadyPlayer42\\src\\main\\resources\\Backgrounds\\background.png")
            setTexture(simpleApp.assetManager, Texture2D(AWTLoader().load(ImageIO.read(file), true)), false)
            setWidth(simpleApp.context.settings.width.toFloat())
            setHeight(simpleApp.context.settings.height.toFloat())
            setPosition(0f, 0f)
        }
        simpleApp.renderManager.createPreView("background", simpleApp.camera).apply {
            setClearFlags(true, true, true)
            attachScene(background)
        }
        simpleApp.viewPort.setClearFlags(false, true, true)
        background.updateGeometricState()
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
            simpleApp.rootNode.attachChild(
                if (it.width.toInt() in 5..12 && Random.nextBoolean())
                    simpleApp.assetManager.loadModel("Models/satelite.obj").apply {
                        name = "floor(${it.x}, ${it.y})"
                        setMaterial(Material(simpleApp.assetManager, "Common/MatDefs/Misc/Unshaded.j3md").apply {
                            setTexture("ColorMap", simpleApp.assetManager.loadTexture("Textures/satelite.png"))
                        })
                        scale(it.width.toFloat(), it.height.toFloat(), it.height.toFloat())
                        localTranslation = Vector3f(it.x.toFloat(), it.y.toFloat(), Z)
                    }
                else
                    simpleApp.assetManager.loadModel("Models/tronc.obj").apply {
                        name = "floor(${it.x}, ${it.y})"
                        setMaterial(Material(simpleApp.assetManager, "Common/MatDefs/Misc/Unshaded.j3md").apply {
                            setTexture("ColorMap", simpleApp.assetManager.loadTexture("Textures/tronc.jpg"))
                        })
                        scale(it.width.toFloat(), it.height.toFloat(), min(it.width.toFloat(), FLOOR_DEPTH))
                        if (Random.nextBoolean())
                            rotate(0f,  Math.PI.toFloat(), 0f)
                        localTranslation = Vector3f(it.x.toFloat(), it.y.toFloat(), Z)
                    }
            )
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
        when (game.nextChronicle(tpf.toDouble())){
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
        const val FLOOR_DEPTH: Float = 10f
        const val SCORES_SIZE: Float = 50f
        const val SCORES_OFFSET_X: Float = 300f
        const val SCORES_OFFSET_Y: Float = 20f
        const val SCORES_SPACE: Float = 500f
    }
}