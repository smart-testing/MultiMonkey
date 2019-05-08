package ru.yandex.testopithecus.rect

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder


open class TRectangle(top: Int, left: Int, right: Int, bottom: Int) {
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
        return if (other is TRectangle) {
            EqualsBuilder()
                    .append(this.top, other.top)
                    .append(this.left, other.left)
                    .append(this.right, other.right)
                    .append(this.top, other.top)
                    .isEquals
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return HashCodeBuilder()
                .append(this.top)
                .append(this.left)
                .append(this.right)
                .append(this.bottom)
                .build()
    }
}
