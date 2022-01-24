package faba.app.goodwallweatherapp.view.animations

import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import faba.app.goodwallweatherapp.R

data class NavAnimations(
    @AnimRes @AnimatorRes val enterAnim: Int = View.NO_ID,
    @AnimRes @AnimatorRes val exitAnim: Int = View.NO_ID,
    @AnimRes @AnimatorRes val popEnterAnim: Int = View.NO_ID,
    @AnimRes @AnimatorRes val popExitAnim: Int = View.NO_ID
) {

    companion object {
        val DEFAULT: NavAnimations =
            NavAnimations(
                enterAnim = R.anim.slide_in_right,
                exitAnim = R.anim.slide_out_left,
                popExitAnim = R.anim.slide_out_right
            )
    }
}