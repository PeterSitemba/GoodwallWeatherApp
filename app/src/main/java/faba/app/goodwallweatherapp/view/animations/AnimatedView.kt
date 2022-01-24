package faba.app.goodwallweatherapp.view.animations

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

interface AnimatedView {

    fun getIntroAnimationViews(delay: Long = 0L): Array<out Pair<View, Long>>

    fun getChildViews(vararg nestedViewGroups: Int): Array<out View> {
        var target = this as ViewGroup

        nestedViewGroups.forEach { target = target.findViewById(it) }

        return target.children.toList().toTypedArray()
    }

    fun Array<out View>.generateCascadingViews(delay: Long = 0L, wait: Long = 100L): Array<out Pair<View, Long>> {
        var waitAccumulator = delay
        val cascadingViews = mutableListOf<Pair<View, Long>>()
        forEach {
            if (it is AnimatedView) cascadingViews.addAll(it.getIntroAnimationViews(delay))
            else cascadingViews.add(Pair(it, waitAccumulator))
            waitAccumulator = cascadingViews.last().second + wait
        }
        return cascadingViews.toTypedArray()
    }
}