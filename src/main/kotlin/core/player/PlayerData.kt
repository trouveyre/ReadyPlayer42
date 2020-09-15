package core.player

import core.character.CharacterData


interface PlayerData {

    val name: String
    val color: String
    val character: CharacterData


    companion object {
        val ManufacturedNames = setOf(
            "OnePlayerReady",
            "xXDarkBlueXx",
            "John",
            "OSS OS",
            "RedDragon",
            "ll1ll111l1l",
            "ProfessorY",
            "MadMan",
            "Kiriko",
            "Nathalie",
            "MyName"
        )
        val ManufacturedColors = setOf(
            "#cc0000",
            "#009933",
            "#003366",
            "#660066",
            "#ff6600",
            "#800000",
            "#663300",
            "#cc66ff",
            "#00ffff",
            "#ccff99",
            "#669999",
            "#666699"
        )
    }
}