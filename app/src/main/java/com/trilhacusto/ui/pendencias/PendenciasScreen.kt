package com.trilhacusto.ui.pendencias

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onLimparMensagemSucesso: () -> Unit = {},
    onNavigateToPessoas: () -> Unit = {},
    onNavigateToCategorias: () -> Unit = {},
    onTogglePrivacyMode: () -> Unit,
    onNavigateToExcluidas: () -> Unit = {}
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var transacaoToDeleteId by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.mensagemSucesso) {
        uiState.mensagemSucesso?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            onLimparMensagemSucesso()
        }
    }

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
                        selected = false,
                        onClick = { onNavigateToExtrato() }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Pendências"
                            )
                        },
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
            },
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                if (uiState.transacoesPendentes.isNotEmpty() || uiState.termoBusca.isNotBlank()) {
                    com.trilhacusto.ui.components.GlassTextField(
                        value = uiState.termoBusca,
                        onValueChange = onBuscaChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        label = { Text("Pesquisar pendências...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    )
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else if (uiState.transacoesPendentes.isEmpty()) {
                        EmptyState(
                            quantidadeExcluidas = uiState.transacoesExcluidas.size,
                            onNavigateToExcluidas = onNavigateToExcluidas,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        val listaFiltrada = if (uiState.termoBusca.isNotBlank()) {
                            uiState.transacoesPendentes.filter {
                                (it.transacao.descricaoCustomizada
                                    ?: it.transacao.descricaoOriginal).contains(
                                    uiState.termoBusca,
                                    ignoreCase = true
                                )
                            }
                        } else {
                            uiState.transacoesPendentes
                        }

                        if (listaFiltrada.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "Nenhum resultado encontrado.",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (uiState.transacoesExcluidas.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    OutlinedButton(
                                        onClick = onNavigateToExcluidas,
                                        shape = RoundedCornerShape(20.dp),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        val total = uiState.transacoesExcluidas.size
                                        Text(
                                            text = if (total == 1) "Ver 1 pendência excluída"
                                            else "Ver $total pendências excluídas",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        } else {
                            val transacoesAgrupadas = listaFiltrada.groupBy {
                                it.transacao.dataHora.formatDateWithYearIfNeeded(includeTime = false)
                            }

                            LazyColumn(
                                contentPadding = PaddingValues(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 8.dp,
                                    bottom = 88.dp
                                ),
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
                                            isPrivacyModeEnabled = uiState.isPrivacyModeEnabled
                                        )
                                    }
                                }

                                // Botão no final da lista para acessar as pendências excluídas (apenas se houver itens excluídos)
                                if (uiState.transacoesExcluidas.isNotEmpty()) {
                                    item(key = "botao_acessar_excluidas") {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        BotaoVisualizarExcluidas(
                                            quantidadeExcluidas = uiState.transacoesExcluidas.size,
                                            onClick = onNavigateToExcluidas
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
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
                onDismissRequest = {
                    showDeleteDialog = false
                    transacaoToDeleteId = null
                },
                onConfirm = {
                    onExcluirClicked(transacaoToDeleteId!!)
                    showDeleteDialog = false
                    transacaoToDeleteId = null
                },
                title = "Excluir Pendência",
                text = "Esta transação será movida para a lista de excluídas e você poderá visualizá-la ou restaurá-la a qualquer momento.",
                confirmButtonText = "Excluir",
                dismissButtonText = "Cancelar",
                isDestructive = true
            )
        }
    }
}

@Composable
fun EmptyState(
    quantidadeExcluidas: Int = 0,
    onNavigateToExcluidas: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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
        if (quantidadeExcluidas > 0) {
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick = onNavigateToExcluidas,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (quantidadeExcluidas == 1) "Ver 1 pendência excluída"
                    else "Ver $quantidadeExcluidas pendências excluídas",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun BotaoVisualizarExcluidas(
    quantidadeExcluidas: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        color = Color(0xFF131424),
        border = BorderStroke(1.dp, Color(0xFF282A45)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF281830)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = null,
                        tint = Color(0xFFF472B6),
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "Ver pendências excluídas",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (quantidadeExcluidas == 0) "Nenhum item excluído"
                        else if (quantidadeExcluidas == 1) "1 item excluído"
                        else "$quantidadeExcluidas itens excluídos",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            if (quantidadeExcluidas > 0) {
                Surface(
                    shape = RoundedCornerShape(percent = 50),
                    color = Color(0xFF281830),
                    border = BorderStroke(1.dp, Color(0xFFF472B6).copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "$quantidadeExcluidas",
                        color = Color(0xFFF472B6),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun PendenciaCardItem(
    transacaoCompleta: TransacaoCompleta,
    onDividirClicked: () -> Unit,
    onEditarClicked: () -> Unit,
    onExcluirClicked: () -> Unit,
    modifier: Modifier = Modifier,
    isPrivacyModeEnabled: Boolean = false
) {
    val transacao = transacaoCompleta.transacao
    val categoria = transacaoCompleta.categoria

    val titulo = transacao.descricaoCustomizada ?: transacao.descricaoOriginal
    val dataFormatada = transacao.dataHora.formatDateWithYearIfNeeded(includeTime = true)

    com.trilhacusto.ui.components.GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDividirClicked() }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (categoria?.icone != null) {
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

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = dataFormatada,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = transacao.valorTotal.toCurrencyString(isPrivacyModeEnabled),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(
                        onClick = { onExcluirClicked() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Excluir pendência",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.75f)
                        )
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
