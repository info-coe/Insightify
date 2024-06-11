package com.infomerica.insightify.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResponseFormatter {

    private var responseValue: String = ""

    fun setResponse(response: String): ResponseFormatter {
        responseValue = response.trim()
        return this
    }

    suspend fun parseResponse(
        result: (MutableList<QACodeReferenceInfo>) -> Unit
    ): ResponseFormatter {
        val responses = mutableListOf<QACodeReferenceInfo>()
        val lines = responseValue.lines()

        var currentResponse: QACodeReferenceInfo? = null
        var codeBlock = false

        withContext(Dispatchers.IO) {
            for (line in lines) {
                when {
                    line.trimStart().startsWith("Question") -> {
                        currentResponse?.let {
                            responses.add(it)
                        }
                        currentResponse = QACodeReferenceInfo(
                            question = line.substringAfter("Question").trim(':').trim(),
                            answer = "",
                            code = "",
                            reference = "",
                            referenceLink = ""
                        )
                    }
                    line.trimStart().startsWith("Answer:") -> currentResponse?.answer = line.substringAfter("Answer:\n").trim()
                    line.trimStart().startsWith("Code:") -> {
                        codeBlock = true
                        currentResponse?.code = line.substringAfter("Code:").trim()
                    }
                    codeBlock && line.trim() != "```" -> currentResponse?.code += "\n$line"
                    codeBlock && line.trim() == "```" -> codeBlock = false
                    line.trimStart().startsWith("Reference:") -> currentResponse?.reference = line.substringAfter("Reference:").trim()
                    line.trimStart().startsWith("ReferenceLink:") -> currentResponse?.referenceLink = line.substringAfter("ReferenceLink:").trim()
                }
            }
        }

        currentResponse?.let {
            responses.add(it)
        }
        result(responses)
        return this
    }
}

data class QACodeReferenceInfo(
    val question: String,
    var answer: String,
    var code: String,
    var reference: String,
    var referenceLink: String
)
