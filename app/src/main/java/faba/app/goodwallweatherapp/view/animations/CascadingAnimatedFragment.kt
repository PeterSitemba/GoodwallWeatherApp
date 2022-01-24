package faba.app.goodwallweatherapp.view.animations

import android.animation.AnimatorSet
import android.view.View

abstract class CascadingAnimatedFragment : AnimatedFragment() {

    //region Nested Types
    open class CascadingFragmentAnimatorSet(var animationSet: AnimatorSet) : FragmentAnimatorSet {

        //region Properties
        internal val animationConfig: MutableList<Pair<View, Long>> = mutableListOf()
        private var hasAnimated: Boolean = false
        //endregion


        //region Public API
        override fun start(rootDelay: Long) {
            if (!hasAnimated) {
                hasAnimated = true

                val animationSet = AnimatorSet()

                animationConfig.forEach {
                    if (it.first is AnimatedView) {
                        val previousConfig = animationConfig.indexOf(it) - 1
                        val delay = animationConfig.getOrNull(previousConfig)?.second ?: 0L + it.second
                        val getChildViews = (it.first as AnimatedView).getIntroAnimationViews(delay)
                        animationSet.playTogether(AnimatorSet().cascadeIn(*getChildViews))
                    } else {
                        animationSet.playTogether(AnimatorSet().cascadeIn(it))
                    }
                }

                this.animationSet = animationSet

                animationSet.startDelay = rootDelay
                animationSet.start()
            }
        }

        override fun reset() {
            hasAnimated = false
        }
    }
    //endregion


    //region Helper Functions
    private fun Array<out View>.generateCascadingViews(
        delay: Long = 0L,
        wait: Long = 100L
    ): Array<out Pair<View, Long>> {
        var waitAccumulator = delay
        return map {
            val pair = Pair(it, waitAccumulator)
            waitAccumulator += wait
            pair
        }.toTypedArray()
    }
    //endregion


    //region Public API
    override fun initialSetup() {
        introAnimator = CascadingFragmentAnimatorSet(AnimatorSet())
        outroAnimator = CascadingFragmentAnimatorSet(AnimatorSet())

        setIntroAnimator()
    }
    //endregion


    //region Extensions
    fun FragmentAnimatorSet.addViews(vararg targets: Pair<View, Long>) {
        targets.forEach { (this as CascadingFragmentAnimatorSet).animationConfig.add(it) }
    }

    fun FragmentAnimatorSet.addViews(vararg targets: View) {
        targets.generateCascadingViews().forEach { (this as CascadingFragmentAnimatorSet).animationConfig.add(it) }
    }
    //endregion
}


//region Extensions
fun AnimatorSet.cascadeIn(vararg targets: Pair<View, Long>): AnimatorSet =
    apply { playTogether(targets.map { AnimatorSet().apply { loadSlideDownAnimator(it.first, it.second) } }) }
//endregion