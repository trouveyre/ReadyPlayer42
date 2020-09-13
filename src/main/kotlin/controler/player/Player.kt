package controler.player

import model.character.CharacterData


interface Player {

    val name: String
    val character: CharacterData
}