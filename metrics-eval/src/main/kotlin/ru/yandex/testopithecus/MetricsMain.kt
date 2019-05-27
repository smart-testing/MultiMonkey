package ru.yandex.testopithecus

object MetricsMain {

    @JvmStatic
    fun main(args: Array<String>) {
        for ((apk, pckg) in apps) {
            val evaluator = MetricsEvaluator()
            evaluator.start()
            val monkey = Runtime.getRuntime().exec(
                    "adb shell am instrument -w -r -e debug false " +
                            "-e apk '$apk' -e pckg '$pckg' " +
                            "-e class 'ru.yandex.testopithecus.SimpleUiTest#testApplicationWithMetrics' " +
                            "ru.yandex.testopithecus.test/androidx.test.runner.AndroidJUnitRunner"
            )
            monkey.inputStream.copyTo(System.out)
            monkey.errorStream.copyTo(System.err)
            monkey.waitFor()
            val result = evaluator.result(true)
            println(result.toString())
        }
    }

    private val apps = mapOf(
            "activitydiary.apk" to "de.rampro.activitydiary.debug",
            "minimaltodo.apk" to "com.avjindersinghsekhon.minimaltodo",
            "brainstonz.apk" to "eu.veldsoft.brainstonz"
    )
}
