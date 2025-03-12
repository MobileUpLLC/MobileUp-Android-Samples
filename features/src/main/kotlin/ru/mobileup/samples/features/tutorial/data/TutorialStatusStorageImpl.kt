package ru.mobileup.samples.features.tutorial.data

import ru.mobileup.samples.core.settings.SettingsFactory
import ru.mobileup.samples.features.tutorial.domain.TutorialStatus

class TutorialStatusStorageImpl(
    settingsFactory: SettingsFactory
) : TutorialStatusStorage {

    companion object {
        private const val KEY_TUTORIAL_STATUS = "tutorial_status"
    }
    private val settings = settingsFactory.createSettings("tutorial_status")

    override suspend fun getStatus(): TutorialStatus {
        return settings
            .getInt(KEY_TUTORIAL_STATUS)
            .mapToTutorialStatus()
    }

    override suspend fun setStatus(status: TutorialStatus) {
        settings.putInt(KEY_TUTORIAL_STATUS, status.mapToInt())
    }
}

private fun TutorialStatus.mapToInt() = when (this) {
    TutorialStatus.NotStarted -> -2
    TutorialStatus.Finished -> -1
    is TutorialStatus.InProgress -> messageIndex
}

private fun Int?.mapToTutorialStatus() = when (this) {
    null, -2 -> TutorialStatus.NotStarted
    -1 -> TutorialStatus.Finished
    else -> TutorialStatus.InProgress(this)
}