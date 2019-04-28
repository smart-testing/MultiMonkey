package ru.yandex.testopithecus

import org.junit.Before
import org.junit.Test
import org.openqa.selenium.*
import org.openqa.selenium.support.ui.WebDriverWait
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import ru.yandex.testopithecus.web_monkey.WebMonkey
import org.openqa.selenium.interactions.Actions
import java.util.concurrent.TimeUnit


class SimpleUiTest {

    private var driver: WebDriver = ChromeDriver()
    private val startUrl = "https://passport.yandex.ru/auth?origin=home_desktop_ru&retpath=https%3A%2F%2Fmail.yandex.ru%2F%2F%3Fmsid%3D1556391525.95971.140394.125530%26m_pssp%3Ddomik&backpath=https%3A%2F%2Fyandex.ru"
    private var wait: WebDriverWait = WebDriverWait(driver, 10)

    @Before
    fun start() {
        driver.manage().window().maximize()
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
        driver.get(startUrl)
        authorization(driver)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mail-ComposeButton-Text")))
        driver.findElement(By.cssSelector(".mail-ComposeButton-Text")).click()
    }

    @Test
    fun testApplication() {
        val monkey = WebMonkey(driver)
        for (step in 0 until STEPS_NUMBER) {
            monkey.performAction()
            closeTabsIfRequired()
        }
    }

    private fun authorization(driver: WebDriver) {
        driver.get("https://passport.yandex.ru/auth?origin=home_desktop_ru&retpath=https%3A%2F%2Fmail.yandex.ru%2F%2F%3Fmsid%3D1555972465.1898.140412.966154%26m_pssp%3Ddomik&backpath=https%3A%2F%2Fyandex.ru")
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#passp-field-login")))
        driver.findElement(By.cssSelector("input[id=passp-field-login]")).sendKeys("apktestandroid")
        driver.findElement(By.tagName("button")).click()

        val action = Actions(driver)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input#passp-field-passwd")))
        val password: WebElement = driver.findElement(By.cssSelector("input#passp-field-passwd"))
        action.moveToElement(password).sendKeys("apktest").click().perform()
        driver.findElement(By.tagName("button")).click()
    }

    private fun closeTabsIfRequired() {
        val tabs = ArrayList(driver.windowHandles)
        if (tabs.size == 2) {
            driver.switchTo().window(tabs[1])
            driver.close()
        }
        driver.switchTo().window(tabs[0])
    }

    companion object {
        // TODO add package name
        const val STEPS_NUMBER = 500
    }
}