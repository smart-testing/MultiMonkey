package ru.yandex.testopithecus

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.yandex.testopithecus.metrics.MetricsEvaluator

class UnitTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun logcatTest() {
        val logcat = context.resources.openRawResource(R.raw.logcat)
        val evaluator = MetricsEvaluator()
        evaluator.start(logcat)
        val result = evaluator.result(false)
        assertEquals(60000, result.time)
        assertEquals(37, result.steps)
        assertEquals(1, result.uniqueCrashes)
        println(result)
    }
}