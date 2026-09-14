package com.narrapay.ui.ajustes.categorias

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.narrapay.data.local.entity.CategoriaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    viewModel: CategoriaViewModel,
    onNavigateBack: () -> Unit
) {
    val categorias by viewModel.categorias.collectAsState()
    
    var showDialog by remember { mutableStateOf(false) }
    var categoriaEditando by remember { mutableStateOf<CategoriaEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorias") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                categoriaEditando = null
                showDialog = true 
            }) {
                Icon(Icons.Default.Add, contentDescription = "Nova Categoria")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            items(categorias) { categoria ->
                ListItem(
                    headlineContent = { Text(categoria.nome) },
                    trailingContent = {
                        Row {
                            IconButton(onClick = {
                                categoriaEditando = categoria
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = { viewModel.excluirCategoria(categoria) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                )
                HorizontalDivider()
            }
        }

        if (showDialog) {
            CategoriaDialog(
                categoria = categoriaEditando,
                onDismiss = { showDialog = false },
                onSave = { novaCategoria ->
                    viewModel.salvarCategoria(novaCategoria)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun CategoriaDialog(
    categoria: CategoriaEntity?,
    onDismiss: () -> Unit,
    onSave: (CategoriaEntity) -> Unit
) {
    var nome by remember { mutableStateOf(categoria?.nome ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (categoria == null) "Nova Categoria" else "Editar Categoria") },
        text = {
            Column {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome da Categoria") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (nome.isNotBlank()) {
                    onSave(
                        CategoriaEntity(
                            id = categoria?.id ?: 0L,
                            nome = nome,
                            icone = "",
                            corHex = categoria?.corHex ?: "#000000"
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
