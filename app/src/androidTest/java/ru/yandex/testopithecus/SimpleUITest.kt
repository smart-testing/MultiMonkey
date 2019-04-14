package ru.yandex.testopithecus

import android.content.Context
import android.content.Intent
import android.util.Log

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.uiautomator.*

import org.junit.Test
import ru.yandex.testopithecus.metrics.MetricsEvaluator
import ru.yandex.testopithecus.system.AndroidMonkey

class SimpleUiTest {

    private val device = UiDevice.getInstance(getInstrumentation())
    private val context = getApplicationContext<Context>()

    @Test
    fun testApplication() {
        runMonkey(DEFAULT_PACKAGE)
    }

    @Test
    fun calculateMetrics() {
        for ((apk, pckg) in apps) {
            reinstall(apk, pckg)
            val evaluator = MetricsEvaluator()
            evaluator.start()
            runMonkey(pckg)
            val result = evaluator.result()
            Log.i(METRICS_LOG_TAG, result.toString())
        }
    }

    private fun reinstall(apk: String, pckg: String) {
        Log.i(METRICS_LOG_TAG, "uninstalling $pckg")
        Log.i(METRICS_LOG_TAG, device.executeShellCommand("pm uninstall $pckg"))
        Log.i(METRICS_LOG_TAG, "clearing $pckg")
        Log.i(METRICS_LOG_TAG, device.executeShellCommand("pm clear $pckg"))
        Log.i(METRICS_LOG_TAG, "installing $pckg")
        Log.i(METRICS_LOG_TAG, device.executeShellCommand("pm install -t -r /sdcard/$apk"))
    }

    private fun runMonkey(pckg: String) {
        openApplication(pckg)
        val monkey = AndroidMonkey(device, pckg)
        for (step in 0 until STEPS_NUMBER) {
            openApplicationIfRequired(pckg)
            monkey.performAction()
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
        private const val METRICS_LOG_TAG = "METRICS"
        private const val STEPS_NUMBER = 10
        private const val LONG_WAIT = 5000L
        private const val DEFAULT_PACKAGE = "ru.yandex.mail"
        private val apps = mapOf(
                "minimaltodo.apk" to "com.avjindersinghsekhon.minimaltodo",
                "activitydiary.apk" to "de.rampro.activitydiary.debug",
                "brainstonz.apk" to "eu.veldsoft.brainstonz"
        )
    }
}