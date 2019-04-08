package ru.yandex.multimonkey.net

import org.apache.commons.lang3.builder.CompareToBuilder
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder


data class NetElement(
    val center : Point,
    val isCheckable: Boolean,
    val isClickable: Boolean,
    val isFocusable: Boolean,
    val isLongClickable: Boolean,
    val resourceName: String?,
    val className: String?
): Comparable<NetElement> {

    override fun equals(other: Any?): Boolean {
        return if (other is NetElement) {
            EqualsBuilder()
                .append(this.isCheckable, other.isCheckable)
                .append(this.isClickable, other.isClickable)
                .append(this.isLongClickable, other.isLongClickable)
                .append(this.isFocusable, other.isFocusable)
                .append(this.isFocusable, other.isFocusable)
                .append(this.resourceName, other.resourceName)
                .append(this.className, other.className)
                .isEquals
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return HashCodeBuilder()
            .append(this.isCheckable)
            .append(this.isClickable)
            .append(this.isFocusable)
            .append(this.isLongClickable)
            .append(this.resourceName)
            .append(this.className)
            .build()
    }

    override fun compareTo(other: NetElement): Int {
        return CompareToBuilder()
            .append(this.isCheckable, other.isCheckable)
            .append(this.isClickable, other.isClickable)
            .append(this.isLongClickable, other.isLongClickable)
            .append(this.isFocusable, other.isFocusable)
            .append(this.isFocusable, other.isFocusable)
            .append(this.resourceName, other.resourceName)
            .append(this.className, other.className)
            .toComparison()
    }
}