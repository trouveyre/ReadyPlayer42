package controler

import model.character.Character
import model.character.MutableCharacter


abstract class LocalPlayer(protected val _character: MutableCharacter) : Player {

    override val character: Character
        get() = _character


    fun react(secondPerFrame: Double) {
        _character.angularVelocity = 0.0
        onReact()
        if (_character.isRunningRight)
            _character.x += _character.speed * secondPerFrame
        if (_character.isRunningLeft)
            _character.x -= _character.speed * secondPerFrame
    }

    open fun onReact() {}
}