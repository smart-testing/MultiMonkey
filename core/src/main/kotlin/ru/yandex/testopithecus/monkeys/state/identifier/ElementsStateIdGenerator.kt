package ru.yandex.testopithecus.monkeys.state.identifier

import org.apache.commons.lang3.builder.EqualsBuilder
import ru.yandex.testopithecus.ui.UiElement
import ru.yandex.testopithecus.ui.UiState


class ElementsStateIdGenerator : StateIdGenerator {

    override fun getId(state: UiState): StateId {
        return DefaultStateId(state)
    }

}

private class DefaultStateId(uiState: UiState) : StateId {

    val elements: Set<UiElement> = uiState.elements.toSet()

    override fun equals(other: Any?): Boolean {
        if (other is DefaultStateId) {
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