package ru.yandex.multimonkey.net

import khttp.post as httpPost

class NetMonkeyClient : Monkey {

    override fun feedback() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun generateAction(netState: NetState): NetAction {
        val headers = mapOf("Content-Type" to "application/json")
        val json = netState.elements
        val response = httpPost(HOST, headers = headers, json = json)
        return NetAction(response.jsonObject)
    }

    companion object {
        private const val HOST = "http://10.0.2.2:5000"
    }
}
