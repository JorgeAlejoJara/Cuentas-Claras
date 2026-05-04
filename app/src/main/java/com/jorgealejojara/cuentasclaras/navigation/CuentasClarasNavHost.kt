package com.jorgealejojara.cuentasclaras.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jorgealejojara.cuentasclaras.feature.add.ADD_ROUTE
import com.jorgealejojara.cuentasclaras.feature.add.addScreen
import com.jorgealejojara.cuentasclaras.feature.budget.budgetScreen
import com.jorgealejojara.cuentasclaras.feature.charts.chartsScreen
import com.jorgealejojara.cuentasclaras.feature.home.HOME_ROUTE
import com.jorgealejojara.cuentasclaras.feature.home.homeScreen

const val PROFILE_ROUTE = "profile"

@Composable
fun CuentasClarasNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
        modifier = modifier
    ) {
        homeScreen(
            onNavigateToAdd = { navController.navigate(ADD_ROUTE) },
            onNavigateToTransactions = { /* TODO: navigate to transactions list */ }
        )

        addScreen(
            onNavigateBack = { navController.popBackStack() }
        )

        chartsScreen()

        budgetScreen()

        composable(route = PROFILE_ROUTE) {
            ProfilePlaceholder()
        }
    }
}

@Composable
private fun ProfilePlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

