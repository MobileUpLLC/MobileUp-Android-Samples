package ru.mobileup.samples.core.permissions

/**
 * A result of single permission request.
 */
sealed class SinglePermissionResult {

    /**
     * Permission has been granted by user
     */
    object Granted : SinglePermissionResult()

    /**
     * Permission has been denied by user
     * If [permanently] == true permission was denied permanently (user chose "Never ask again")
     * If [automatically] == true permission was denied automatically (user didn't interact with the request)
     */
    class Denied(val permanently: Boolean, val automatically: Boolean) : SinglePermissionResult()
}
