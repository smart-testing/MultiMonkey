package ru.yandex.multimonkey.monkeys.state.identifier

import ru.yandex.multimonkey.net.UiState


class DefaultStateIdGenerator : StateIdGenerator<StateId> {

    override fun getId(state: UiState): StateId {
        return DefaultStateId(state)
    }

}

private class DefaultStateId(val uiState: UiState) : StateId {

    override fun equals(other: Any?): Boolean {
        if (other is DefaultStateId) {
            return uiState == other.uiState
        }
        return false
    }

    override fun hashCode(): Int {
        return uiState.hashCode()
    }
}