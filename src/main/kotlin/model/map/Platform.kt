package model.map

import org.dyn4j.dynamics.PhysicsBody

interface Platform : PhysicsBody {

    val x: Double
    val y: Double
    val width: Double
    val height: Double
}