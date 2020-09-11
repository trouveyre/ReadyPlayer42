package model.map

import org.dyn4j.dynamics.Body
import org.dyn4j.geometry.Geometry
import org.dyn4j.geometry.MassType


data class StaticPlatform(
    override val x: Double,
    override val y: Double,
    override val width: Double,
    override val height: Double = 1.0
) : Body(), Platform {

    init {
        addFixture(Geometry.createRectangle(width * 2, height * 2))
        setMass(MassType.INFINITE)
        translate(x, y)
    }
}