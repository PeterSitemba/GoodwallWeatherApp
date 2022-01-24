package faba.app.goodwallweatherapp.view.animations

import android.view.View

interface AnimatedView {

    fun getIntroAnimationViews(delay: Long = 0L): Array<out Pair<View, Long>>

}