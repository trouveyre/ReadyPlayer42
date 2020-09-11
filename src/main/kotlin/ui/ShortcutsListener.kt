package ui

import com.jme3.input.controls.ActionListener
import controler.LivingLocalPlayer
import model.character.Move
import model.character.Orientation


class ShortcutsListener(private val player: LivingLocalPlayer) : ActionListener {
    override fun onAction(name: String, isPressed: Boolean, tpf: Float) {
        when (name) {
            ACTION_NAME_RUN_RIGHT -> {
                if (isPressed)
                    player.makeCharacter(Move.Run, Orientation.Right)
                else
                    player.makeCharacter(Move.None)
            }
            ACTION_NAME_RUN_LEFT -> {
                if (isPressed)
                    player.makeCharacter(Move.Run, Orientation.Left)
                else
                    player.makeCharacter(Move.None)
            }
            ACTION_NAME_JUMP -> {
                if (isPressed)
                    player.makeCharacter(Move.Jump)
            }
            ACTION_NAME_CROUCH -> {
                if (isPressed)
                    player.makeCharacter(Move.Crouch)
                else
                    player.makeCharacter(Move.None)
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