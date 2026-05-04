package com.jorgealejojara.cuentasclaras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jorgealejojara.cuentasclaras.feature.add.ADD_ROUTE
import com.jorgealejojara.cuentasclaras.feature.budget.BUDGET_ROUTE
import com.jorgealejojara.cuentasclaras.feature.charts.CHARTS_ROUTE
import com.jorgealejojara.cuentasclaras.feature.home.HOME_ROUTE
import com.jorgealejojara.cuentasclaras.navigation.CuentasClarasNavHost
import com.jorgealejojara.cuentasclaras.ui.theme.CuentasClarasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CuentasClarasTheme {
                CuentasClarasMainScreen()
            }
        }
    }
}

private data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(HOME_ROUTE, "Inicio", Icons.Default.Home),
    BottomNavItem(CHARTS_ROUTE, "Gráficas", Icons.Default.BarChart),
    BottomNavItem(BUDGET_ROUTE, "Presupuestos", Icons.Default.Savings)
)

@Composable
private fun CuentasClarasMainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Hide bottom bar on Add screen
    val showBottomBar = currentDestination?.route != ADD_ROUTE

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        CuentasClarasNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}