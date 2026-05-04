package com.jorgealejojara.cuentasclaras.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jorgealejojara.cuentasclaras.core.domain.model.Account
import com.jorgealejojara.cuentasclaras.core.domain.model.TransactionType
import com.jorgealejojara.cuentasclaras.core.ui.atoms.CategoryIcon
import com.jorgealejojara.cuentasclaras.core.ui.atoms.CcLoadingIndicator
import com.jorgealejojara.cuentasclaras.core.ui.molecules.EmptyState
import com.jorgealejojara.cuentasclaras.core.ui.molecules.ErrorState
import com.jorgealejojara.cuentasclaras.core.ui.theme.CuentasClarasThemeExt
import java.text.NumberFormat
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun HomeScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is HomeUiState.Loading -> {
            CcLoadingIndicator(modifier = modifier.fillMaxSize())
        }

        is HomeUiState.Error -> {
            ErrorState(
                message = state.message,
                modifier = modifier.fillMaxSize().padding(32.dp)
            )
        }

        is HomeUiState.Success -> {
            HomeContent(
                state = state,
                onToggleMasked = viewModel::toggleMasked,
                onPeriodChange = viewModel::setPeriod,
                onAccountClick = viewModel::toggleAccount,
                onNavigateToAdd = onNavigateToAdd,
                onNavigateToTransactions = onNavigateToTransactions,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun HomeContent(
    state: HomeUiState.Success,
    onToggleMasked: () -> Unit,
    onPeriodChange: (Period) -> Unit,
    onAccountClick: (Long) -> Unit,
    onNavigateToAdd: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    modifier: Modifier = Modifier
) {
    val monthName = Month.of(state.selectedMonth)
        .getDisplayName(TextStyle.FULL, Locale("es"))
        .replaceFirstChar { it.uppercase() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ── Top Bar ──
        HomeTopBar(
            masked = state.masked,
            onToggleMasked = onToggleMasked
        )

        // ── Balance header ──
        BalanceHeader(
            totalBalance = state.totalBalance,
            totalIncome = state.totalIncome,
            totalExpense = state.totalExpense,
            monthName = monthName,
            masked = state.masked
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── Account carousel ──
        AccountCarousel(
            accounts = state.accounts,
            activeAccountId = state.activeAccountId,
            masked = state.masked,
            onAccountClick = onAccountClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── Period filter ──
        SegmentedControl(
            options = Period.entries,
            selected = state.period,
            label = { it.label },
            onSelect = onPeriodChange,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ── Mini chart card ──
        MiniChartCard(
            balance = state.totalIncome - state.totalExpense,
            masked = state.masked,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ── Transactions grouped ──
        TransactionsSection(
            transactions = state.recentTransactions,
            masked = state.masked,
            onNavigateToAdd = onNavigateToAdd,
            onNavigateToTransactions = onNavigateToTransactions
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// ── Top Bar ──

@Composable
private fun HomeTopBar(
    masked: Boolean,
    onToggleMasked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "JA",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "Inicio",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = onToggleMasked) {
            Icon(
                imageVector = if (masked) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                contentDescription = if (masked) "Mostrar" else "Ocultar",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notificaciones",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ── Balance Header ──

@Composable
private fun BalanceHeader(
    totalBalance: Double,
    totalIncome: Double,
    totalExpense: Double,
    monthName: String,
    masked: Boolean
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Text(
            text = "PATRIMONIO TOTAL · ${monthName.lowercase()}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (masked) "••••••••••" else formatCOP(totalBalance),
            style = MaterialTheme.typography.displaySmall.copy(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-1).sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Income
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowUp,
                    contentDescription = null,
                    tint = CuentasClarasThemeExt.colors.income,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (masked) "••••" else "+${formatCOP(totalIncome)}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium
                    ),
                    color = CuentasClarasThemeExt.colors.income
                )
            }

            // Expense
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null,
                    tint = CuentasClarasThemeExt.colors.expense,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (masked) "••••" else "-${formatCOP(totalExpense)}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium
                    ),
                    color = CuentasClarasThemeExt.colors.expense
                )
            }
        }
    }
}

// ── Account Carousel ──

private val accountColors = listOf(
    Color(0xFFA8E6CF), // Verde menta
    Color(0xFFFFD3B6), // Naranja pastel
    Color(0xFFD4A5FF), // Lila
    Color(0xFFFFF3B0), // Amarillo pastel
)

@Composable
private fun AccountCarousel(
    accounts: List<Account>,
    activeAccountId: Long?,
    masked: Boolean,
    onAccountClick: (Long) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(accounts, key = { it.id }) { account ->
            val index = accounts.indexOf(account)
            val color = accountColors[index % accountColors.size]
            val isActive = activeAccountId == account.id

            AccountCard(
                account = account,
                color = color,
                isActive = isActive,
                masked = masked,
                onClick = { onAccountClick(account.id) }
            )
        }

        // Add account card
        item {
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar cuenta",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun AccountCard(
    account: Account,
    color: Color,
    isActive: Boolean,
    masked: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = color,
        shadowElevation = if (isActive) 4.dp else 1.dp,
        modifier = Modifier.width(220.dp)
    ) {
        Box {
            // Decorative circle
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 0.dp)
                    .size(80.dp)
                    .background(
                        Color.White.copy(alpha = 0.18f),
                        CircleShape
                    )
            )

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (masked) "••••••••" else formatCOP(account.balance),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Color(0xFF1A1A1A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cuenta",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF1A1A1A).copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ── Segmented Control ──

@Composable
private fun <T> SegmentedControl(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .then(
                        if (isSelected) Modifier.background(MaterialTheme.colorScheme.surface)
                        else Modifier
                    )
                    .clickable { onSelect(option) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label(option),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ── Mini Chart Card ──

@Composable
private fun MiniChartCard(
    balance: Double,
    masked: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "FLUJO DE LOS ÚLTIMOS DÍAS",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (masked) "••••••" else formatCOP(balance),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clickable { }
                ) {
                    Text(
                        text = "Ver más ›",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chart placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📈 Gráfico próximamente",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ── Transactions Section ──

@Composable
private fun TransactionsSection(
    transactions: List<TransactionWithCategory>,
    masked: Boolean,
    onNavigateToAdd: () -> Unit,
    onNavigateToTransactions: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Movimientos",
                style = MaterialTheme.typography.titleLarge
            )
            Surface(
                onClick = onNavigateToTransactions,
                shape = RoundedCornerShape(20.dp),
                color = Color.Transparent
            ) {
                Text(
                    text = "Ver todos",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (transactions.isEmpty()) {
            EmptyState(
                title = "Aún no hay movimientos",
                message = "Registra tu primer ingreso o gasto y empieza a ver claro el panorama.",
                actionLabel = "Agregar transacción",
                onAction = onNavigateToAdd,
                modifier = Modifier.padding(vertical = 24.dp)
            )
        } else {
            // Group transactions by relative date
            val grouped = transactions.groupBy { formatRelativeDate(it.transaction.date) }

            grouped.entries.take(3).forEach { (dateLabel, txs) ->
                Text(
                    text = dateLabel.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = CardDefaults.outlinedCardBorder(),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                        txs.take(5).forEachIndexed { index, twc ->
                            TransactionRow(
                                twc = twc,
                                masked = masked
                            )
                            if (index < txs.size - 1 && index < 4) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun TransactionRow(
    twc: TransactionWithCategory,
    masked: Boolean
) {
    val tx = twc.transaction
    val cat = twc.category
    val isIncome = tx.type == TransactionType.INCOME

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Category icon
        CategoryIcon(
            iconName = cat.icon,
            color = Color(cat.color),
            size = 40.dp,
            iconSize = 20.dp
        )

        // Title + subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = tx.description,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${cat.name} · ${formatTime(tx.date)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Amount
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = if (masked) "••••••" else
                    "${if (isIncome) "+" else ""}${formatCOP(tx.amount)}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                ),
                color = if (isIncome) CuentasClarasThemeExt.colors.income
                else CuentasClarasThemeExt.colors.expense
            )
        }
    }
}

// ── Formatters ──

private val copFormatter = NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply {
    maximumFractionDigits = 0
}

private fun formatCOP(amount: Double): String {
    return copFormatter.format(kotlin.math.abs(amount))
}

private fun formatRelativeDate(date: LocalDate): String {
    val today = LocalDate.now()
    val days = ChronoUnit.DAYS.between(date, today)
    return when {
        days == 0L -> "Hoy"
        days == 1L -> "Ayer"
        days < 7L -> "Hace $days días"
        else -> {
            val day = date.dayOfMonth
            val month = date.month.getDisplayName(TextStyle.SHORT, Locale("es"))
                .replaceFirstChar { it.uppercase() }
            "$day $month"
        }
    }
}

private fun formatTime(date: LocalDate): String {
    val day = date.dayOfMonth
    val month = date.month.getDisplayName(TextStyle.SHORT, Locale("es"))
        .replaceFirstChar { it.uppercase() }
    return "$day $month"
}
