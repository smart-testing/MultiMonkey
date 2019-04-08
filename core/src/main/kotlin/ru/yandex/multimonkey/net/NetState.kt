package ru.yandex.multimonkey.net

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder


class NetState(unsortedElements: List<NetElement>) {

    val elements = unsortedElements.sorted()

    override fun equals(other: Any?): Boolean {
        return if (other is NetState) {
            EqualsBuilder()
                .append(elements, other.elements)
                .isEquals
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return HashCodeBuilder()
            .append(this.elements)
            .toHashCode()
    }

}