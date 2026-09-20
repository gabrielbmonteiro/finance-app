package com.trilhacusto.ui.extrato

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trilhacusto.ui.components.TransacaoCardItem
import com.trilhacusto.util.formatDateWithYearIfNeeded

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
                    selected = true,
                    onClick = { }
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
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)) }
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    val isSelected = uiState.filtroResponsavel == null
                    val brush = if (isSelected) {
                        androidx.compose.ui.graphics.Brush.linearGradient(colors = listOf(com.trilhacusto.ui.theme.PurplePrimary, com.trilhacusto.ui.theme.PurpleSecondary))
                    } else {
                        androidx.compose.ui.graphics.Brush.linearGradient(colors = listOf(com.trilhacusto.ui.theme.GlassBackground, com.trilhacusto.ui.theme.GlassBackground))
                    }
                    Box(
                        modifier = Modifier
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(percent = 50))
                            .background(brush)
                            .border(1.dp, com.trilhacusto.ui.theme.GlassBorder, androidx.compose.foundation.shape.RoundedCornerShape(percent = 50))
                            .clickable { onFiltroResponsavelChanged(null) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Todas", color = if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                items(uiState.pessoas) { pessoa ->
                    val isSelected = uiState.filtroResponsavel == pessoa.id
                    val brush = if (isSelected) {
                        androidx.compose.ui.graphics.Brush.linearGradient(colors = listOf(com.trilhacusto.ui.theme.PurplePrimary, com.trilhacusto.ui.theme.PurpleSecondary))
                    } else {
                        androidx.compose.ui.graphics.Brush.linearGradient(colors = listOf(com.trilhacusto.ui.theme.GlassBackground, com.trilhacusto.ui.theme.GlassBackground))
                    }
                    Box(
                        modifier = Modifier
                            .clip(androidx.compose.foundation.shape.RoundedCornerShape(percent = 50))
                            .background(brush)
                            .border(1.dp, com.trilhacusto.ui.theme.GlassBorder, androidx.compose.foundation.shape.RoundedCornerShape(percent = 50))
                            .clickable { onFiltroResponsavelChanged(pessoa.id) }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(pessoa.nome, color = if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurfaceVariant)
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
            } else if (uiState.transacoes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhuma transação encontrada.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                val transacoesAgrupadas = uiState.transacoes.groupBy {
                    it.transacao.dataHora.formatDateWithYearIfNeeded(includeTime = false)
                }
                
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    transacoesAgrupadas.forEach { (data, lista) ->
                        item(key = "header_$data") {
                            Text(
                                text = data,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                        }
                        items(
                            items = lista,
                            key = { transacao -> transacao.transacao.id }
                        ) { transacao ->
                            TransacaoCardItem(
                                transacaoCompleta = transacao,
                                onClick = { onTransacaoClick(transacao.transacao.id) },
                                onExcluir = { 
                                    transacaoToDeleteId = transacao.transacao.id
                                    showDeleteDialog = true
                                },
                                filtroPessoaId = uiState.filtroResponsavel,
                                isPrivacyModeEnabled = uiState.isPrivacyModeEnabled
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        } // closes Column
    } // closes Scaffold lambda
    
    // Agora fora do Scaffold, dentro do Box:

        
        com.trilhacusto.ui.components.GlassErrorSnackbar(
            errorMessage = uiState.error,
            onDismiss = onLimparErro,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp)
        )

        if (showDeleteDialog && transacaoToDeleteId != null) {
            com.trilhacusto.ui.components.GlassConfirmDialog(
                onDismissRequest = { showDeleteDialog = false },
                onConfirm = {
                    onExcluirClick(transacaoToDeleteId!!)
                    showDeleteDialog = false
                },
                title = "Excluir Transação",
                text = "Tem certeza que deseja excluir permanentemente esta transação? Esta ação não pode ser desfeita.",
                isDestructive = true
            )
        }
    }
}
