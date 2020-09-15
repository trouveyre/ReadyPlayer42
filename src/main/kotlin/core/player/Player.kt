package core.player

import core.character.Character
import core.character.Move
import core.character.Orientation


abstract class Player(
    override val name: String,
    override val color: String,
    private val _character: Character
) : PlayerData {

    override val character: Character
        get() = _character

    fun order(move: Move, orientation: Orientation = _character.orientation) {
        _character.orientation = orientation
        _character.action = move
        onOrder(move, orientation)
    }

    open fun onOrder(move: Move, orientation: Orientation) {}

    fun react(elapsedTime: Double) {
        onReact()
        _character.nextPosition(elapsedTime)
    }

    open fun onReact() {}
}