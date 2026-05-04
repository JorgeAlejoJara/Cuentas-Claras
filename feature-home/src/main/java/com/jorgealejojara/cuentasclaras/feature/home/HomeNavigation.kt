package com.jorgealejojara.cuentasclaras.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val HOME_ROUTE = "home"

fun NavGraphBuilder.homeScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToTransactions: () -> Unit
) {
    composable(route = HOME_ROUTE) {
        HomeScreen(
            onNavigateToAdd = onNavigateToAdd,
            onNavigateToTransactions = onNavigateToTransactions
        )
    }
}

