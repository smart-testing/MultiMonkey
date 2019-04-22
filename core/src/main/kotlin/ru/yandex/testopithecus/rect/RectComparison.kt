package ru.yandex.testopithecus.rect

object RectComparison {
    fun minDistance(r1: Rectangle, r2: Rectangle): Int {
        var min = Integer.MAX_VALUE
        if (Math.abs(r1.top - r2.bottom) < min)
            min = Math.abs(r1.top - r2.bottom)
        if (Math.abs(r1.bottom - r2.top) < min)
            min = Math.abs(r1.bottom - r2.top)
        if (Math.abs(r1.left - r2.right) < min)
            min = Math.abs(r1.left - r2.right)
        if (Math.abs(r1.right - r2.left) < min)
            min = Math.abs(r1.right - r2.left)
        return min
    }
}
