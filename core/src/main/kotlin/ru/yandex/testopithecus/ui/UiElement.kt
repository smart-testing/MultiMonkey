package ru.yandex.testopithecus.ui

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder

data class UiElement(
        val id: String,
        val attributes: Map<String, Any>,
        val possibleActions: List<String>
) {

    override fun equals(other: Any?): Boolean {
        return if (other is UiElement) {
            EqualsBuilder()
                .append(this.attributes, other.attributes)
                .append(this.possibleActions, other.possibleActions)
                .isEquals
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return HashCodeBuilder()
            .append(this.attributes)
            .append(this.possibleActions)
            .build()
    }
}