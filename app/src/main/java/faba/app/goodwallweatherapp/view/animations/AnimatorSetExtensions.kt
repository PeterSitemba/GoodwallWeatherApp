package faba.app.goodwallweatherapp.view.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

fun AnimatorSet.loadSlideDownAnimator(target: View, startDelay: Long = 0L) {
    this.startDelay = startDelay
    playTogether(fadeIn(target, 500), slideDown(target))
}


private fun slideDown(
    target: View,
    duration: Long = 1000L,
    interpolator: Interpolator = DecelerateInterpolator(),
    distance: Float = 100f
): ObjectAnimator {
    val translationYValueTo = target.translationY
    return ObjectAnimator.ofFloat(target, View.TRANSLATION_Y.name, -1f * distance, translationYValueTo).apply {
        this.duration = duration
        this.interpolator = interpolator
    }
}


private fun fadeIn(
    target: View,
    duration: Long = 1000L,
    interpolator: Interpolator = LinearInterpolator()
): ObjectAnimator {
    val alphaValueTo = target.alpha
    target.alpha = 0f

    return ObjectAnimator.ofFloat(target, View.ALPHA.name, target.alpha, alphaValueTo).apply {
        this.duration = duration
        this.interpolator = interpolator
    }
}

