package ru.mobileup.samples.features.tutorial.data

import ru.mobileup.samples.features.tutorial.domain.TutorialStatus

interface TutorialStatusStorage {
    suspend fun getStatus(): TutorialStatus
    suspend fun setStatus(status: TutorialStatus)
}