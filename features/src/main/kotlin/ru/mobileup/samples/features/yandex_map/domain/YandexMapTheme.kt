package ru.mobileup.samples.features.yandex_map.domain

enum class YandexMapTheme {
    Bright, Default, Dark;

    companion object {
        fun fromString(str: String?) = when (str) {
            Bright.name -> Bright
            Dark.name -> Dark
            else -> Default
        }
    }
}