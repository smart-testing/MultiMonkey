package ru.yandex.testopithecus.web_monkey


import org.openqa.selenium.Rectangle
import ru.yandex.testopithecus.rect.TRectangle

class RectSelenium(rect : Rectangle) : TRectangle(rect.y - rect.height/2,
                                rect.x - rect.width/2,
                                rect.x + rect.width/2,
                                    rect.y + rect.height/2)
