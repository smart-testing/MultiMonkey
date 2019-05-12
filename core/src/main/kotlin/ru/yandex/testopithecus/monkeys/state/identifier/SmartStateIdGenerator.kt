package ru.yandex.testopithecus.monkeys.state.identifier

import org.apache.commons.lang3.builder.EqualsBuilder
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState


class SmartElementsStateIdGenerator : StateIdGenerator {

    override fun getId(state: UiState): StateId {
        return SmartStateId(state)
    }

}

private class SmartStateId(uiState: UiState) : StateId {

    val elements: Set<UiElement> = createElementsSet(uiState)

    private fun createElementsSet(uiState: UiState): Set<UiElement> {
        val elements = mutableSetOf<UiElement>()
        for (element in uiState.elements) {
            val attributes = mutableMapOf<String, Any>()
            for ((attribute, value) in element.attributes) {
                addAttributeToMap(attributes, attribute, value)
            }
            elements.add(UiElement("", attributes, listOf()))
        }
        return elements
    }

    private fun addAttributeToMap(attributes: MutableMap<String, Any>, attribute: String, value: Any) {
        if (attribute == "text") {
            attributes[attribute] = (value as String).isBlank()
            return
        }
        attributes[attribute] = value
    }

    override fun equals(other: Any?): Boolean {
        if (other is SmartStateId) {
            return EqualsBuilder()
                    .append(elements, other.elements)
                    .isEquals
        }
        return false
    }

    override fun hashCode(): Int {
        return elements.hashCode()
    }
}