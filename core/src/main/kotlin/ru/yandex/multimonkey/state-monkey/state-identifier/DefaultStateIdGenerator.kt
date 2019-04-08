package ru.yandex.multimonkey.`state-monkey`.`state-identifier`

import ru.yandex.multimonkey.net.UiState


class DefaultStateIdGenerator : StateIdGenerator<StateId> {

    override fun getId(state: UiState): StateId {
        return DefaultStateId(state)
    }

}

private class DefaultStateId(val uiState: UiState) : StateId