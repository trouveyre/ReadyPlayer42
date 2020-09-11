package ui

import com.jme3.app.Application
import com.jme3.app.SimpleApplication
import com.jme3.app.state.BaseAppState
import com.jme3.input.KeyInput
import com.jme3.input.controls.InputListener
import com.jme3.input.controls.KeyTrigger
import com.jme3.material.Material
import com.jme3.math.ColorRGBA
import com.jme3.math.Vector3f
import com.jme3.scene.Geometry
import com.jme3.scene.Spatial
import com.jme3.scene.shape.Box
import com.jme3.scene.shape.Sphere
import controler.LocalPlayer
import controler.Player
import model.map.Map
import ui.ShortcutsListener.Companion.ACTION_NAME_CROUCH
import ui.ShortcutsListener.Companion.ACTION_NAME_JUMP
import ui.ShortcutsListener.Companion.ACTION_NAME_RUN_LEFT
import ui.ShortcutsListener.Companion.ACTION_NAME_RUN_RIGHT

class InGameAppState(
    private val map: Map,
    vararg players: Player
) : BaseAppState() {

    private val players: MutableMap<Player, Spatial> = players.associate { it to Geometry() }.toMutableMap()
    private val listeners: MutableList<InputListener> = mutableListOf()

    override fun initialize(app: Application) {
        val simpleApp = app as SimpleApplication
        initCamera(simpleApp)
        initFloors(simpleApp)
        initPlayers(simpleApp)
    }

    override fun cleanup(app: Application) {
        app.inputManager.apply {
            clearMappings()
            listeners.forEach { removeListener(it) }
        }
        (app as SimpleApplication).rootNode.children.clear()
    }

    override fun onEnable() {

    }

    override fun onDisable() {

    }

    private fun initCamera(app: SimpleApplication) {
        app.camera.apply {
            location = Vector3f(5f, 40f, 70f)
            lookAt(Vector3f(5f, 20f, 0f), Vector3f.UNIT_Y)
        }
    }

    private fun initFloors(app: SimpleApplication) {
        val chunk = map.nextChunk()
        chunk.platforms.forEach {
            app.rootNode.attachChild(Geometry().apply {
                name = "floor(${it.x}, ${it.y})"
                mesh = Box(it.width, it.height, FLOOR_DEPTH)
                material = Material(app.assetManager, "Common/MatDefs/Misc/Unshaded.j3md").also {
                    it.setColor("Color", ColorRGBA.Brown)
                }
                localTranslation = Vector3f(it.x, it.y, Z)
            })
        }
    }

    private fun initPlayers(app: SimpleApplication) {
        players.keys.forEach { player ->
            players[player] = Geometry().apply {
                name = player.javaClass.simpleName
                mesh = Sphere(32, 32, 2f, true, false)
                material = Material(application.assetManager, "Common/MatDefs/Misc/Unshaded.j3md").also {
                    it.setColor("Color", ColorRGBA.Red)
                }
                localTranslation = Vector3f(player.characterX, player.characterY, Z)
                app.rootNode.attachChild(this)
            }

            if (player is LocalPlayer) {
                val shortcuts = ShortcutsListener(player)
                listeners.add(shortcuts)
                app.inputManager.apply {
                    addMapping(ACTION_NAME_RUN_RIGHT, KeyTrigger(KeyInput.KEY_D))
                    addMapping(ACTION_NAME_RUN_LEFT, KeyTrigger(KeyInput.KEY_Q))
                    addMapping(ACTION_NAME_JUMP, KeyTrigger(KeyInput.KEY_Z))
                    addMapping(ACTION_NAME_CROUCH, KeyTrigger(KeyInput.KEY_S))
                    addListener(
                        shortcuts,
                        ACTION_NAME_RUN_RIGHT,
                        ACTION_NAME_RUN_LEFT,
                        ACTION_NAME_JUMP,
                        ACTION_NAME_CROUCH
                    )
                }
            }
        }
    }

    override fun update(tpf: Float) {
        players.forEach {
            it.key.react(tpf, true)
            it.value.localTranslation = Vector3f(it.key.characterX, it.key.characterY, Z)
        }
    }


    companion object {
        const val Z: Float = 0f
        const val FLOOR_DEPTH: Float = 5f
    }
}