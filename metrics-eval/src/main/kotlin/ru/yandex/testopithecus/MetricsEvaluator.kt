package ru.yandex.testopithecus

import java.io.BufferedReader
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

class MetricsEvaluator {

    private val metrics = Metrics()
    private lateinit var logcat: BufferedReader

    private val worker: Thread = Thread {
        val pattern = "yyyy-MM-dd HH:mm:ss"
        val timeFormatter = DateTimeFormatter.ofPattern(pattern)
        var startTime = LocalDateTime.MIN
        var curStep = 0
        var nextCrashLine = -1
        val uniqueCrashes = mutableSetOf<String>()

        while (true) {
            val line = logcat.readLine()
            if (Thread.interrupted() || line == null || line.contains("MONKEY_FINISH_LOG_TAG")) {
                break
            }
            val timestamp = line.substring(0, pattern.length)
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
                line.contains("STEPS_LOG_TAG") -> curStep++
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
        val proc = Runtime.getRuntime().exec("adb logcat -v time -v year")
        start(proc.inputStream)
    }

    private fun start(input: InputStream) {
        logcat = input.bufferedReader()
        worker.start()
    }

    fun result(interrupt: Boolean): Metrics {
        if (interrupt) {
            worker.interrupt()
        }
        worker.join()
        if (metrics.crashes > 0) {
            metrics.meanSteps /= metrics.crashes
            metrics.meanTime /= metrics.crashes
        }
        return metrics
    }
}