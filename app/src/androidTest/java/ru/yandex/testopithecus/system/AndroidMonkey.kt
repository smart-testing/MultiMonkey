package ru.yandex.testopithecus.system

import androidx.test.uiautomator.By
import androidx.test.uiautomator.StaleObjectException
import androidx.test.uiautomator.UiDevice
import ru.yandex.testopithecus.ui.UiAction
import ru.yandex.testopithecus.ui.UiState

abstract class AndroidMonkey(
        private val device: UiDevice,
        private val applicationPackage: String,
        private val apk: String) {

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
        val action = generateAction(uiState)
        val id = action.id?.toInt()
        val element = id?.let { elements[id] }
        AndroidActionPerformer(device, applicationPackage, apk, element).perform(action)
    }

    protected abstract fun generateAction(uiState: UiState): UiAction
}