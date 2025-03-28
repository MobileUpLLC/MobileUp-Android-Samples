package ru.mobileup.samples.core.utils

import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity

/**
 * Should be used to handle clicks on toolbar back button.
 *
 * Usage:
 * val context = LocalContext.current
 * ...
 * onClick = {
 *     dispatchOnBackPressed(context)
 * }
 */
fun dispatchOnBackPressed(context: Context) {
    val activity = context.getActivity() ?: return
    activity.onBackPressedDispatcher.onBackPressed()
}

fun Context.getActivity(): FragmentActivity? = when (this) {
    is FragmentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}