package com.trilhacusto.ui.pendencias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trilhacusto.data.local.entity.TransacaoEntity
import com.trilhacusto.data.local.relation.TransacaoCompleta
import com.trilhacusto.ui.theme.TrilhaCustoTheme
import com.trilhacusto.util.formatDateWithYearIfNeeded
import com.trilhacusto.util.toCurrencyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendenciasScreen(
    uiState: PendenciasUiState,
    onBuscaChanged: (String) -> Unit,
    onFilterSelected: (FiltroPendencia) -> Unit,
    onDividirClicked: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToExtrato: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToNovaDespesa: () -> Unit,
    onNavigateToEditar: (String) -> Unit,
    onExcluirClicked: (String) -> Unit,
    onIgnorarClicked: (String) -> Unit,
    onReativarClicked: (String) -> Unit,
    onLimparErro: () -> Unit = {},
    onNavigateToPessoas: () -> Unit = {},
    onNavigateToCategorias: () -> Unit = {},
    onTogglePrivacyMode: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var transacaoToDeleteId by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aguardando Rateio", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                actions = {
                    IconButton(onClick = onTogglePrivacyMode) {
                        val icon = if (uiState.isPrivacyModeEnabled) Icons.Default.VisibilityOff else Icons.Default.Visibility
                        Icon(icon, contentDescription = "Alternar Visibilidade", tint = MaterialTheme.colorScheme.onBackground)
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
                    selected = false,
                    onClick = { onNavigateToExtrato() }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Pendências") },
                    label = { Text("Pendências") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Conta") },
                    label = { Text("Conta") },
                    selected = false,
                    onClick = { onNavigateToAccount() }
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToNovaDespesa,
                icon = { Icon(Icons.Default.Add, contentDescription = "Nova Despesa") },
                text = { Text("NOVA DESPESA") }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (uiState.transacoesPendentes.isNotEmpty() || uiState.termoBusca.isNotBlank()) {
                com.trilhacusto.ui.components.GlassTextField(
                    value = uiState.termoBusca,
                    onValueChange = onBuscaChanged,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    label = { Text("Pesquisar pendências...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) }
                )
            }
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (uiState.transacoesPendentes.isEmpty()) {
                    EmptyState(modifier = Modifier.align(Alignment.Center))
                } else {
                    val listaFiltrada = if (uiState.termoBusca.isNotBlank()) {
                        uiState.transacoesPendentes.filter { 
                            (it.transacao.descricaoCustomizada ?: it.transacao.descricaoOriginal).contains(uiState.termoBusca, ignoreCase = true) 
                        }
                    } else {
                        uiState.transacoesPendentes
                    }
                    
                    if (listaFiltrada.isEmpty()) {
                        Text("Nenhum resultado encontrado.", modifier = Modifier.align(Alignment.Center).padding(16.dp))
                    } else {
                        val transacoesAgrupadas = listaFiltrada.groupBy {
                            it.transacao.dataHora.formatDateWithYearIfNeeded(includeTime = false)
                        }

                        LazyColumn(
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                        transacoesAgrupadas.forEach { (data, lista) ->
                            item {
                                Text(
                                    text = data,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                )
                            }
                            items(lista) { transacaoCompleta ->
                                PendenciaCardItem(
                                    transacaoCompleta = transacaoCompleta,
                                    onDividirClicked = { onDividirClicked(transacaoCompleta.transacao.id) },
                                    onEditarClicked = { onNavigateToEditar(transacaoCompleta.transacao.id) },
                                    onExcluirClicked = { 
                                        transacaoToDeleteId = transacaoCompleta.transacao.id
                                        showDeleteDialog = true
                                    },
                                    onIgnorarClicked = { onIgnorarClicked(transacaoCompleta.transacao.id) },
                                    onReativarClicked = { onReativarClicked(transacaoCompleta.transacao.id) },
                                    isPrivacyModeEnabled = uiState.isPrivacyModeEnabled
                                )
                            }
                        }
                        }
                    }
                }
            }
        }
    }

    com.trilhacusto.ui.components.GlassErrorSnackbar(
        errorMessage = uiState.error,
        onDismiss = onLimparErro,
        modifier = Modifier.padding(16.dp)
    )

    if (showDeleteDialog && transacaoToDeleteId != null) {
        com.trilhacusto.ui.components.GlassConfirmDialog(
            onDismissRequest = { showDeleteDialog = false },
            onConfirm = {
                onExcluirClicked(transacaoToDeleteId!!)
                showDeleteDialog = false
                transacaoToDeleteId = null
            },
            title = "Excluir Pendência",
            text = "Tem certeza que deseja excluir esta transação permanentemente?",
            isDestructive = true
        )
    }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Tudo certo",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tudo categorizado!\nNenhuma pendência.",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PendenciaCardItem(
    transacaoCompleta: TransacaoCompleta,
    onDividirClicked: () -> Unit,
    onEditarClicked: () -> Unit,
    onExcluirClicked: () -> Unit,
    onIgnorarClicked: () -> Unit,
    onReativarClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isPrivacyModeEnabled: Boolean = false
) {
    val transacao = transacaoCompleta.transacao
    val categoria = transacaoCompleta.categoria
    
    val titulo = transacao.descricaoCustomizada ?: transacao.descricaoOriginal
    val dataFormatada = transacao.dataHora.formatDateWithYearIfNeeded(includeTime = true)
    
    val isIgnorado = transacao.statusAtribuicao == "IGNORADO"
    val alphaValue = if (isIgnorado) 0.5f else 1.0f

    com.trilhacusto.ui.components.GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { 
                if (isIgnorado) {
                    onReativarClicked()
                } else {
                    onDividirClicked()
                }
            }
    ) {
        Column(modifier = Modifier.fillMaxWidth().alpha(alphaValue)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isIgnorado) MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                            else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                        .alpha(alphaValue),
                    contentAlignment = Alignment.Center
                ) {
                    if (isIgnorado) {
                        Icon(
                            imageVector = Icons.Default.Block,
                            contentDescription = "Ignorado",
                            tint = MaterialTheme.colorScheme.error
                        )
                    } else if (categoria?.icone != null) {
                        Text(
                            text = categoria.icone,
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.HourglassEmpty,
                            contentDescription = "Pendente",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f).alpha(alphaValue)) {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isIgnorado) "$dataFormatada\n(Clique para reativar)" else dataFormatada,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = transacao.valorTotal.toCurrencyString(isPrivacyModeEnabled),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = alphaValue)
                    )
                    Row {
                        if (!isIgnorado) {
                            IconButton(onClick = { onIgnorarClicked() }, modifier = Modifier.size(32.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Desativar",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        IconButton(onClick = { onExcluirClicked() }, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Excluir",
                                tint = MaterialTheme.colorScheme.error.copy(alpha = alphaValue)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PendenciasScreenPreview() {
    TrilhaCustoTheme {
        PendenciasScreen(
            uiState = PendenciasUiState(
                transacoesPendentes = listOf(
                    TransacaoCompleta(
                        TransacaoEntity(
                            id = "1",
                            valorTotal = 200.0,
                            descricaoOriginal = "Supermercado Extra",
                            dataHora = 1700000000000L,
                            statusAtribuicao = "PENDENTE"
                        ),
                        null,
                        emptyList()
                    ),
                    TransacaoCompleta(
                        TransacaoEntity(
                            id = "2",
                            valorTotal = 45.90,
                            descricaoOriginal = "Uber",
                            dataHora = 1700000000000L,
                            statusAtribuicao = "PENDENTE"
                        ),
                        null,
                        emptyList()
                    )
                )
            ),
            onBuscaChanged = {},
            onFilterSelected = {},
            onDividirClicked = {},
            onNavigateToHome = {},
            onNavigateToExtrato = {},
            onNavigateToAccount = {},
            onNavigateToNovaDespesa = {},
            onNavigateToEditar = {},
            onExcluirClicked = {},
            onIgnorarClicked = {},
            onReativarClicked = {},
            onTogglePrivacyMode = {},
            onNavigateToPessoas = {},
            onNavigateToCategorias = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PendenciasScreenEmptyPreview() {
    TrilhaCustoTheme {
        PendenciasScreen(
            uiState = PendenciasUiState(transacoesPendentes = emptyList()),
            onBuscaChanged = {},
            onFilterSelected = {},
            onDividirClicked = {},
            onNavigateToHome = {},
            onNavigateToExtrato = {},
            onNavigateToAccount = {},
            onNavigateToNovaDespesa = {},
            onNavigateToEditar = {},
            onExcluirClicked = {},
            onIgnorarClicked = {},
            onReativarClicked = {},
            onTogglePrivacyMode = {},
            onNavigateToPessoas = {},
            onNavigateToCategorias = {}
        )
    }
}
