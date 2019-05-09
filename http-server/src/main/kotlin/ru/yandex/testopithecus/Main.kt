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
import ru.yandex.testopithecus.monkeys.log.RestoreMonkey
import ru.yandex.testopithecus.monkeys.log.ReplayMonkey
import ru.yandex.testopithecus.monkeys.state.StateModelMonkey
import ru.yandex.testopithecus.ui.Monkey
import ru.yandex.testopithecus.utils.deserializeState
import ru.yandex.testopithecus.utils.serializeAction
import java.io.File

var model: Monkey = StateModelMonkey()
val logFile = File("logs/common.log")

fun Application.main() {
    logFile.delete()
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
            val resp = serializeAction(action).toString()
            call.respond(resp)
        }
    }
    routing {
        post("/log") {
            val log = call.receiveText()
            println("log: $log")
            (model as? RestoreMonkey)?.appendToLog(log) ?: logFile.appendText(log + "\n")
        }
    }
    routing {
        post("/init/{mode}/{recordName?}") {
            logFile.delete()
            val mode = call.parameters["mode"]
            model = when (mode?.toLowerCase()) {
                "statemodel" -> StateModelMonkey()
                "log" -> {
                    val file = call.parameters["recordName"]
                    println(file)
                    file?.let { RestoreMonkey(it) } ?: StateModelMonkey()
                }
                "replay" -> {
                    val file = call.parameters["recordName"]
                    file?.let { ReplayMonkey(it, logFile) } ?: StateModelMonkey()
                }
                else -> StateModelMonkey()
            }
        }
    }
}
