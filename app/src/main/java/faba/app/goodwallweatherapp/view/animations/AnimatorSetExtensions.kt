package faba.app.goodwallweatherapp.view.animations

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator

//region Composite Animations
fun AnimatorSet.loadSlideDownAnimator(target: View, startDelay: Long = 0L) {
    this.startDelay = startDelay
    playTogether(fadeIn(target, 500), slideDown(target))
}

fun AnimatorSet.loadSlideUpAnimator(target: View, startDelay: Long = 0L) {
    this.startDelay = startDelay
    playTogether(fadeIn(target), slideUp(target))
}

fun AnimatorSet.loadSlideLeftAnimator(target: View, startDelay: Long = 0L) {
    this.startDelay = startDelay
    playTogether(fadeIn(target), slideLeft(target))
}

fun AnimatorSet.loadSlideLeftSlowAnimator(target: View, startDelay: Long = 0L) {
    this.startDelay = startDelay
    play(slideLeft(target, 4000L, AccelerateDecelerateInterpolator()))
}

fun AnimatorSet.loadRotateClockwiseAnimator(target: View) =
    loadRotateClockwiseAnimator(target, 0L)

fun AnimatorSet.loadRotateClockwiseAnimator(target: View, startDelay: Long) {
    this.startDelay = startDelay
    play(rotate(target, 300L, AccelerateDecelerateInterpolator(), 180F))
}

fun AnimatorSet.loadRotateAntiClockwiseAnimator(target: View) =
    loadRotateAntiClockwiseAnimator(target, 0L)

fun AnimatorSet.loadRotateAntiClockwiseAnimator(target: View, startDelay: Long) {
    this.startDelay = startDelay
    play(rotate(target, 300L, AccelerateDecelerateInterpolator(), -180F))
}
//endregion


//region Basic Animations
private fun rotate(
    target: View,
    duration: Long,
    interpolator: Interpolator = DecelerateInterpolator(),
    degrees: Float
): ObjectAnimator {
    val translationYValueTo = target.rotation
    return ObjectAnimator.ofFloat(target, View.ROTATION.name, translationYValueTo, translationYValueTo + degrees).apply {
        this.duration = duration
        this.interpolator = interpolator
    }
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

private fun slideUp(
    target: View,
    duration: Long = 1000L,
    interpolator: Interpolator = DecelerateInterpolator(),
    distance: Float = 100f
): ObjectAnimator {
    val translationYValueTo = target.translationY
    return ObjectAnimator.ofFloat(target, View.TRANSLATION_Y.name, distance, translationYValueTo).apply {
        this.duration = duration
        this.interpolator = interpolator
    }
}

private fun slideLeft(
    target: View,
    duration: Long = 1000L,
    interpolator: Interpolator = DecelerateInterpolator(),
    distance: Float = 100f
): ObjectAnimator {
    val translationXValueTo = target.translationX
    return ObjectAnimator.ofFloat(target, View.TRANSLATION_X.name, distance, translationXValueTo).apply {
        this.duration = duration
        this.interpolator = interpolator
    }
}

private fun slideRight(
    target: View,
    duration: Long = 1000L,
    interpolator: Interpolator = DecelerateInterpolator(),
    distance: Float = 100f
): ObjectAnimator {
    val translationXValueTo = target.translationX
    return ObjectAnimator.ofFloat(target, View.TRANSLATION_X.name, -1 * distance, translationXValueTo).apply {
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

private fun fadeOut(target: View, duration: Long = 1000L, interpolator: Interpolator = LinearInterpolator()) =
    ObjectAnimator.ofFloat(target, View.ALPHA.name, target.alpha, 0f).apply {
        this.duration = duration
        this.interpolator = interpolator
    }
//endregion