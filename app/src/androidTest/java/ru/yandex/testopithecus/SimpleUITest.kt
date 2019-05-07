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
        for ((apk, pkg) in apps) {
            reinstall(apk, pkg)
            val evaluator = MetricsEvaluator()
            evaluator.start()
            runMonkey(pkg)
            val result = evaluator.result(true)
            Log.i(METRICS_LOG_TAG, result.toString())
        }
    }

    private fun reinstall(apk: String, pckg: String) {
        Log.i(METRICS_LOG_TAG, "uninstalling $pckg")
        Log.i(METRICS_LOG_TAG, device.executeShellCommand("pm uninstall $pckg"))
        Log.i(METRICS_LOG_TAG, "clearing $pckg")
        Log.i(METRICS_LOG_TAG, device.executeShellCommand("pm clear $pckg"))
        Log.i(METRICS_LOG_TAG, "installing $pckg")
        Log.i(METRICS_LOG_TAG, device.executeShellCommand("pm install -t -r /data/local/tmp/apks/$apk"))
    }

    private fun runMonkey(pkg: String) {
        openApplication(pkg)
        val monkey = AndroidMonkey(device, pkg)
        for (step in 0 until STEPS_NUMBER) {
            Log.d(STEPS_LOG_TAG, "current step: $step")
            openApplicationIfRequired(pkg)
            monkey.performAction()
        }
    }


    private fun openApplicationIfRequired(pkg: String) {
        if (device.currentPackageName != pkg) {
            openApplication(pkg)
        }
    }

    private fun openApplication(pkg: String) {
        device.pressHome()
        val intent = context.packageManager.getLaunchIntentForPackage(pkg)
                ?: throw IllegalArgumentException("No application '$pkg'")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        device.wait(Until.hasObject(By.pkg(pkg).depth(0)), LONG_WAIT)
    }

    companion object {
        const val STEPS_LOG_TAG = "STEP_COUNTER"
        private const val METRICS_LOG_TAG = "METRICS"
        private const val STEPS_NUMBER = 100
        private const val LONG_WAIT = 5000L
        private const val DEFAULT_PACKAGE = "ru.yandex.mail"
        private val apps = mapOf(
                "minimaltodo.apk" to "com.avjindersinghsekhon.minimaltodo",
                "activitydiary.apk" to "de.rampro.activitydiary.debug",
                "brainstonz.apk" to "eu.veldsoft.brainstonz"
        )
    }
}