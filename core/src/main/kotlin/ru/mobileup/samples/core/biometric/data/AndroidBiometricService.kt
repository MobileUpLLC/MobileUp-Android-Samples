package ru.mobileup.samples.core.biometric.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.BLOCK_MODE_CBC
import android.security.keystore.KeyProperties.ENCRYPTION_PADDING_PKCS7
import android.security.keystore.KeyProperties.KEY_ALGORITHM_AES
import android.security.keystore.KeyProperties.PURPOSE_DECRYPT
import android.security.keystore.KeyProperties.PURPOSE_ENCRYPT
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import ru.mobileup.samples.core.activity.ActivityProvider
import ru.mobileup.samples.core.biometric.domain.BiometricAuthResult
import ru.mobileup.samples.core.biometric.domain.BiometricSupportStatus
import ru.mobileup.samples.core.biometric.domain.BiometricType
import java.security.KeyStore
import java.security.KeyStoreException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

class AndroidBiometricService(
    context: Context,
    private val activityProvider: ActivityProvider
) : BiometricService {

    companion object {
        private const val KEYSTORE_NAME = "AndroidKeyStore"
        private const val KEY_NAME = "my_key"
        private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
        private const val KEY_PURPOSE = PURPOSE_ENCRYPT or PURPOSE_DECRYPT
    }

    override val biometricType = BiometricType.Fingerprint

    private val cipher by lazy { Cipher.getInstance(TRANSFORMATION) }
    private val biometricManager = BiometricManager.from(context)
    private var callback: ((authStatus: BiometricAuthResult) -> Unit)? = null
    private val executor = ContextCompat.getMainExecutor(context)
    private val keyStore by lazy {
        try {
            KeyStore.getInstance(KEYSTORE_NAME)
        } catch (e: KeyStoreException) {
            null
        }
    }

    private val keyGenerator by lazy {
        try {
            KeyGenerator.getInstance(KEY_ALGORITHM_AES, KEYSTORE_NAME)
        } catch (e: Exception) {
            null
        }
    }

    private val biometricPrompt: BiometricPrompt?
        get() = activityProvider.activity?.let {
            BiometricPrompt(it, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence,
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    val status = when (errorCode) {
                        BiometricPrompt.ERROR_LOCKOUT -> BiometricAuthResult.TooManyAttempts
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON -> BiometricAuthResult.Cancel
                        BiometricPrompt.ERROR_USER_CANCELED,
                        BiometricPrompt.ERROR_CANCELED -> BiometricAuthResult.Cancel

                        else -> BiometricAuthResult.Failed
                    }
                    callback?.invoke(status)
                    callback = null
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    super.onAuthenticationSucceeded(result)
                    callback?.invoke(BiometricAuthResult.Success)
                    callback = null
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback?.invoke(BiometricAuthResult.Failed)
                }
            }
            )
        }

    private val cryptoObject: BiometricPrompt.CryptoObject?
        get() = initCipher()?.let { cipher -> BiometricPrompt.CryptoObject(cipher) }

    override fun getBiometricSupportStatus(): BiometricSupportStatus {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricSupportStatus.Supported
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricSupportStatus.NotEnrolled
            else -> BiometricSupportStatus.NotSupported
        }
    }

    override fun startBiometricAuth(
        callback: (authStatus: BiometricAuthResult) -> Unit
    ) {
        this.callback = callback
        cryptoObject?.let { cryptoObject ->
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Аутентификация")
                .setDescription("Для входа в приложение необходима аутентификация")
                .setNegativeButtonText("Отмена")
                .build()
            biometricPrompt?.authenticate(promptInfo, cryptoObject)
        }
    }

    private fun initCipher(): Cipher? {
        try {
            val secretKey = keyStore?.let { keyStore ->
                keyStore.load(null)
                createKey()
                return@let keyStore.getKey(KEY_NAME, null) as? SecretKey
            }
            cipher?.init(Cipher.ENCRYPT_MODE, secretKey)
            return cipher
        } catch (e: Exception) {
            return null
        }
    }

    private fun createKey() {
        val spec = KeyGenParameterSpec.Builder(KEY_NAME, KEY_PURPOSE)
            .setBlockModes(BLOCK_MODE_CBC)
            .setUserAuthenticationRequired(true)
            .setEncryptionPaddings(ENCRYPTION_PADDING_PKCS7)
            .build()

        keyGenerator?.init(spec)
        keyGenerator?.generateKey()
    }
}
