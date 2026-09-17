package com.trilhacusto.ui.ajustes.pessoas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trilhacusto.data.local.entity.PessoaEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PessoasScreen(
    viewModel: PessoaViewModel,
    onNavigateBack: () -> Unit
) {
    val pessoas by viewModel.pessoas.collectAsStateWithLifecycle()
    
    var showDialog by remember { mutableStateOf(false) }
    var pessoaEditando by remember { mutableStateOf<PessoaEntity?>(null) }
    
    var showDeleteDialog by remember { mutableStateOf(false) }
    var pessoaToDelete by remember { mutableStateOf<PessoaEntity?>(null) }
    var hasDebts by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pessoas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                pessoaEditando = null
                showDialog = true 
            }) {
                Icon(Icons.Default.Add, contentDescription = "Nova Pessoa")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            items(pessoas, key = { it.id }) { pessoa ->
                ListItem(
                    headlineContent = { Text(pessoa.nome) },
                    leadingContent = { Icon(Icons.Default.Person, contentDescription = null) },
                    trailingContent = {
                        Row {
                            IconButton(onClick = {
                                pessoaEditando = pessoa
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    hasDebts = viewModel.checkHasDebts(pessoa.id)
                                    pessoaToDelete = pessoa
                                    showDeleteDialog = true
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                )
                HorizontalDivider()
            }
        }

        if (showDialog) {
            PessoaDialog(
                pessoa = pessoaEditando,
                existingColors = pessoas.map { it.corHex },
                onDismiss = { showDialog = false },
                onSave = { novaPessoa ->
                    viewModel.salvarPessoa(novaPessoa)
                    showDialog = false
                }
            )
        }

        if (showDeleteDialog && pessoaToDelete != null) {
            com.trilhacusto.ui.components.GlassConfirmDialog(
                onDismissRequest = { showDeleteDialog = false },
                onConfirm = {
                    viewModel.excluirPessoaComTratamento(pessoaToDelete!!)
                    showDeleteDialog = false
                },
                title = "Excluir Pessoa",
                text = if (hasDebts) {
                    "Esta pessoa está associada a uma ou mais dívidas. Ao excluí-la, o rateio será desfeito e essas compras voltarão para a aba de Pendências. Tem certeza que deseja deletar?"
                } else {
                    "Deseja excluir esta pessoa?"
                },
                isDestructive = true
            )
        }
    }
}

@Composable
fun PessoaDialog(
    pessoa: PessoaEntity?,
    existingColors: List<String>,
    onDismiss: () -> Unit,
    onSave: (PessoaEntity) -> Unit
) {
    var nome by remember { mutableStateOf(pessoa?.nome ?: "") }

    com.trilhacusto.ui.components.GlassConfirmDialog(
        onDismissRequest = onDismiss,
        onConfirm = {
            if (nome.isNotBlank()) {
                val coresVibrantes = listOf("#E91E63", "#9C27B0", "#3F51B5", "#2196F3", "#00BCD4", "#009688", "#4CAF50", "#FF9800", "#FF5722")
                val availableColors = coresVibrantes.filter { it !in existingColors }
                val corPadrao = if (availableColors.isNotEmpty()) availableColors.random() else coresVibrantes.random()
                onSave(
                    PessoaEntity(
                        id = pessoa?.id ?: 0L,
                        nome = nome,
                        corHex = pessoa?.corHex ?: corPadrao
                    )
                )
            }
        },
        title = if (pessoa == null) "Nova Pessoa" else "Editar Pessoa",
        confirmButtonText = "Salvar",
        dismissButtonText = "Cancelar",
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                com.trilhacusto.ui.components.GlassTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }
        }
    )
}
