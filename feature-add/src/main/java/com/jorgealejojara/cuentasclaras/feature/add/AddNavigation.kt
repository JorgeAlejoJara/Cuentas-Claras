package com.jorgealejojara.cuentasclaras.feature.add

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val ADD_ROUTE = "add"

fun NavGraphBuilder.addScreen(
    onNavigateBack: () -> Unit
) {
    composable(route = ADD_ROUTE) {
        AddScreen(onNavigateBack = onNavigateBack)
    }
}

fun NavController.navigateToAdd() {
    navigate(ADD_ROUTE)
}

