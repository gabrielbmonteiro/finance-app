package com.narrapay.ui.pendencias

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.narrapay.data.local.entity.TransacaoEntity
import com.narrapay.data.local.relation.TransacaoCompleta
import com.narrapay.ui.theme.NarrapayTheme
import com.narrapay.util.toCurrencyString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendenciasScreen(
    uiState: PendenciasUiState,
    onBuscaChanged: (String) -> Unit,
    onFilterSelected: (FiltroPendencia) -> Unit,
    onDividirClicked: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToExtrato: () -> Unit,
    onNavigateToNovaDespesa: () -> Unit,
    onNavigateToEditar: (String) -> Unit,
    onExcluirClicked: (String) -> Unit,
    onIgnorarClicked: (String) -> Unit,
    onReativarClicked: (String) -> Unit,
    onNavigateToPessoas: () -> Unit = {},
    onNavigateToCategorias: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aguardando Rateio", fontWeight = FontWeight.Bold) },
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
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToNovaDespesa,
                icon = { Text("➕") },
                text = { Text("NOVA DESPESA") }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (uiState.transacoesPendentes.isNotEmpty() || uiState.termoBusca.isNotBlank()) {
                OutlinedTextField(
                    value = uiState.termoBusca,
                    onValueChange = onBuscaChanged,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Pesquisar pendências...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true
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
                            val sdf = SimpleDateFormat("dd 'de' MMMM", Locale("pt", "BR"))
                            sdf.format(Date(it.transacao.dataHora))
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
                                    onExcluirClicked = { onExcluirClicked(transacaoCompleta.transacao.id) },
                                    onIgnorarClicked = { onIgnorarClicked(transacaoCompleta.transacao.id) },
                                    onReativarClicked = { onReativarClicked(transacaoCompleta.transacao.id) }
                                )
                            }
                        }
                        }
                    }
                }
            }
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
    modifier: Modifier = Modifier
) {
    val transacao = transacaoCompleta.transacao
    val categoria = transacaoCompleta.categoria
    
    val titulo = transacao.descricaoCustomizada ?: transacao.descricaoOriginal
    val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale("pt", "BR"))
    val dataFormatada = sdf.format(Date(transacao.dataHora))
    
    val isIgnorado = transacao.statusAtribuicao == "IGNORADO"
    val alphaValue = if (isIgnorado) 0.5f else 1.0f

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { 
                if (isIgnorado) {
                    onReativarClicked()
                } else {
                    onDividirClicked()
                }
            },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth().alpha(alphaValue)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isIgnorado) "🚫" else (categoria?.icone ?: "⏳"),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(end = 12.dp).alpha(alphaValue)
                )

                Column(modifier = Modifier.weight(1f).alpha(alphaValue)) {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isIgnorado) "$dataFormatada (Clique para reativar)" else dataFormatada,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = transacao.valorTotal.toCurrencyString(),
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
    NarrapayTheme {
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
            onNavigateToNovaDespesa = {},
            onNavigateToEditar = {},
            onExcluirClicked = {},
            onIgnorarClicked = {},
            onReativarClicked = {},
            onNavigateToPessoas = {},
            onNavigateToCategorias = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PendenciasScreenEmptyPreview() {
    NarrapayTheme {
        PendenciasScreen(
            uiState = PendenciasUiState(transacoesPendentes = emptyList()),
            onBuscaChanged = {},
            onFilterSelected = {},
            onDividirClicked = {},
            onNavigateToHome = {},
            onNavigateToExtrato = {},
            onNavigateToNovaDespesa = {},
            onNavigateToEditar = {},
            onExcluirClicked = {},
            onIgnorarClicked = {},
            onReativarClicked = {},
            onNavigateToPessoas = {},
            onNavigateToCategorias = {}
        )
    }
}
