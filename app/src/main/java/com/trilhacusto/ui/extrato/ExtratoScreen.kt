package com.trilhacusto.ui.extrato

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trilhacusto.data.local.relation.TransacaoCompleta
import com.trilhacusto.ui.components.TransacaoCardItem
import com.trilhacusto.util.formatDateWithYearIfNeeded
import com.trilhacusto.util.toCurrencyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtratoScreen(
    uiState: ExtratoUiState,
    onBuscaChanged: (String) -> Unit,
    onFiltroResponsavelChanged: (Long?) -> Unit,
    onTransacaoClick: (String) -> Unit,
    onExcluirClick: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPendencias: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onLimparErro: () -> Unit,
    onTogglePrivacyMode: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var transacaoToDeleteId by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Extrato de Compras") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    actions = {
                        IconButton(onClick = onTogglePrivacyMode) {
                            val icon =
                                if (uiState.isPrivacyModeEnabled) Icons.Default.VisibilityOff else Icons.Default.Visibility
                            Icon(
                                icon,
                                contentDescription = "Alternar Visibilidade",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
                        label = { Text("Início") },
                        selected = false,
                        onClick = { onNavigateToHome() }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Menu, contentDescription = "Extrato") },
                        label = { Text("Extrato") },
                        selected = true,
                        onClick = { }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Pendências"
                            )
                        },
                        label = { Text("Pendências") },
                        selected = false,
                        onClick = { onNavigateToPendencias() }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Conta") },
                        label = { Text("Conta") },
                        selected = false,
                        onClick = { onNavigateToAccount() }
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                com.trilhacusto.ui.components.GlassTextField(
                    value = uiState.filtroBusca,
                    onValueChange = onBuscaChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    label = { Text("Buscar por descrição...") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        val isSelected = uiState.filtroResponsavel == null
                        val brush = if (isSelected) {
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    com.trilhacusto.ui.theme.PurplePrimary,
                                    com.trilhacusto.ui.theme.PurpleSecondary
                                )
                            )
                        } else {
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    com.trilhacusto.ui.theme.GlassBackground,
                                    com.trilhacusto.ui.theme.GlassBackground
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(percent = 50))
                                .background(brush)
                                .border(
                                    1.dp,
                                    com.trilhacusto.ui.theme.GlassBorder,
                                    androidx.compose.foundation.shape.RoundedCornerShape(percent = 50)
                                )
                                .clickable { onFiltroResponsavelChanged(null) }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Todas",
                                color = if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    items(uiState.pessoas) { pessoa ->
                        val isSelected = uiState.filtroResponsavel == pessoa.id
                        val brush = if (isSelected) {
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    com.trilhacusto.ui.theme.PurplePrimary,
                                    com.trilhacusto.ui.theme.PurpleSecondary
                                )
                            )
                        } else {
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    com.trilhacusto.ui.theme.GlassBackground,
                                    com.trilhacusto.ui.theme.GlassBackground
                                )
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(androidx.compose.foundation.shape.RoundedCornerShape(percent = 50))
                                .background(brush)
                                .border(
                                    1.dp,
                                    com.trilhacusto.ui.theme.GlassBorder,
                                    androidx.compose.foundation.shape.RoundedCornerShape(percent = 50)
                                )
                                .clickable { onFiltroResponsavelChanged(pessoa.id) }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                pessoa.nome,
                                color = if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (uiState.error != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = uiState.error, color = MaterialTheme.colorScheme.error)
                    }
                } else if (uiState.faturasAgrupadas.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Nenhuma transação encontrada.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    val expandedInvoices = remember { mutableStateMapOf<String, Boolean>() }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (uiState.faturasAgrupadas.size > 1) {
                            val allExpanded = uiState.faturasAgrupadas.all { grupo ->
                                val groupKey = "${grupo.mesAno.ano}_${grupo.mesAno.mes}"
                                expandedInvoices[groupKey] ?: false
                            }
                            item(key = "toggle_all_bar") {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${uiState.faturasAgrupadas.size} faturas encontradas",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = if (allExpanded) "Recolher todas" else "Expandir todas",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .clickable {
                                                val target = !allExpanded
                                                uiState.faturasAgrupadas.forEach { grupo ->
                                                    val groupKey = "${grupo.mesAno.ano}_${grupo.mesAno.mes}"
                                                    expandedInvoices[groupKey] = target
                                                }
                                            }
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        items(
                            items = uiState.faturasAgrupadas,
                            key = { "${it.mesAno.ano}_${it.mesAno.mes}" }
                        ) { grupo ->
                            val groupKey = "${grupo.mesAno.ano}_${grupo.mesAno.mes}"
                            val isExpanded = expandedInvoices[groupKey] ?: (uiState.faturasAgrupadas.firstOrNull() == grupo)

                            FaturaEnvelopeCard(
                                grupo = grupo,
                                isExpanded = isExpanded,
                                isPrivacyModeEnabled = uiState.isPrivacyModeEnabled,
                                filtroPessoaId = uiState.filtroResponsavel,
                                onToggleExpand = {
                                    expandedInvoices[groupKey] = !isExpanded
                                },
                                onTransacaoClick = onTransacaoClick,
                                onExcluirClick = { id ->
                                    transacaoToDeleteId = id
                                    showDeleteDialog = true
                                }
                            )
                        }

                        item { Spacer(modifier = Modifier.height(24.dp)) }
                    }
                }
            }
        }

        com.trilhacusto.ui.components.GlassErrorSnackbar(
            errorMessage = uiState.error,
            onDismiss = onLimparErro,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        )

        if (showDeleteDialog && transacaoToDeleteId != null) {
            com.trilhacusto.ui.components.GlassConfirmDialog(
                onDismissRequest = { showDeleteDialog = false },
                onConfirm = {
                    onExcluirClick(transacaoToDeleteId!!)
                    showDeleteDialog = false
                },
                title = "Remover da Fatura",
                text = "Esta compra sairá desta fatura e voltará para a lista de pendências para ser rateada novamente.",
                confirmButtonText = "Remover",
                dismissButtonText = "Cancelar",
                isDestructive = true
            )
        }
    }
}

@Composable
private fun FaturaEnvelopeCard(
    grupo: FaturaExtratoGroup,
    isExpanded: Boolean,
    isPrivacyModeEnabled: Boolean,
    filtroPessoaId: Long?,
    onToggleExpand: () -> Unit,
    onTransacaoClick: (String) -> Unit,
    onExcluirClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val chevronRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 280),
        label = "chevronRotation"
    )

    val envelopeShape = RoundedCornerShape(24.dp)

    // Fundo escuro/preto com gradiente sutil alinhado à paleta escura do app
    val containerBrush = if (isExpanded) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1E1338), // Topo com leve brilho roxo escuro
                Color(0xFF111222), // Superfície escura tipo obsidian
                Color(0xFF090A14)  // Base preta profunda
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF16172C), // Superfície escura refinada
                Color(0xFF0D0E1C)  // Base preta
            )
        )
    }

    val containerBorderColor = if (isExpanded) {
        com.trilhacusto.ui.theme.PurplePrimary.copy(alpha = 0.55f)
    } else {
        Color(0xFF282A45)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(envelopeShape)
            .background(containerBrush)
            .border(1.5.dp, containerBorderColor, envelopeShape)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ==========================================
            // CABEÇALHO PRINCIPAL DA FATURA (SEMPRE VISÍVEL)
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(20.dp)
            ) {
                // Top Row: Badge "FATURA DO CARTÃO" + Pill com Datas da Fatura "( 28 ago - 29 set )"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Badge destacando que este bloco é uma FATURA
                    Surface(
                        shape = RoundedCornerShape(percent = 50),
                        color = Color(0xFF281845),
                        border = BorderStroke(1.dp, com.trilhacusto.ui.theme.PurplePrimary.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                                contentDescription = null,
                                tint = Color(0xFFC084FC),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "FATURA DO CARTÃO",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    fontSize = 10.sp
                                ),
                                color = Color(0xFFC084FC)
                            )
                        }
                    }

                    // Datas da Fatura (ex: 28 ago - 29 set)
                    Surface(
                        shape = RoundedCornerShape(percent = 50),
                        color = Color(0xFF101C33),
                        border = BorderStroke(1.dp, Color(0xFF3B82F6).copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = Color(0xFF60A5FA),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${grupo.periodoExtrato}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                ),
                                color = Color(0xFF60A5FA)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Nome do Mês da Fatura em destaque
                Text(
                    text = grupo.nomeMes,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Row do Cabeçalho: Total e Botão de Expandir/Recolher
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = if (filtroPessoaId != null) "TOTAL DESTA PESSOA" else "TOTAL DA FATURA",
                            style = MaterialTheme.typography.labelSmall.copy(
                                letterSpacing = 0.8.sp,
                                fontSize = 10.sp
                            ),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = grupo.totalFatura.toCurrencyString(isPrivacyModeEnabled),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFC084FC)
                        )
                    }

                    // Botão interativo de ação (escuro e elegante)
                    Surface(
                        shape = RoundedCornerShape(percent = 50),
                        color = if (isExpanded) Color(0xFF261942) else Color(0xFF18192E),
                        border = BorderStroke(
                            1.dp,
                            if (isExpanded) com.trilhacusto.ui.theme.PurplePrimary.copy(alpha = 0.55f)
                            else Color(0xFF333658)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isExpanded) "Recolher" else "Ver compras",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isExpanded) Color(0xFFE9D5FF) else Color(0xFFCBD5E1)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(18.dp)
                                    .rotate(chevronRotation),
                                tint = if (isExpanded) Color(0xFFC084FC) else Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }

            // ==========================================
            // ITENS DA FATURA (COMPRAS / PENDÊNCIAS INTERNAS)
            // ==========================================
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(250)) + fadeOut(animationSpec = tween(200))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    HorizontalDivider(
                        color = Color.White.copy(alpha = 0.08f),
                        thickness = 1.dp
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Header das compras dentro da fatura
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "COMPRAS DESTA FATURA (${grupo.transacoes.size})",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp,
                                    fontSize = 11.sp
                                ),
                                color = Color(0xFFC084FC)
                            )
                        }

                        // Lista das despesas usando o card padrão do app (GlassCard)
                        grupo.transacoes.forEach { transacao ->
                            TransacaoCardItem(
                                transacaoCompleta = transacao,
                                onClick = { onTransacaoClick(transacao.transacao.id) },
                                onExcluir = { onExcluirClick(transacao.transacao.id) },
                                filtroPessoaId = filtroPessoaId,
                                isPrivacyModeEnabled = isPrivacyModeEnabled
                            )
                        }
                    }
                }
            }
        }
    }
}
