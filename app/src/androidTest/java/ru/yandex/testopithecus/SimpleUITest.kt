package ru.yandex.testopithecus

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.fail
import org.junit.Test
import ru.yandex.testopithecus.exception.ServerErrorException
import ru.yandex.testopithecus.exception.SessionFinishedException
import ru.yandex.testopithecus.exception.TestFailException
import ru.yandex.testopithecus.exception.TestOkException
import ru.yandex.testopithecus.monkeys.state.StateModelMonkey
import ru.yandex.testopithecus.stateenricher.EmptyEnricher
import ru.yandex.testopithecus.stateenricher.ScreenshotsEnricher
import ru.yandex.testopithecus.system.AndroidMonkeyHttp
import ru.yandex.testopithecus.system.AndroidMonkeyRunner
import ru.yandex.testopithecus.system.AndroidScreenshotManager
import ru.yandex.testopithecus.utils.Reinstaller


class SimpleUiTest {

    private val device = UiDevice.getInstance(getInstrumentation())
    private val context = getApplicationContext<Context>()

    @Test
    fun takeScreenshot() {
        AndroidScreenshotManager.shouldTakeScreenshotDuringRestore()
    }

    @Test
    fun restoreActions() {
        runMonkey(LOG_MODE, DEFAULT_PACKAGE, DEFAULT_DIR + DEFAULT_APK, "minimaltodo")
    }

    @Test
    fun replayTest() {
        runMonkey(REPLAY_MODE, DEFAULT_PACKAGE, DEFAULT_DIR + DEFAULT_APK, "minimaltodo")
    }

    @Test
    fun testApplication() {
        runMonkey(STATE_MODEL_MODE, DEFAULT_PACKAGE, DEFAULT_DIR + DEFAULT_APK)
    }

    @Test
    fun testApplicationWithMetrics() {
        val extras = InstrumentationRegistry.getArguments()
        val pckg = extras["pckg"] as String
        val apk = extras["apk"] as String
        Log.i(LOG_TAG, "pckg: $pckg")
        Log.i(LOG_TAG, "apk: $apk")
        runMonkey(STATE_MODEL_MODE, pckg, "crash/$apk")
    }

    @Test
    fun testApplicationWithScreenshots() {
        runMonkeyScreenshots(DEFAULT_PACKAGE, DEFAULT_DIR + DEFAULT_APK)
    }

    private fun runMonkey(mode: String, pckg: String, apk: String, file: String? = null) {
        Reinstaller.reinstall(device, pckg, apk)
        openApplication(pckg)
        val monkey = AndroidMonkeyRunner(device, pckg, apk, monkey = AndroidMonkeyHttp(mode = mode, file = file))
        for (step in 0 until STEPS_NUMBER) {
            Log.d(STEPS_LOG_TAG, "current step: $step")
            openApplicationIfRequired(pckg)
            try {
                monkey.performAction()
            } catch (e: SessionFinishedException) {
                return
            } catch (e: ServerErrorException) {
                Log.e(LOG_TAG, e.message)
            } catch (e: TestOkException) {
                Log.d(LOG_TAG, "Test passed")
                return
            } catch (e: TestFailException) {
                Log.e(LOG_TAG, e.message)
                if (e.expected.isNotEmpty()) {
                    Log.e(LOG_TAG, "screenshot were saved at " + context.filesDir)
                    AndroidScreenshotManager.parseScreenshotAndSave(e.expected, "expected")
                    AndroidScreenshotManager.parseScreenshotAndSave(e.actual, "actual")
                }
                fail("See logcat for more details")
            }
        }
    }

    private fun runMonkeyScreenshots(pckg: String, apk: String) {
        Reinstaller.reinstall(device, pckg, apk)
        openApplication(pckg)
        val cvServerAddress = "http://${AndroidMonkeyRunner.ANDROID_LOCALHOST}:5000"
        val model = StateModelMonkey(ScreenshotsEnricher(cvServerAddress, EmptyEnricher()))
        val monkey = AndroidMonkeyRunner(device, pckg, apk, screenshotDir = context.filesDir, monkey = model)
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
        private const val STATE_MODEL_MODE = "statemodel"
        private const val LOG_MODE = "log"
        private const val REPLAY_MODE = "replay"
        const val STEPS_LOG_TAG = "STEP_COUNTER"
        private const val LOG_TAG = "MONKEY"
        private const val STEPS_NUMBER = 1000
        private const val LONG_WAIT = 5000L
        private const val DEFAULT_PACKAGE = "com.avjindersinghsekhon.minimaltodo"
        private const val DEFAULT_DIR = "nocrash/"
        private const val DEFAULT_APK = "minimaltodo.apk"
    }
}