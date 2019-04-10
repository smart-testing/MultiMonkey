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
import org.json.JSONObject
import ru.yandex.multimonkey.monkeys.state.StateModelMonkey
import ru.yandex.multimonkey.ui.Monkey

val model: Monkey = StateModelMonkey()

fun Application.main() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        get("/") {
            val jsonState = call.receive<JSONObject>()
            val uiState = deserializeState(jsonState)
            val action = model.generateAction(uiState)
            call.respond(serializeAction(action))
        }
    }
}
