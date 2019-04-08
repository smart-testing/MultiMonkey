package ru.yandex.multimonkey

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing

data class UIAction(
        val action: Action
)

data class Action(
        val id: String?,
        val action: ActionType,
        val attributes: Map<String, Any>
)

data class Feedback(
        val feedback: FeedbackData,
        val state: UIState
)

data class FeedbackData(
        val status: FeedbackStatus,
        val error: String
)

enum class FeedbackStatus {
    OK, ERROR
}

data class UIState(
        val elements: List<UIElement>,
        val global: GlobalState
)

data class GlobalState(
        val screenSize: Size,
        val screenshot: String,
        val possibleActions: List<ActionType>
)

data class Size(
        val width: Int,
        val height: Int
)

data class UIElement(
        val id: String,
        val attributes: Attributes,
        val possibleActions: List<ActionType>
)

data class Attributes(
        val resourceName: String,
        val isCheckable: Boolean,
        val center: Point
)

data class Point(
        val x: Int,
        val y: Int
)

enum class ActionType {
    CLICK, SWIPE, INPUT
}

fun Application.main() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        get("/") {
            val uiState = call.receive<UIState>()
            call.respond(UIAction(Action("AAAAFFAFASF", ActionType.INPUT, mapOf("text" to "pastapizza"))))
        }
    }
}
