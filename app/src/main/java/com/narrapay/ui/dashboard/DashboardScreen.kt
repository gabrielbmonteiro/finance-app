package com.narrapay.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import com.narrapay.ui.components.TransacaoCardItem
import com.narrapay.ui.theme.GreenPositive
import com.narrapay.ui.theme.OrangePai
import com.narrapay.util.toCurrencyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    onNavigateToPendencias: () -> Unit,
    onNavigateToExtrato: () -> Unit,
    onSincronizarPluggy: () -> Unit,
    onTransacaoClick: (String) -> Unit,
    onMudarMes: (Int) -> Unit,
    onSelecionarMesAno: (Int, Int) -> Unit,
    onAbrirSelecaoMesAno: () -> Unit,
    onFecharSelecaoMesAno: () -> Unit,
    onSalvarConfiguracao: (Int, Int) -> Unit,
    onFecharConfiguracao: () -> Unit,
    onAbrirConfiguracao: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { onMudarMes(-1) }) {
                            Icon(androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Mês Anterior")
                        }
                        Surface(
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable { onAbrirSelecaoMesAno() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = uiState.mesAtual, 
                                    fontWeight = FontWeight.Bold, 
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Trocar Mês")
                            }
                        }
                        IconButton(onClick = { onMudarMes(1) }) {
                            Icon(androidx.compose.material.icons.Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Próximo Mês")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onSincronizarPluggy) {
                        Icon(Icons.Default.Refresh, contentDescription = "Sincronizar")
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
                    onClick = { onNavigateToExtrato() }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Pendências") },
                    label = { Text("Pendências") },
                    selected = false,
                    onClick = { onNavigateToPendencias() }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            } else {
                DashboardContent(
                    uiState = uiState,
                    onNavigateToExtrato = onNavigateToExtrato,
                    onTransacaoClick = onTransacaoClick,
                    onAbrirConfiguracao = onAbrirConfiguracao
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
                ElevatedCard {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        if (uiState.syncStatus == SyncStatus.SUCCESS) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GreenPositive, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Sincronizado com Sucesso!", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Clear, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Erro na Sincronização.", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (uiState.showFaturaConfigDialog) {
            var fechamento by remember { mutableStateOf(uiState.diaFechamento.toString()) }
            var vencimento by remember { mutableStateOf(uiState.diaVencimento.toString()) }

            AlertDialog(
                onDismissRequest = onFecharConfiguracao,
                title = { Text("Configurar Fatura") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = fechamento,
                            onValueChange = { fechamento = it.filter { char -> char.isDigit() } },
                            label = { Text("Dia de Fechamento") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = vencimento,
                            onValueChange = { vencimento = it.filter { char -> char.isDigit() } },
                            label = { Text("Dia de Vencimento") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        val f = fechamento.toIntOrNull() ?: uiState.diaFechamento
                        val v = vencimento.toIntOrNull() ?: uiState.diaVencimento
                        onSalvarConfiguracao(f, v)
                    }) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onFecharConfiguracao) {
                        Text("Cancelar")
                    }
                }
            )
        }
        
        if (uiState.showMesAnoDialog) {
            val meses = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")
            var selectedAno by remember(uiState.anoSelecionado) { mutableStateOf(if (uiState.anoSelecionado > 0) uiState.anoSelecionado else java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)) }
            
            AlertDialog(
                onDismissRequest = { onFecharSelecaoMesAno() },
                title = { Text("Escolher Mês e Ano") },
                text = {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                        ) {
                            IconButton(onClick = { selectedAno-- }) {
                                Text("<")
                            }
                            Text(selectedAno.toString(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
                            IconButton(onClick = { selectedAno++ }) {
                                Text(">")
                            }
                        }
                        
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            itemsIndexed(meses) { index, mes ->
                                val isSelected = index == uiState.mesSelecionado && selectedAno == uiState.anoSelecionado
                                OutlinedButton(
                                    onClick = { onSelecionarMesAno(index, selectedAno) },
                                    colors = if (isSelected) ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer, contentColor = MaterialTheme.colorScheme.onPrimaryContainer) else ButtonDefaults.outlinedButtonColors()
                                ) {
                                    Text(mes)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { onSelecionarMesAno(java.util.Calendar.getInstance().get(java.util.Calendar.MONTH), java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)) }) {
                        Text("Mês Atual")
                    }
                }
            )
        }
    }
}

@Composable
private fun DashboardContent(
    uiState: DashboardUiState,
    onNavigateToExtrato: () -> Unit,
    onTransacaoClick: (String) -> Unit,
    onAbrirConfiguracao: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth().clickable { onNavigateToExtrato() },
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(
                                "Fatura Atual",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                            Text(
                                uiState.periodoFatura,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                            )
                        }
                        IconButton(onClick = onAbrirConfiguracao) {
                            Icon(Icons.Default.Settings, contentDescription = "Configurar", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        uiState.faturaTotal.toCurrencyString(),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        item {
            if (uiState.gastosPessoas.isNotEmpty()) {
                LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(uiState.gastosPessoas) { gasto ->
                        val parsedColor = try { androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(gasto.pessoa.corHex)) } catch (e: Exception) { MaterialTheme.colorScheme.primary }
                        
                        ElevatedCard(
                            modifier = Modifier.width(140.dp),
                            colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(gasto.pessoa.nome, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                                Text(
                                    gasto.valor.toCurrencyString(),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = parsedColor
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Proporção de Gastos",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                
                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(160.dp)) {
                        val strokeWidth = 24.dp.toPx()
                        var currentAngle = -90f
                        
                        if (uiState.gastosPessoas.isEmpty() || uiState.gastosPessoas.all { it.proporcao <= 0f }) {
                            drawArc(
                                color = androidx.compose.ui.graphics.Color.Black,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        } else {
                            uiState.gastosPessoas.forEach { gasto ->
                                val prop = gasto.proporcao.coerceIn(0f, 1f)
                                if (prop > 0) {
                                    val sweep = 360f * prop
                                    val parsedColor = try { androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(gasto.pessoa.corHex)) } catch (e: Exception) { androidx.compose.ui.graphics.Color.Gray }
                                    
                                    drawArc(
                                        color = parsedColor,
                                        startAngle = currentAngle,
                                        sweepAngle = sweep,
                                        useCenter = false,
                                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                    )
                                    currentAngle += sweep
                                }
                            }
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val meuGasto = uiState.gastosPessoas.firstOrNull()
                        if (meuGasto != null) {
                            val parsedColor = try { androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(meuGasto.pessoa.corHex)) } catch (e: Exception) { androidx.compose.ui.graphics.Color.Gray }
                            Text(
                                text = "${(meuGasto.proporcao * 100).toInt()}%",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = parsedColor
                            )
                            Text(
                                text = meuGasto.pessoa.nome,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
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
                    onClick = { onTransacaoClick(pendencia.transacao.id) }
                )
            }
        }
        
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}
