package com.jorgealejojara.cuentasclaras.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.jorgealejojara.cuentasclaras.feature.add.ADD_ROUTE
import com.jorgealejojara.cuentasclaras.feature.add.addScreen
import com.jorgealejojara.cuentasclaras.feature.budget.budgetScreen
import com.jorgealejojara.cuentasclaras.feature.charts.chartsScreen
import com.jorgealejojara.cuentasclaras.feature.home.HOME_ROUTE
import com.jorgealejojara.cuentasclaras.feature.home.homeScreen

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
    }
}

