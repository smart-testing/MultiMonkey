package ru.yandex.testopithecus.system

import android.graphics.Rect
import ru.yandex.testopithecus.rect.TRectangle

class RectAndroid(rect: Rect) : TRectangle(rect.top, rect.left, rect.right, rect.bottom)
