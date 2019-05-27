package ru.yandex.testopithecus

object MetricsMain {

    fun reinstall(pckg: String, apk: String) {
        Runtime.getRuntime().exec("adb shell pm uninstall $pckg")
        Runtime.getRuntime().exec("adb shell pm clear $pckg")
        Runtime.getRuntime().exec("adb shell pm install -t -r /data/local/tmp/apks/$apk")
    }

    @JvmStatic
    fun main(args: Array<String>) {
        for ((apk, pckg) in apps) {
            reinstall(pckg, apk)
            val evaluator = MetricsEvaluator()
            evaluator.start()
            val monkey = Runtime.getRuntime().exec(
                    "adb shell am instrument -w -r -e debug false" +
                            "-e class 'ru.yandex.testopithecus.SimpleUiTest#testApplication'" +
                            "ru.yandex.testopithecus.test/androidx.test.runner.AndroidJUnitRunner"
            )
            monkey.waitFor()
            val result = evaluator.result(true)
            println(result.toString())
        }
    }

    private val apps = mapOf(
            "minimaltodo.apk" to "com.avjindersinghsekhon.minimaltodo",
            "activitydiary.apk" to "de.rampro.activitydiary.debug",
            "brainstonz.apk" to "eu.veldsoft.brainstonz"
    )
}
