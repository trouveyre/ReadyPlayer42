package model.character


class Character(positionX: Float, positionY: Float) {

    var x: Float = positionX
        get() {
            if (isRunningRight)
                field += speed
            if (isRunningLeft)
                field -= speed
            return field
        }

    var y: Float = positionY

    var action: Move = Move.None

    var orientation: Orientation = Orientation.Right

    var speed: Float = SPEED_DEFAULT_VALUE

    var isRunningRight: Boolean = false
        set(value) {
            if (value)
                isRunningLeft = false
            field = value
        }

    var isRunningLeft: Boolean = false
        set(value) {
            if (value)
                isRunningRight = false
            field = value
        }

    fun stopRunning() {
        isRunningRight = false
        isRunningLeft = false
    }


    override fun toString(): String {
        return "Character($x, $y) ${
            if (isRunningRight) 
                "is running right" 
            else if (isRunningLeft)
                "is running left" 
            else 
                "is not running"
        }"
    }


    companion object {
        const val SPEED_DEFAULT_VALUE: Float = 55f
    }
}