package ru.mobileup.samples.features.tutorial.domain

sealed interface TutorialStatus {
    data object NotStarted : TutorialStatus
    data class InProgress(val messageIndex: Int) : TutorialStatus
    data object Finished : TutorialStatus
}