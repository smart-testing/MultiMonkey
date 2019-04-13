package ru.yandex.multimonkey

import android.content.Context
import android.content.Intent

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.uiautomator.*

import org.junit.Before
import org.junit.Test
import ru.yandex.multimonkey.system.SystemMonkey

class SimpleUiTest {

    private val device = UiDevice.getInstance(getInstrumentation())
    private val context = getApplicationContext<Context>()

    @Before
    fun launchApp() {
        openApplication()
    }

    @Test
    fun testApplication() {
        val monkey = SystemMonkey(device)
        for (step in 0 until STEPS_NUMBER) {
            openApplicationIfRequired()
            val action = monkey.generateAction() ?: continue
            try {
                action.perform()
            } catch (e: StaleObjectException) {}
        }
    }

    private fun openApplicationIfRequired() {
        if (device.currentPackageName != APPLICATION_PACKAGE) {
            openApplication()
        }
    }

    private fun openApplication() {
        device.pressHome()
        val intent = context.packageManager.getLaunchIntentForPackage(APPLICATION_PACKAGE)
        if (intent == null) {
            throw IllegalArgumentException("No application '$APPLICATION_PACKAGE'")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(APPLICATION_PACKAGE).depth(0)), LONG_WAIT)
    }

    companion object {
        // TODO add package name
        const val APPLICATION_PACKAGE = "ru.yandex.mail"
        const val STEPS_NUMBER = 500
        const val LONG_WAIT = 5000L
    }
}