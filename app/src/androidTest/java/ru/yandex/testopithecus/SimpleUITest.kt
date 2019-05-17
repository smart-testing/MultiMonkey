package ru.yandex.testopithecus

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Test
import ru.yandex.testopithecus.exception.SessionFinishedException
import ru.yandex.testopithecus.metrics.MetricsEvaluator
import ru.yandex.testopithecus.system.AndroidMonkey
import ru.yandex.testopithecus.utils.Reinstaller

class SimpleUiTest {

    private val device = UiDevice.getInstance(getInstrumentation())
    private val context = getApplicationContext<Context>()

    @Test
    fun testApplication() {
        runMonkey(DEFAULT_PACKAGE, DEFAULT_APK)
    }

    @Test
    fun testApplicationWithScreenshots() {
        runMonkeyScreenshots(DEFAULT_PACKAGE, DEFAULT_APK)
    }

    @Test
    fun calculateMetrics() {
        for ((apk, pckg) in apps) {
            Reinstaller.reinstall(device, pckg, apk)
            val evaluator = MetricsEvaluator()
            evaluator.start()
            runMonkey(pckg, apk)
            val result = evaluator.result(true)
            Log.i(METRICS_LOG_TAG, result.toString())
        }
    }


    private fun runMonkey(pckg: String, apk: String) {
        Reinstaller.reinstall(device, pckg, apk)
        openApplication(pckg)
        val monkey = AndroidMonkey(device, pckg, apk, useHTTP = true)
        for (step in 0 until STEPS_NUMBER) {
            Log.d(STEPS_LOG_TAG, "current step: $step")
            openApplicationIfRequired(pckg)
            try {
                monkey.performAction()
            } catch (e: SessionFinishedException) {
                return
            }
        }
    }

    private fun runMonkeyScreenshots(pckg: String, apk: String) {
        Reinstaller.reinstall(device, pckg, apk)
        openApplication(pckg)
        val monkey = AndroidMonkey(device, pckg, apk, screenshotDir = context.filesDir,
                url = "http://${AndroidMonkey.ANDROID_LOCALHOST}:5000")
        for (step in 0 until STEPS_NUMBER) {
            Log.d(STEPS_LOG_TAG, "current step: $step")
            openApplicationIfRequired(pckg)
            try {
                monkey.performAction()
            } catch (e: SessionFinishedException) {
                return
            }
        }
    }

    private fun openApplicationIfRequired(pckg: String) {
        if (device.currentPackageName != pckg) {
            openApplication(pckg)
        }
    }

    private fun openApplication(pckg: String) {
        device.pressHome()
        val intent = context.packageManager.getLaunchIntentForPackage(pckg)
                ?: throw IllegalArgumentException("No application '$pckg'")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(pckg).depth(0)), LONG_WAIT)
    }

    companion object {
        const val STEPS_LOG_TAG = "STEP_COUNTER"
        private const val METRICS_LOG_TAG = "METRICS"
        private const val STEPS_NUMBER = 10000
        private const val LONG_WAIT = 5000L
        private const val DEFAULT_PACKAGE = "com.avjindersinghsekhon.minimaltodo"
        private const val DEFAULT_APK = "minimaltodo.apk"
        private val apps = mapOf(
                "minimaltodo.apk" to "com.avjindersinghsekhon.minimaltodo",
                "activitydiary.apk" to "de.rampro.activitydiary.debug",
                "brainstonz.apk" to "eu.veldsoft.brainstonz"
        )
    }
}