package ru.yandex.testopithecus

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.request.receiveText
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.routing
import org.json.JSONObject
import ru.yandex.testopithecus.monkeys.state.StateModelMonkey
import ru.yandex.testopithecus.ui.Monkey

val model: Monkey = StateModelMonkey()

fun Application.main() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        post("/generate-action") {
            val jsonState = JSONObject(call.receiveText())
            val uiState = deserializeState(jsonState)
            val action = model.generateAction(uiState)
            call.respond(serializeAction(action))
        }
    }
}
