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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trilhacusto.data.local.relation.TransacaoCompleta
import com.trilhacusto.ui.components.GlassCard
import com.trilhacusto.ui.components.GlassConfirmDialog
import com.trilhacusto.ui.components.GlassErrorSnackbar
import com.trilhacusto.ui.components.GlassTextField
import com.trilhacusto.util.formatDateWithYearIfNeeded
import com.trilhacusto.util.toCurrencyString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendenciasExcluidasScreen(
    uiState: PendenciasUiState,
    onNavigateBack: () -> Unit,
    onRetirarDaExclusao: (String) -> Unit,
    onExcluirDefinitivamente: (String) -> Unit,
    onBuscaChanged: (String) -> Unit,
    onTogglePrivacyMode: () -> Unit,
    onLimparErro: () -> Unit = {},
    onLimparMensagemSucesso: () -> Unit = {}
) {
    var transacaoToDeleteId by remember { mutableStateOf<String?>(null) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.mensagemSucesso) {
        uiState.mensagemSucesso?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            onLimparMensagemSucesso()
        }
    }

    val listaFiltrada = if (uiState.termoBuscaExcluidas.isNotBlank()) {
        uiState.transacoesExcluidas.filter {
            val titulo = (it.transacao.descricaoCustomizada ?: it.transacao.descricaoOriginal).lowercase()
            titulo.contains(uiState.termoBuscaExcluidas.lowercase())
        }
    } else {
        uiState.transacoesExcluidas
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "Pendências Excluídas",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = if (uiState.transacoesExcluidas.isEmpty()) "Nenhum item"
                                else "${uiState.transacoesExcluidas.size} item(ns) arquivado(s)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
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
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (uiState.transacoesExcluidas.isNotEmpty()) {
                    // Campo de Busca
                    GlassTextField(
                        value = uiState.termoBuscaExcluidas,
                        onValueChange = onBuscaChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        label = { Text("Pesquisar excluídas...") },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }
                    )

                    // Card explicativo/informativo
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        color = Color(0xFF131525),
                        border = BorderStroke(1.dp, Color(0xFF252845)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color(0xFF60A5FA),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Estes itens foram desativados do rateio. Toque em 'Retirar da exclusão' para que voltem às pendências ativas.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else if (uiState.transacoesExcluidas.isEmpty()) {
                        EmptyExcluidasState(modifier = Modifier.align(Alignment.Center))
                    } else if (listaFiltrada.isEmpty()) {
                        Text(
                            "Nenhum resultado encontrado.",
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        val agrupadas = listaFiltrada.groupBy {
                            it.transacao.dataHora.formatDateWithYearIfNeeded(includeTime = false)
                        }

                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                top = 8.dp,
                                bottom = 24.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            agrupadas.forEach { (data, lista) ->
                                item {
                                    Text(
                                        text = data,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                    )
                                }
                                items(lista, key = { it.transacao.id }) { transacaoCompleta ->
                                    ItemExcluidoCard(
                                        transacaoCompleta = transacaoCompleta,
                                        isPrivacyModeEnabled = uiState.isPrivacyModeEnabled,
                                        onRetirarDaExclusao = {
                                            onRetirarDaExclusao(transacaoCompleta.transacao.id)
                                        },
                                        onExcluirDefinitivo = {
                                            transacaoToDeleteId = transacaoCompleta.transacao.id
                                            showConfirmDeleteDialog = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        GlassErrorSnackbar(
            errorMessage = uiState.error,
            onDismiss = onLimparErro,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )

        if (showConfirmDeleteDialog && transacaoToDeleteId != null) {
            GlassConfirmDialog(
                onDismissRequest = {
                    showConfirmDeleteDialog = false
                    transacaoToDeleteId = null
                },
                onConfirm = {
                    onExcluirDefinitivamente(transacaoToDeleteId!!)
                    showConfirmDeleteDialog = false
                    transacaoToDeleteId = null
                },
                title = "Excluir",
                text = "Tem certeza que deseja apagar permanentemente esta transação do aplicativo? Esta ação NÃO pode ser desfeita.",
                confirmButtonText = "Excluir",
                dismissButtonText = "Cancelar",
                isDestructive = true
            )
        }
    }
}

@Composable
private fun ItemExcluidoCard(
    transacaoCompleta: TransacaoCompleta,
    isPrivacyModeEnabled: Boolean,
    onRetirarDaExclusao: () -> Unit,
    onExcluirDefinitivo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transacao = transacaoCompleta.transacao
    val categoria = transacaoCompleta.categoria
    val titulo = transacao.descricaoCustomizada ?: transacao.descricaoOriginal
    val dataFormatada = transacao.dataHora.formatDateWithYearIfNeeded(includeTime = true)

    GlassCard(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícone
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF281830)),
                    contentAlignment = Alignment.Center
                ) {
                    if (categoria?.icone != null) {
                        Text(
                            text = categoria.icone,
                            style = MaterialTheme.typography.titleMedium
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Excluída",
                            tint = Color(0xFFF472B6),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Título e Data
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = titulo,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = dataFormatada,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Valor
                Text(
                    text = transacao.valorTotal.toCurrencyString(isPrivacyModeEnabled),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Linha de Ações: Botão "Retirar da exclusão" e "Excluir definitivo"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botão de restaurar / retirar da exclusão
                Button(
                    onClick = onRetirarDaExclusao,
                    shape = RoundedCornerShape(percent = 50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1E284A),
                        contentColor = Color(0xFF93C5FD)
                    ),
                    border = BorderStroke(1.dp, Color(0xFF3B82F6).copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Restore,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF93C5FD)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Retirar da exclusão",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Botão de exclusão definitiva
                IconButton(
                    onClick = onExcluirDefinitivo,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = "Excluir definitivamente",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.75f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyExcluidasState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(percent = 50))
                .background(Color(0xFF1C1D33)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DeleteOutline,
                contentDescription = "Vazio",
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Nenhuma pendência excluída",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Itens que você excluir da tela de pendências aparecerão aqui caso queira restaurá-los.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}
