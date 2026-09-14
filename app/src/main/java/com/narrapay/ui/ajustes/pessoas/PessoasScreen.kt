package com.narrapay.ui.ajustes.pessoas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.narrapay.data.local.entity.PessoaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PessoasScreen(
    viewModel: PessoaViewModel,
    onNavigateBack: () -> Unit
) {
    val pessoas by viewModel.pessoas.collectAsState()
    
    var showDialog by remember { mutableStateOf(false) }
    var pessoaEditando by remember { mutableStateOf<PessoaEntity?>(null) }

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
            items(pessoas) { pessoa ->
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
                            IconButton(onClick = { viewModel.excluirPessoa(pessoa) }) {
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
                onDismiss = { showDialog = false },
                onSave = { novaPessoa ->
                    viewModel.salvarPessoa(novaPessoa)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun PessoaDialog(
    pessoa: PessoaEntity?,
    onDismiss: () -> Unit,
    onSave: (PessoaEntity) -> Unit
) {
    var nome by remember { mutableStateOf(pessoa?.nome ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (pessoa == null) "Nova Pessoa" else "Editar Pessoa") },
        text = {
            Column {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (nome.isNotBlank()) {
                    val coresVibrantes = listOf("#E91E63", "#9C27B0", "#3F51B5", "#2196F3", "#00BCD4", "#009688", "#4CAF50", "#FF9800", "#FF5722")
                    val corPadrao = coresVibrantes.random()
                    onSave(
                        PessoaEntity(
                            id = pessoa?.id ?: 0L,
                            nome = nome,
                            corHex = pessoa?.corHex ?: corPadrao
                        )
                    )
                }
            }) {
                Text("Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
