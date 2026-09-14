package com.narrapay.ui.extrato

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.narrapay.ui.components.TransacaoCardItem
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtratoScreen(
    uiState: ExtratoUiState,
    onBuscaChanged: (String) -> Unit,
    onFiltroResponsavelChanged: (Long?) -> Unit,
    onTransacaoClick: (String) -> Unit,
    onExcluirClick: (String) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPendencias: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Extrato de Compras") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
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
                    selected = true,
                    onClick = { }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = uiState.filtroBusca,
                onValueChange = onBuscaChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Buscar por descrição...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = uiState.filtroResponsavel == null,
                        onClick = { onFiltroResponsavelChanged(null) },
                        label = { Text("Todas") }
                    )
                }
                items(uiState.pessoas) { pessoa ->
                    FilterChip(
                        selected = uiState.filtroResponsavel == pessoa.id,
                        onClick = { onFiltroResponsavelChanged(pessoa.id) },
                        label = { Text(pessoa.nome) }
                    )
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
            } else if (uiState.transacoes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma transação encontrada.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                val transacoesAgrupadas = uiState.transacoes.groupBy {
                    val sdf = java.text.SimpleDateFormat("dd 'de' MMMM", java.util.Locale("pt", "BR"))
                    sdf.format(java.util.Date(it.transacao.dataHora))
                }
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
                        items(lista) { transacao ->
                            TransacaoCardItem(
                                transacaoCompleta = transacao,
                                onClick = { onTransacaoClick(transacao.transacao.id) },
                                onExcluir = { onExcluirClick(transacao.transacao.id) }
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}
