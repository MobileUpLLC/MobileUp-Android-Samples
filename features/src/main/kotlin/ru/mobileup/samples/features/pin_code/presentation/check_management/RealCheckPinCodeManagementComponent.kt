package ru.mobileup.samples.features.pin_code.presentation.check_management

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.essenty.lifecycle.doOnStart
import com.arkivanov.essenty.lifecycle.doOnStop
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.mobileup.samples.core.ComponentFactory
import ru.mobileup.samples.core.utils.toStateFlow
import ru.mobileup.samples.features.pin_code.createCheckPinCodeComponent
import ru.mobileup.samples.features.pin_code.data.PinCodeStorage
import ru.mobileup.samples.features.pin_code.presentation.check.CheckPinCodeComponent
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class RealCheckPinCodeManagementComponent(
    componentContext: ComponentContext,
    componentFactory: ComponentFactory,
    pinCodeStorage: PinCodeStorage
) : ComponentContext by componentContext, CheckPinCodeManagementComponent {

    companion object {
        private val CHECK_PIN_CODE_TIMEOUT: Duration = 30.seconds
    }

    private val navigation = SlotNavigation<Unit>()

    override val checkPinCodeComponentSlot: StateFlow<ChildSlot<*, CheckPinCodeComponent>> =
        childSlot(
            source = navigation,
            serializer = null,
            childFactory = { _, componentContext ->
                componentFactory.createCheckPinCodeComponent(
                    componentContext,
                    ::onCheckPinCodeOutput
                )
            }
        ).toStateFlow(lifecycle)

    private var backgroundStartTime: Instant? = null

    init {
        // runBlocking для того, чтобы на старте приложения точно было проверено наличие пин-кода
        runBlocking {
            pinCodeStorage.getPinCode()?.let {
                navigation.activate(Unit)
            }
        }

        lifecycle.doOnStart {
            runBlocking {
                val time = backgroundStartTime ?: return@runBlocking
                val pinCode = pinCodeStorage.getPinCode() ?: return@runBlocking

                val currentMoment = Clock.System.now()
                val difference = currentMoment - time
                if (difference >= CHECK_PIN_CODE_TIMEOUT) {
                    navigation.activate(Unit)
                }
                backgroundStartTime = null
            }
        }
        lifecycle.doOnStop {
            backgroundStartTime = Clock.System.now()
        }
    }

    private fun onCheckPinCodeOutput(output: CheckPinCodeComponent.Output) {
        when (output) {
            is CheckPinCodeComponent.Output.CheckSucceeded -> navigation.dismiss()
            is CheckPinCodeComponent.Output.LoggedOut -> navigation.dismiss()
        }
    }
}