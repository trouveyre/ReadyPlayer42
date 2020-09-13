package controler

import model.character.Move
import model.character.Character
import model.character.Orientation

class LivingLocalPlayer(name: String, character: Character): LocalPlayer(name, character) {

    fun makeCharacter(move: Move, orientation: Orientation = _character.orientation) {
        _character.orientation = orientation
        _character.action = move
    }
}