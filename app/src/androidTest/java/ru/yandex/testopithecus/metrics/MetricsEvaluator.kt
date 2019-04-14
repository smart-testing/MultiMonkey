package ru.yandex.testopithecus.metrics

import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

class MetricsEvaluator {

    private val metrics = Metrics()

    private val worker: Thread = Thread {
        val timeFormatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss")
        var startTime = LocalDateTime.MIN
        var curStep = 0
        var nextCrashLine = -1
        val uniqueCrashes = mutableSetOf<String>()

        val proc = Runtime.getRuntime().exec("logcat -v time")
        val logcat = proc.inputStream.bufferedReader()
        while (true) {
            val line = logcat.readLine()
            if (Thread.interrupted()) {
                break
            }
            val timestamp = "19-" + line.substring(0, 14)
            val curTime: LocalDateTime? = try {
                LocalDateTime.parse(timestamp, timeFormatter)
            } catch (e: DateTimeParseException) {
                null
            }
            if (curTime != null && startTime == LocalDateTime.MIN) {
                startTime = curTime
            }
            if (nextCrashLine >= 0) {
                nextCrashLine--
            }
            when {
                curTime != null && line.contains("FATAL EXCEPTION: main") -> {
                    val crashTime = ChronoUnit.MILLIS.between(startTime, curTime).toInt()
                    metrics.crashes++
                    if (metrics.steps == 0) {
                        metrics.steps = curStep
                    }
                    if (metrics.time == 0) {
                        metrics.time = crashTime
                    }
                    metrics.meanSteps += curStep
                    metrics.meanTime += crashTime
                    curStep = 0
                    startTime = curTime
                    nextCrashLine = 3
                }
                line.contains("clickNoSync") -> curStep++
                nextCrashLine == 0 -> {
                    val crashLine = line.substringAfter("AndroidRuntime:")
                    if (uniqueCrashes.add(crashLine)) {
                        metrics.uniqueCrashes++
                    }
                }
            }
        }
    }

    fun start() {
        worker.start()
    }

    fun result(): Metrics {
        worker.interrupt()
        Log.i(LOG_TAG,"interrupting worker")
        worker.join()
        return metrics
    }

    companion object {
        private const val LOG_TAG = "METRICS_EVALUATOR"
    }
}