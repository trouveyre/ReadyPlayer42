package controler

import model.character.Move
import model.character.MutableCharacter
import model.character.Orientation

class LivingLocalPlayer(character: MutableCharacter): LocalPlayer(character) {

    fun makeCharacter(move: Move, orientation: Orientation = _character.orientation) {
        _character.orientation = orientation
        _character.action = move
    }
}