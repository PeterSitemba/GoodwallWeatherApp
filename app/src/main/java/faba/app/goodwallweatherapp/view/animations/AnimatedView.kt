package faba.app.goodwallweatherapp.view.animations

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

interface AnimatedView {

    fun getIntroAnimationViews(delay: Long = 0L): Array<out Pair<View, Long>>

}