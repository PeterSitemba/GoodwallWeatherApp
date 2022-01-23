package faba.app.goodwallweatherapp.view.fragments

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import faba.app.goodwallweatherapp.MainActivity

inline val Fragment.mainActivity: MainActivity
    get() = activity as MainActivity

val Fragment.navController: NavController
    get() = mainActivity.navController