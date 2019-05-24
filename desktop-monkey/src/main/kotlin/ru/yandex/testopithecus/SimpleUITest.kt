package ru.yandex.testopithecus

import org.junit.Before
import org.junit.Test
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import ru.yandex.testopithecus.web_monkey.WebMonkey
import java.net.URL






class SimpleUiTest {

    private var driver: WebDriver = ChromeDriver()
    private val startUrl = "https://yandex.ru"
    private val host = URL(startUrl).host
    private var wait: WebDriverWait = WebDriverWait(driver, 10)

    @Before
    fun start() {
        driver.manage().window().maximize()
        driver.get(startUrl)
    }

    @Test
    fun testApplication() {
        val monkey = WebMonkey(driver)
        for (step in 0 until STEPS_NUMBER) {
            backIfRequired()
            closeTabsIfRequired()
            monkey.performAction()
        }
    }

    private fun backIfRequired() {
        val curUrl = driver.currentUrl
        if (!curUrl.contains(host)) {
            driver.navigate().back()
        }
    }
    private fun closeTabsIfRequired() {
        val tabs = ArrayList(driver.windowHandles)
        if (tabs.size > 1) {
            driver.switchTo().window(tabs[0])
            driver.close()
        }
        for (i in 2 until tabs.size) {
            driver.switchTo().window(tabs[i])
            driver.close()
        }
        if (tabs.size > 1) {
            driver.switchTo().window(tabs[1])
        }
    }

    companion object {
        // TODO add package name
        const val STEPS_NUMBER = 500
        const val LONG_WAIT = 5000L
    }
}