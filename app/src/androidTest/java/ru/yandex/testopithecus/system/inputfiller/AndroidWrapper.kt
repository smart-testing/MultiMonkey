package ru.yandex.testopithecus.system.inputfiller

import android.graphics.Rect
import ru.yandex.testopithecus.rect.Rectangle

class RectAndroid(rect: Rect) : Rectangle(rect.top, rect.left, rect.right, rect.bottom)
