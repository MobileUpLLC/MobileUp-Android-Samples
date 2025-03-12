package ru.mobileup.samples.features.tutorial.presentation

import androidx.compose.ui.geometry.Rect
import ru.mobileup.samples.features.tutorial.domain.HighlightableItem

fun HighlightableItem.toRect() = Rect(left, top, right, bottom)

fun Rect.toHighlightableItem() = HighlightableItem(left, top, right, bottom)