package com.jorgealejojara.cuentasclaras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Adjust
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jorgealejojara.cuentasclaras.feature.add.ADD_ROUTE
import com.jorgealejojara.cuentasclaras.feature.budget.BUDGET_ROUTE
import com.jorgealejojara.cuentasclaras.feature.charts.CHARTS_ROUTE
import com.jorgealejojara.cuentasclaras.feature.home.HOME_ROUTE
import com.jorgealejojara.cuentasclaras.navigation.CuentasClarasNavHost
import com.jorgealejojara.cuentasclaras.navigation.PROFILE_ROUTE
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

private sealed class BottomNavItem(
    val route: String,
    val label: String
) {
    class Tab(
        route: String,
        label: String,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector
    ) : BottomNavItem(route, label)

    class Fab(
        route: String
    ) : BottomNavItem(route, "")
}

private val bottomNavItems = listOf(
    BottomNavItem.Tab(HOME_ROUTE, "Inicio", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem.Tab(CHARTS_ROUTE, "Gráficas", Icons.Filled.BarChart, Icons.Outlined.BarChart),
    BottomNavItem.Fab(ADD_ROUTE),
    BottomNavItem.Tab(BUDGET_ROUTE, "Presupuestos", Icons.Filled.Adjust, Icons.Outlined.Adjust),
    BottomNavItem.Tab(PROFILE_ROUTE, "Perfil", Icons.Filled.Person, Icons.Outlined.Person),
)

@Composable
private fun CuentasClarasMainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route != ADD_ROUTE

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                CuentasClarasBottomBar(
                    items = bottomNavItems,
                    isSelected = { route ->
                        currentDestination?.hierarchy?.any { it.route == route } == true
                    },
                    onItemClick = { item ->
                        if (item is BottomNavItem.Fab) {
                            navController.navigate(item.route)
                        } else {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        CuentasClarasNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun CuentasClarasBottomBar(
    items: List<BottomNavItem>,
    isSelected: (String) -> Boolean,
    onItemClick: (BottomNavItem) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 0.dp
    ) {
        items.forEach { item ->
            when (item) {
                is BottomNavItem.Tab -> {
                    val selected = isSelected(item.route)
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selected,
                        onClick = { onItemClick(item) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                is BottomNavItem.Fab -> {
                    FabNavItem(onClick = { onItemClick(item) })
                }
            }
        }
    }
}

@Composable
private fun RowScope.FabNavItem(onClick: () -> Unit) {
    NavigationBarItem(
        icon = {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 2.dp,
                modifier = Modifier
                    .width(56.dp)
                    .height(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Agregar",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        label = { },
        selected = false,
        onClick = onClick,
        colors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent
        )
    )
}