package faba.app.goodwallweatherapp.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions

fun NavController.navigateTo(@IdRes destId: Int, options: NavOptions?) {
    navigateTo(destId, null, options)
}

fun NavController.navigateTo(@IdRes destId: Int, animationsOverride: NavAnimations? = NavAnimations.DEFAULT) {
    navigateTo(destId, null, animationsOverride)
}

fun NavController.navigateTo(
    @IdRes destId: Int, args: Bundle?,
    animationsOverride: NavAnimations? = NavAnimations.DEFAULT
) {
    navigateTo(destId, args, getNavOptions(destId, animationsOverride))
}


fun NavController.navigateTo(@IdRes destId: Int, args: Bundle?, options: NavOptions?) {
    if (!popBackStack(destId, false)) {
        navigate(destId, args, options)
    }
}

fun NavController.navigateTo(directions: NavDirections, animationsOverride: NavAnimations? = NavAnimations.DEFAULT) {
    navigateTo(directions, getNavOptions(directions.actionId, animationsOverride))
}

fun NavController.navigateTo(directions: NavDirections, options: NavOptions?) {
    if (!popBackStack(directions.actionId, false)) {
        navigate(directions, options)
    }
}

private fun NavController.getNavOptions(@IdRes destId: Int, animations: NavAnimations?): NavOptions? {
    val currentNavOptions: NavOptions? = currentDestination?.getAction(destId)?.navOptions

    val newNavOptions = NavOptions.Builder()

    if (animations != null) {
        newNavOptions
            .setEnterAnim(animations.enterAnim)
            .setExitAnim(animations.exitAnim)
            .setPopEnterAnim(animations.popEnterAnim)
            .setPopExitAnim(animations.popExitAnim)
    } else if (currentNavOptions != null) {
        newNavOptions
            .setEnterAnim(currentNavOptions.enterAnim)
            .setExitAnim(currentNavOptions.exitAnim)
            .setPopEnterAnim(currentNavOptions.popEnterAnim)
            .setPopExitAnim(currentNavOptions.popExitAnim)
    }

    currentNavOptions?.let {
        newNavOptions.setPopUpTo(it.popUpTo, it.isPopUpToInclusive)
            .setLaunchSingleTop(it.shouldLaunchSingleTop())
    }

    return newNavOptions.build()
}