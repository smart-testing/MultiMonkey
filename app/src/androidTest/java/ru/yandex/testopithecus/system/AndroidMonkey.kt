package ru.yandex.testopithecus.system

import androidx.test.uiautomator.*
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.monkeys.state.StateModelMonkey


class AndroidMonkey(private val device: UiDevice, private val applicationPackage: String) {

    private val model: Monkey = StateModelMonkey()


    fun performAction() {
        try {
            performActionImpl()
        } catch (e: StaleObjectException) {
            System.err.println(e.localizedMessage)
        }
    }

    private fun performActionImpl() {
        val elements = device.findObjects(By.pkg(applicationPackage))
        val uiState = AndroidElementParser.parse(elements)
        val action = model.generateAction(uiState)
        val id = action.id?.toInt()
        val element = if (id != null) elements[id] else return
        AndroidActionPerformer(element).perform(action)
    }
}