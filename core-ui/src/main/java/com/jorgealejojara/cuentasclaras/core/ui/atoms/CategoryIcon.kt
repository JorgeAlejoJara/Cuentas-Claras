package com.jorgealejojara.cuentasclaras.core.ui.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CategoryIcon(
    iconName: String,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    iconSize: Dp = 20.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                color = color.copy(alpha = 0.15f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = mapIcon(iconName),
            contentDescription = iconName,
            tint = color,
            modifier = Modifier.size(iconSize)
        )
    }
}

fun mapIcon(name: String): ImageVector {
    return when (name) {
        "Restaurant" -> Icons.Default.Restaurant
        "DirectionsCar" -> Icons.Default.DirectionsCar
        "SportsEsports" -> Icons.Default.SportsEsports
        "LocalHospital" -> Icons.Default.LocalHospital
        "School" -> Icons.Default.School
        "Home" -> Icons.Default.Home
        "Checkroom" -> Icons.Default.Checkroom
        "MoreHoriz" -> Icons.Default.MoreHoriz
        "Work" -> Icons.Default.Work
        "Laptop" -> Icons.Default.Laptop
        "TrendingUp" -> Icons.Default.TrendingUp
        "AttachMoney" -> Icons.Default.AttachMoney
        "ShoppingBag" -> Icons.Default.ShoppingBag
        else -> Icons.Default.MoreHoriz
    }
}

