package com.jorgealejojara.cuentasclaras.feature.budget

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val BUDGET_ROUTE = "budget"

fun NavGraphBuilder.budgetScreen() {
    composable(route = BUDGET_ROUTE) {
        BudgetScreen()
    }
}

