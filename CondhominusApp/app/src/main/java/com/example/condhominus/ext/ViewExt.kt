package com.example.condhominus.ext

import android.view.View
import android.view.ViewGroup
import androidx.transition.TransitionManager

fun View.visible(animate: Boolean = false) {
    if (this.visibility != View.VISIBLE) {
        when (parent is ViewGroup && animate) {
            true -> TransitionManager.beginDelayedTransition(parent as ViewGroup)
        }
        visibility = View.VISIBLE
    }
}

fun View.gone(animate: Boolean = false) {
    if (this.visibility != View.GONE) {
        when (parent is ViewGroup && animate) {
            true -> TransitionManager.beginDelayedTransition(parent as ViewGroup)
        }
        visibility = View.GONE
    }
}