package com.ucb.ucbtest.navigation

sealed class Screen(val route: String) {
    object PlanScreen: Screen("plan")
    object MapScreen: Screen("map")
}