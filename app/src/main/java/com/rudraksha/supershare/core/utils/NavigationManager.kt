package com.rudraksha.supershare.core.utils

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.rudraksha.supershare.core.domain.model.Screen
import kotlinx.serialization.Serializable

class NavigationManager(private val navController: NavHostController) {

    fun navigate(screen: Screen, popUp: Boolean = false) {
        if (popUp) {
            navController.popBackStack()
        }
        navController.navigate(screen.route)
    }

    fun navigateWithSerializable(screen: Screen.SerializableRoute<Serializable>, data: Serializable) {
        navController.navigate(screen.buildRoute(data))
    }

    fun popBackStack() {
        navController.popBackStack()
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun currentRoute(): String? {
        return navController.currentBackStackEntry?.destination?.route
    }

    fun getSerializableArgument(
        screen: Screen.SerializableRoute<Serializable>,
        backStackEntry: NavBackStackEntry,
        argKey: String
    ): Serializable? {
        val encoded = backStackEntry.arguments?.getString(argKey)
        return  if (encoded != null) {
            screen.parseArgument(encoded)
        } else null
    }
}
