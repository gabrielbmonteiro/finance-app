package com.trilhacusto.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trilhacusto.ui.components.GlassConfirmDialog
import com.trilhacusto.ui.components.GlassTextField
import com.trilhacusto.ui.components.TransacaoCardItem
import com.trilhacusto.ui.theme.GreenPositive
import com.trilhacusto.ui.theme.PrimaryAccent
import com.trilhacusto.util.toCurrencyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onNavigateToPendencias: () -> Unit,
    onNavigateToExtrato: (Long?) -> Unit,
    onSincronizarPluggy: () -> Unit,
    onTransacaoClick: (String) -> Unit,
    onMudarMes: (Int) -> Unit,
    onSelecionarMesAno: (Int, Int) -> Unit,
    onAbrirSelecaoMesAno: () -> Unit,
    onFecharSelecaoMesAno: () -> Unit,
    onSalvarConfiguracao: (Int, Int) -> Unit,
    onFecharConfiguracao: () -> Unit,
    onAbrirConfiguracao: () -> Unit,
    onTogglePrivacyMode: () -> Unit,
    onNavigateToExport: () -> Unit,
    onNavigateToAccount: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onMudarMes(-1) }) {
                            Text("<", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(percent = 20))
                                .background(androidx.compose.ui.graphics.Brush.linearGradient(colors = listOf(com.trilhacusto.ui.theme.GlassBackground, com.trilhacusto.ui.theme.GlassBackground)))
                                .border(1.dp, com.trilhacusto.ui.theme.GlassBorder,
                                    RoundedCornerShape(percent = 20)
                                )
                                .clickable { onAbrirSelecaoMesAno() }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = uiState.mesAtual, 
                                    fontWeight = FontWeight.Bold, 
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Trocar Mês", tint = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                        IconButton(onClick = { onMudarMes(1) }) {
                            Text(">", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onTogglePrivacyMode) {
                        val icon = if (uiState.isPrivacyModeEnabled) Icons.Default.VisibilityOff else Icons.Default.Visibility
                        Icon(icon, contentDescription = "Alternar Visibilidade", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
                    label = { Text("Início") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Menu, contentDescription = "Extrato") },
                    label = { Text("Extrato") },
                    selected = false,
                    onClick = { onNavigateToExtrato(null) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Pendências") },
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
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                DashboardContent(
                    uiState = uiState,
                    onNavigateToExtrato = onNavigateToExtrato,
                    onTransacaoClick = onTransacaoClick,
                    onAbrirConfiguracao = onAbrirConfiguracao,
                    onSincronizarPluggy = onSincronizarPluggy,
                    onNavigateToExport = onNavigateToExport
                )
            }
        }

        if (uiState.showSyncLoading) {
            androidx.compose.ui.window.Dialog(onDismissRequest = { }) {
                ElevatedCard {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Sincronizando com a Pluggy...")
                    }
                }
            }
        } else if (uiState.syncStatus != null) {
            androidx.compose.ui.window.Dialog(onDismissRequest = { }) {
                com.trilhacusto.ui.components.GlassCard(
                    containerColor = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.6f)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        if (uiState.syncStatus == SyncStatus.SUCCESS) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GreenPositive, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Sincronizado com Sucesso!", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Clear, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Erro na Sincronização.", fontWeight = FontWeight.Bold)
                            if (uiState.error != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = uiState.error,
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }

        if (uiState.showFaturaConfigDialog) {
            var fechamento by remember { mutableStateOf(uiState.diaFechamento.toString()) }
            var vencimento by remember { mutableStateOf(uiState.diaVencimento.toString()) }

            GlassConfirmDialog(
                onDismissRequest = onFecharConfiguracao,
                onConfirm = {
                    val f = fechamento.toIntOrNull() ?: uiState.diaFechamento
                    val v = vencimento.toIntOrNull() ?: uiState.diaVencimento
                    onSalvarConfiguracao(f, v)
                },
                title = "Configurar Fatura",
                isDarker = true,
                confirmButtonText = "Salvar",
                dismissButtonText = "Cancelar",
                content = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        GlassTextField(
                            value = fechamento,
                            onValueChange = { fechamento = it.filter { char -> char.isDigit() } },
                            label = { Text("Dia de Fechamento") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GlassTextField(
                            value = vencimento,
                            onValueChange = { vencimento = it.filter { char -> char.isDigit() } },
                            label = { Text("Dia de Vencimento") },
                            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            )
        }
        
        if (uiState.showMesAnoDialog) {
            val meses = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")
            var selectedAno by remember(uiState.anoSelecionado) { mutableStateOf(if (uiState.anoSelecionado > 0) uiState.anoSelecionado else java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)) }
            
            GlassConfirmDialog(
                onDismissRequest = { onFecharSelecaoMesAno() },
                onConfirm = { onSelecionarMesAno(java.util.Calendar.getInstance().get(java.util.Calendar.MONTH), java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)) },
                title = "Escolher Mês e Ano",
                isDarker = true,
                confirmButtonText = "Mês Atual",
                dismissButtonText = "Fechar",
                content = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        ) {
                            IconButton(onClick = { selectedAno-- }) {
                                Text("<", color = MaterialTheme.colorScheme.onSurface)
                            }
                            Text(selectedAno.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(horizontal = 16.dp))
                            IconButton(onClick = { selectedAno++ }) {
                                Text(">", color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                        
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            itemsIndexed(meses) { index, mes ->
                                val isSelected = index == uiState.mesSelecionado && selectedAno == uiState.anoSelecionado
                                val bgColor = if (isSelected) PrimaryAccent else androidx.compose.ui.graphics.Color.White.copy(alpha = 0.1f)
                                val textColor = if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                
                                Box(
                                    modifier = Modifier
                                        .background(color = bgColor, shape = RoundedCornerShape(12.dp))
                                        .clickable { onSelecionarMesAno(index, selectedAno) }
                                        .padding(vertical = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = mes, color = textColor, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun DashboardContent(
    uiState: DashboardUiState,
    onNavigateToExtrato: (Long?) -> Unit,
    onTransacaoClick: (String) -> Unit,
    onAbrirConfiguracao: () -> Unit,
    onSincronizarPluggy: () -> Unit,
    onNavigateToExport: () -> Unit
) {
    var tooltipGasto by remember { mutableStateOf<GastoPessoa?>(null) }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            com.trilhacusto.ui.components.GlassCard(
                modifier = Modifier.fillMaxWidth().clickable { onNavigateToExtrato(null) }
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(
                                "Fatura Atual",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onAbrirConfiguracao() }) {
                                Text(
                                    uiState.periodoFatura,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.Edit, contentDescription = "Configurar", tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                            }
                        }
                        val rotation by androidx.compose.animation.core.animateFloatAsState(
                            targetValue = if (uiState.showSyncLoading) 360f else 0f,
                            animationSpec = if (uiState.showSyncLoading) {
                                androidx.compose.animation.core.infiniteRepeatable(
                                    animation = androidx.compose.animation.core.tween(1000, easing = androidx.compose.animation.core.LinearEasing),
                                    repeatMode = androidx.compose.animation.core.RepeatMode.Restart
                                )
                            } else {
                                androidx.compose.animation.core.tween(300)
                            }
                        )
                        
                    Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = onSincronizarPluggy, enabled = !uiState.showSyncLoading) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Sincronizar",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.rotate(rotation)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                        Column {
                            Text(
                                uiState.faturaTotal.toCurrencyString(uiState.isPrivacyModeEnabled),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            if (uiState.lastSyncTime != null) {
                                val sdf = java.text.SimpleDateFormat("dd/MM/yy', 'HH:mm", java.util.Locale("pt", "BR"))
                                val timeStr = sdf.format(java.util.Date(uiState.lastSyncTime))
                                Text(
                                    text = "Última atualização $timeStr",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                        IconButton(onClick = onNavigateToExport) {
                            Icon(
                                Icons.Default.Download,
                                contentDescription = "Exportar",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }

        item {
            if (uiState.gastosPessoas.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(androidx.compose.foundation.rememberScrollState()).height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    uiState.gastosPessoas.forEach { gasto ->
                        val parsedColor = try {
                            androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(gasto.pessoa.corHex))
                        } catch (e: Exception) {
                            com.trilhacusto.ui.theme.PrimaryAccent
                        }
                        
                        com.trilhacusto.ui.components.GlassCard(
                            modifier = Modifier.wrapContentWidth().fillMaxHeight().clickable { onNavigateToExtrato(gasto.pessoa.id) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(gasto.pessoa.nome, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                Text(
                                    gasto.valor.toCurrencyString(uiState.isPrivacyModeEnabled),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = parsedColor,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Proporção de Gastos",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                
                if (uiState.gastosPessoas.isEmpty() || uiState.gastosPessoas.all { it.proporcao <= 0f }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(percent = 50))
                            .background(androidx.compose.ui.graphics.Color.Black)
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(percent = 50))
                    ) {
                        uiState.gastosPessoas.forEach { gasto ->
                            val prop = gasto.proporcao.coerceIn(0f, 1f)
                            if (prop > 0) {
                                val parsedColor = try {
                                    androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(gasto.pessoa.corHex))
                                } catch (e: Exception) {
                                    com.trilhacusto.ui.theme.PrimaryAccent
                                }
                                val brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                    colors = listOf(parsedColor.copy(alpha = 0.6f), parsedColor)
                                )
                                Box(
                                    modifier = Modifier
                                        .weight(prop)
                                        .fillMaxHeight()
                                        .background(brush)
                                        .clickable {
                                            tooltipGasto = if (tooltipGasto == gasto) null else gasto
                                        }
                                )
                            }
                        }
                    }
                    
                    androidx.compose.animation.AnimatedVisibility(
                        visible = tooltipGasto != null,
                        enter = androidx.compose.animation.fadeIn(),
                        exit = androidx.compose.animation.fadeOut()
                    ) {
                        tooltipGasto?.let { selectedGasto ->
                            val pColor = when(selectedGasto.pessoa.nome.lowercase()) {
                                "gabriel" -> com.trilhacusto.ui.theme.ColorGabriel
                                "pai" -> com.trilhacusto.ui.theme.ColorPai
                                "mãe", "mae" -> com.trilhacusto.ui.theme.ColorMae
                                else -> com.trilhacusto.ui.theme.PrimaryAccent
                            }
                            val percent = (selectedGasto.proporcao * 100).toInt()
                            com.trilhacusto.ui.components.GlassCard(
                                modifier = Modifier.padding(top = 12.dp).wrapContentWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(modifier = Modifier.size(10.dp).clip(androidx.compose.foundation.shape.CircleShape).background(pColor))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        "${selectedGasto.pessoa.nome}: $percent%",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }



        
        if (uiState.ultimasPendencias.isNotEmpty()) {
            item {
                Text(
                    text = "Últimas Pendências",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
            }
            items(uiState.ultimasPendencias) { pendencia ->
                TransacaoCardItem(
                    transacaoCompleta = pendencia,
                    onClick = { onTransacaoClick(pendencia.transacao.id) },
                    isPrivacyModeEnabled = uiState.isPrivacyModeEnabled
                )
            }
        }
        
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
