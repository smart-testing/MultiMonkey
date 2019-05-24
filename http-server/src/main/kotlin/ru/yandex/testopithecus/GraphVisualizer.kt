package ru.yandex.testopithecus

import freemarker.template.Configuration
import java.io.StringWriter


class GraphVisualizer {

    var graphJson = "{}"
    private val cfg = Configuration()

    init {
        cfg.setClassForTemplateLoading(javaClass, "")
    }

    fun buildHtml(): String {
        val root = mutableMapOf<String, String>()
        val template = cfg.getTemplate("templates/test.html")
        val writer = StringWriter()
        template.process(root, writer)
        return writer.toString()
    }

    fun getJs(): String {
        val root = mutableMapOf<String, String>()
        root["graphJson"] = graphJson
        val template = cfg.getTemplate("js/main.js")
        val writer = StringWriter()
        template.process(root, writer)
        return writer.toString()
    }

}