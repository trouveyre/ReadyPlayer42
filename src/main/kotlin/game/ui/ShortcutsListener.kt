package game.ui

import com.jme3.app.Application
import com.jme3.input.KeyInput
import com.jme3.input.KeyNames
import com.jme3.input.controls.ActionListener
import com.jme3.input.controls.KeyTrigger
import core.player.LocalPlayer
import core.character.Move
import core.character.Orientation


class ShortcutsListener(private val player: LocalPlayer, app: Application) : ActionListener {

    private val _serial: Int = serial


    init {
        app.inputManager.apply {
            addMapping(ACTION_NAME_RUN_RIGHT + _serial, KeyTrigger(player.runRightKey))
            addMapping(ACTION_NAME_RUN_LEFT + _serial, KeyTrigger(player.runLeftKey))
            addMapping(ACTION_NAME_JUMP + _serial, KeyTrigger(player.jumpKey))
            addMapping(ACTION_NAME_CROUCH + _serial, KeyTrigger(player.crouchKey))
            addListener(
                this@ShortcutsListener,
                ACTION_NAME_RUN_RIGHT + _serial,
                ACTION_NAME_RUN_LEFT + _serial,
                ACTION_NAME_JUMP + _serial,
                ACTION_NAME_CROUCH + _serial
            )
        }
    }


    override fun onAction(name: String, isPressed: Boolean, tpf: Float) {
        when (name.dropLast(_serial.toString().length)) {
            ACTION_NAME_RUN_RIGHT -> {
                if (isPressed)
                    player.order(Move.Run, false, Orientation.Right)
                else
                    player.order(Move.Run, true)
            }
            ACTION_NAME_RUN_LEFT -> {
                if (isPressed)
                    player.order(Move.Run, false, Orientation.Left)
                else
                    player.order(Move.Run, true)
            }
            ACTION_NAME_JUMP -> {
                if (isPressed)
                    player.order(Move.Jump)
                else
                    player.order(Move.Jump, true)
            }
            ACTION_NAME_CROUCH -> {
                if (isPressed)
                    player.order(Move.Crouch)
                else
                    player.order(Move.Crouch, true)
            }
            else -> {}
        }
    }


    companion object {
        const val ACTION_NAME_RUN_RIGHT = "right"
        const val ACTION_NAME_RUN_LEFT = "left"
        const val ACTION_NAME_JUMP = "jump"
        const val ACTION_NAME_CROUCH = "crouch"

        private var serial: Int = 0
            get() = field++
    }
}