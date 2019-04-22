package ru.yandex.testopithecus.rect


open class Rectangle(top: Int, left: Int, right: Int, bottom: Int) {
    var top: Int = 0
    var left: Int = 0
    var right: Int = 0
    var bottom: Int = 0

    init {
        this.top = top
        this.left = left
        this.right = right
        this.bottom = bottom
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rectangle

        if (top != other.top) return false
        if (left != other.left) return false
        if (right != other.right) return false
        if (bottom != other.bottom) return false

        return true
    }

    override fun hashCode(): Int {
        var result = top
        result = 31 * result + left
        result = 31 * result + right
        result = 31 * result + bottom
        return result
    }
}
