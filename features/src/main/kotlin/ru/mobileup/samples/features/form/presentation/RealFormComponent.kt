package ru.mobileup.samples.features.form.presentation

import android.content.Intent
import androidx.core.net.toUri
import com.arkivanov.decompose.ComponentContext
import dev.icerock.moko.resources.desc.strResDesc
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mobileup.kmm_form_validation.control.CheckControl
import ru.mobileup.kmm_form_validation.options.ImeAction
import ru.mobileup.kmm_form_validation.options.KeyboardOptions
import ru.mobileup.kmm_form_validation.options.KeyboardType
import ru.mobileup.kmm_form_validation.options.PasswordVisualTransformation
import ru.mobileup.kmm_form_validation.validation.control.isNotBlank
import ru.mobileup.kmm_form_validation.validation.control.validation
import ru.mobileup.kmm_form_validation.validation.form.FormValidator
import ru.mobileup.kmm_form_validation.validation.form.RevalidateOnValueChanged
import ru.mobileup.kmm_form_validation.validation.form.SetFocusOnFirstInvalidControlAfterValidation
import ru.mobileup.kmm_form_validation.validation.form.ValidateOnFocusLost
import ru.mobileup.samples.core.activity.ActivityProvider
import ru.mobileup.samples.core.error_handling.ErrorHandler
import ru.mobileup.samples.core.error_handling.safeLaunch
import ru.mobileup.samples.core.message.data.MessageService
import ru.mobileup.samples.core.message.domain.Message
import ru.mobileup.samples.core.utils.CheckControl
import ru.mobileup.samples.core.utils.InputControl
import ru.mobileup.samples.core.utils.PhoneNumberVisualTransformation
import ru.mobileup.samples.core.utils.componentScope
import ru.mobileup.samples.core.utils.computed
import ru.mobileup.samples.core.utils.formValidator
import ru.mobileup.samples.core.utils.withProgress
import ru.mobileup.samples.core.R as CoreR

private const val PHONE_PREFIX_DIGIT = "7"
private const val PHONE_DIGIT_COUNT_WITHOUT_PREFIX = 10 // 7 XXX XXX XX XX

class RealFormComponent(
    componentContext: ComponentContext,
    private val activityProvider: ActivityProvider,
    private val errorHandler: ErrorHandler,
    private val messageService: MessageService
) : ComponentContext by componentContext, FormComponent {

    override val phoneInputControl = InputControl(
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Next
        ),
        textTransformation = { text -> text.filter { it.isDigit() } },
        visualTransformation = PhoneNumberVisualTransformation,
        maxLength = PHONE_DIGIT_COUNT_WITHOUT_PREFIX
    )

    override val passwordInputControl = InputControl(
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = PasswordVisualTransformation()
    )

    override val agreementWithTermsCheckControl: CheckControl = CheckControl()

    private val formValidator: FormValidator = formValidator {
        features = listOf(
            ValidateOnFocusLost,
            RevalidateOnValueChanged,
            SetFocusOnFirstInvalidControlAfterValidation
        )

        input(phoneInputControl) {
            isNotBlank(CoreR.string.field_error_is_blank.strResDesc())

            validation(
                { it.length == PHONE_DIGIT_COUNT_WITHOUT_PREFIX },
                CoreR.string.field_error_invalid_format.strResDesc()
            )
        }

        input(passwordInputControl) {
            isNotBlank(CoreR.string.field_error_is_blank.strResDesc())
        }
    }

    override val isLoginEnabled = computed(
        phoneInputControl.text,
        phoneInputControl.error,
        passwordInputControl.text,
        passwordInputControl.error,
        agreementWithTermsCheckControl.checked
    ) { phone, phoneError, password, passwordError, agreementChecked ->
        phone.isNotBlank() && phoneError == null && password.isNotBlank() && passwordError == null && agreementChecked
    }

    override val isLoginInProgress = MutableStateFlow(false)

    init {
        isLoginInProgress
            .onEach {
                agreementWithTermsCheckControl.enabled.value = !it
                phoneInputControl.enabled.value = !it
                passwordInputControl.enabled.value = !it
            }
            .launchIn(componentScope)
    }

    override fun onLoginClick() {
        if (isLoginInProgress.value) return
        val isValid = formValidator.validate().isValid
        if (!isValid) return

        componentScope.safeLaunch(errorHandler) {
            withProgress(isLoginInProgress) {
                val phone = PHONE_PREFIX_DIGIT + phoneInputControl.text.value
                val password = passwordInputControl.text.value
                delay(2000)
                messageService.showMessage(
                    Message(CoreR.string.common_success.strResDesc())
                )
            }
        }
    }

    override fun onAgreementClick(tag: String) {
        componentScope.safeLaunch(errorHandler) {
            when (tag) {
                FormComponent.PRIVACY_POLICY -> {
                    activityProvider.awaitActivity().startActivity(
                        Intent(Intent.ACTION_VIEW, "https://career.habr.com/companies/mobileup".toUri())
                    )
                }
                FormComponent.TERMS_OF_USE_TAG -> {
                    activityProvider.awaitActivity().startActivity(
                        Intent(Intent.ACTION_VIEW, "https://mobileup.ru/".toUri())
                    )
                }
            }
        }
    }
}