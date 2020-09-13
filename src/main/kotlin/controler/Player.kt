package controler

import model.character.CharacterData


interface Player {

    val name: String
    val character: CharacterData
}