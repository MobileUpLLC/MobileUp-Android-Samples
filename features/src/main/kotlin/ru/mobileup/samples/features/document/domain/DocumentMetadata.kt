package ru.mobileup.samples.features.document.domain

data class DocumentMetadata(
    val name: String,
    val size: String,
    val dateModified: String,
    val mime: String,
    val flags: String
)