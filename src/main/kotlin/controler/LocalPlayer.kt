package controler

import model.character.Character


abstract class LocalPlayer(override val name: String, protected val _character: Character) : Player {

    override val character: Character
        get() = _character


    fun react(secondPerFrame: Double) {
        onReact()
        if (_character.isRunningRight)
            _character.x += _character.speed * secondPerFrame
        if (_character.isRunningLeft)
            _character.x -= _character.speed * secondPerFrame
    }

    open fun onReact() {}
}