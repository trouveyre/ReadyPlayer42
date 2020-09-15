package game.ui

import com.jme3.input.controls.ActionListener
import core.player.LocalPlayer
import core.character.Move
import core.character.Orientation


class ShortcutsListener(private val player: LocalPlayer) : ActionListener {
    override fun onAction(name: String, isPressed: Boolean, tpf: Float) {
        when (name.dropLast(1)) {
            ACTION_NAME_RUN_RIGHT -> {
                if (isPressed)
                    player.order(Move.Run, Orientation.Right)
                else
                    player.order(Move.None)
            }
            ACTION_NAME_RUN_LEFT -> {
                if (isPressed)
                    player.order(Move.Run, Orientation.Left)
                else
                    player.order(Move.None)
            }
            ACTION_NAME_JUMP -> {
                if (isPressed)
                    player.order(Move.Jump)
            }
            ACTION_NAME_CROUCH -> {
                if (isPressed)
                    player.order(Move.Crouch)
                else
                    player.order(Move.None)
            }
            else -> {
            }
        }
    }


    companion object {
        const val ACTION_NAME_RUN_RIGHT = "right"
        const val ACTION_NAME_RUN_LEFT = "left"
        const val ACTION_NAME_JUMP = "jump"
        const val ACTION_NAME_CROUCH = "crouch"
    }
}