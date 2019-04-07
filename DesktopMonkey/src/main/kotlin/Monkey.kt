package net.netau.vasyoid

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class Monkey {

    private val driver: ChromeDriver

    init {
        System.setProperty(
            "webdriver.chrome.driver",
            "/home/vasyoid/WebstormProjects/school_timetable/node_modules/.bin/chromedriver"
        )
        val options = ChromeOptions()
        options.setBinary("/home/vasyoid/WebstormProjects/school_timetable/Timetable-linux-x64/Timetable")
        driver = ChromeDriver(options)
    }

    fun run(steps: Int) {
        val monkeyController = MonkeyController(driver)
        for (step in 1..steps) {
            val action = monkeyController.generateAction()
            println(action.position)
            action.perform(driver)
        }
        driver.quit()
    }
}

fun main() {
    Monkey().run(100)
}
