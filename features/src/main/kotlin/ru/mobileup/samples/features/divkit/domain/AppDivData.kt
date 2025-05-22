package ru.mobileup.samples.features.divkit.domain

import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div2.DivData
import org.json.JSONObject
import ru.mobileup.samples.core.error_handling.DeserializationException

data class AppDivData(
    val card: JSONObject,
    val templates: JSONObject?
) {

    companion object {
        private const val CARD_OBJECT = "card"
        private const val TEMPLATES_OBJECT = "templates"

        fun fromString(json: String): AppDivData = JSONObject(json).run {
            AppDivData(
                card = getJSONObject(CARD_OBJECT) ?: throw DeserializationException(
                    IllegalArgumentException("DivKitUiData.fromJson error while parse $json")
                ),
                templates = getJSONObject(TEMPLATES_OBJECT)
            )
        }
    }

    fun toDivData(parser: DivParsingEnvironment) = DivData(
        env = parser.apply { this@AppDivData.templates?.let(::parseTemplates) },
        json = card
    )
}
