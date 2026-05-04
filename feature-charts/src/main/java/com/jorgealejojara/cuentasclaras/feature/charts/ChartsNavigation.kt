package com.jorgealejojara.cuentasclaras.feature.charts

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val CHARTS_ROUTE = "charts"

fun NavGraphBuilder.chartsScreen() {
    composable(route = CHARTS_ROUTE) {
        ChartsScreen()
    }
}

