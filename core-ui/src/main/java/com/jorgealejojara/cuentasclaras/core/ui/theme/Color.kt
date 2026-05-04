package com.jorgealejojara.cuentasclaras.core.ui.theme

import androidx.compose.ui.graphics.Color

// ── Primary ──
val PrimaryLight = Color(0xFF00875A)
val OnPrimaryLight = Color(0xFFFFFFFF)
val PrimaryContainerLight = Color(0xFF002114)
val OnPrimaryContainerLight = Color(0xFFB6F0D6)

val PrimaryDark = Color(0xFF6FDDB0)
val OnPrimaryDark = Color(0xFF00382A)
val PrimaryContainerDark = Color(0xFF00513A)
val OnPrimaryContainerDark = Color(0xFFB6F0D6)

// ── Secondary ──
val SecondaryLight = Color(0xFF4D6358)
val SecondaryContainerLight = Color(0xFFD0E8DA)

val SecondaryDark = Color(0xFFB4CCBE)
val SecondaryContainerDark = Color(0xFF354B41)

// ── Tertiary ──
val TertiaryLight = Color(0xFF3D6473)
val TertiaryContainerLight = Color(0xFFC0E8FA)

val TertiaryDark = Color(0xFFA4CDDF)
val TertiaryContainerDark = Color(0xFF234C5B)

// ── Background / Surface ──
val BackgroundLight = Color(0xFFF7FBF8)
val SurfaceLight = Color(0xFFFFFFFF)
val SurfaceVariant1Light = Color(0xFFF0F5F1)
val SurfaceVariant2Light = Color(0xFFE7EFE9)
val SurfaceVariant3Light = Color(0xFFDCE7DE)
val OnSurfaceLight = Color(0xFF181D1A)
val OnSurfaceVariantLight = Color(0xFF404943)
val OutlineLight = Color(0xFF707974)
val OutlineVariantLight = Color(0xFFBFC9C2)

val BackgroundDark = Color(0xFF0F1411)
val SurfaceDark = Color(0xFF161B17)
val SurfaceVariant1Dark = Color(0xFF1C231F)
val SurfaceVariant2Dark = Color(0xFF222A25)
val SurfaceVariant3Dark = Color(0xFF2A332D)
val OnSurfaceDark = Color(0xFFDEE5E0)
val OnSurfaceVariantDark = Color(0xFFBFC9C2)
val OutlineDark = Color(0xFF8A938D)
val OutlineVariantDark = Color(0xFF404943)

// ── Error ──
val ErrorLight = Color(0xFFB3261E)
val OnErrorLight = Color(0xFFFFFFFF)
val ErrorContainerLight = Color(0xFFFFDAD6)
val OnErrorContainerLight = Color(0xFF410002)

val ErrorDark = Color(0xFFFFB4AB)
val OnErrorDark = Color(0xFF93000A)
val ErrorContainerDark = Color(0xFF93000A)
val OnErrorContainerDark = Color(0xFFFFDAD6)

// ── Semantic (finanzas) ──
val IncomeLight = Color(0xFF00875A)
val ExpenseLight = Color(0xFFB3261E)
val SavingsLight = Color(0xFF3D6473)
val WarningLight = Color(0xFFB86E00)

val IncomeDark = Color(0xFF6FDDB0)
val ExpenseDark = Color(0xFFFFB4AB)
val SavingsDark = Color(0xFFA4CDDF)
val WarningDark = Color(0xFFFFB873)

// ── Category palette (para chips y charts) ──
object CategoryColors {
    val Food = Color(0xFFE07A5F)
    val Transport = Color(0xFF5E81AC)
    val Home = Color(0xFF8B5CF6)
    val Shopping = Color(0xFFD08770)
    val Health = Color(0xFFE76F8E)
    val Entertainment = Color(0xFFF2A65A)
    val Education = Color(0xFF4FB286)
    val Other = Color(0xFF9AA5A0)

    val all = listOf(Food, Transport, Home, Shopping, Health, Entertainment, Education, Other)
}

