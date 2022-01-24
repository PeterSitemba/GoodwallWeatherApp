package faba.app.goodwallweatherapp.view.animations

import android.animation.AnimatorSet
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

abstract class AnimatedFragment : Fragment() {

    //region Nested Types
    open class AnimatedFragmentAnimatorSet(var animationSet: AnimatorSet) : FragmentAnimatorSet {

        //region Properties
        private var hasAnimated: Boolean = false
        //endregion


        //region Public API
        override fun start(rootDelay: Long) {
            if (!hasAnimated) {
                hasAnimated = true
                animationSet.startDelay = rootDelay
                animationSet.start()
            }
        }

        override fun reset() {
            hasAnimated = false
        }
        //endregion
    }
    //endregion


    //region Properties
    lateinit var introAnimator: FragmentAnimatorSet

    lateinit var outroAnimator: FragmentAnimatorSet
    //endregion


    //region Android Lifecycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialSetup()
    }
    //endregion


    //region Helper Functions
    open fun initialSetup() {
        introAnimator = AnimatedFragmentAnimatorSet(AnimatorSet())
        outroAnimator = AnimatedFragmentAnimatorSet(AnimatorSet())

        setIntroAnimator()
    }

    protected abstract fun setIntroAnimator()
    //endregion


    //region Extensions
    protected fun AnimatorSet.toFragmentAnimatorSet(): FragmentAnimatorSet = AnimatedFragmentAnimatorSet(this)

    protected infix fun View.after(milliseconds: Long): Pair<View, Long> = Pair(this, milliseconds)

    protected infix fun AnimatorSet.after(milliseconds: Long): AnimatorSet =
        this.apply { startDelay = milliseconds }

    protected val Int.milliseconds: Long
        get() = this.toLong()

    protected val Float.seconds: Long
        get() = (this * 1000f).toLong()
    //endregion
}

