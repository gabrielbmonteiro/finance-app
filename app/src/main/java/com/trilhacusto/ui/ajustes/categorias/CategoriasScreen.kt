package com.trilhacusto.ui.ajustes.categorias

import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trilhacusto.data.local.entity.CategoriaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    viewModel: CategoriaViewModel,
    onNavigateBack: () -> Unit
) {
    val categorias by viewModel.categorias.collectAsStateWithLifecycle()
    
    var showDialog by remember { mutableStateOf(false) }
    var categoriaEditando by remember { mutableStateOf<CategoriaEntity?>(null) }
    
    var showDeleteDialog by remember { mutableStateOf(false) }
    var categoriaToDelete by remember { mutableStateOf<CategoriaEntity?>(null) }
    
    val errorState by viewModel.error.collectAsStateWithLifecycle()

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
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues)
            ) {
                items(categorias, key = { it.id }) { categoria ->
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
                                IconButton(onClick = {
                                    categoriaToDelete = categoria
                                    showDeleteDialog = true
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
                CategoriaDialog(
                    categoria = categoriaEditando,
                    onDismiss = { showDialog = false },
                    onSave = { novaCategoria ->
                        viewModel.salvarCategoria(novaCategoria)
                        showDialog = false
                    }
                )
            }

            if (showDeleteDialog && categoriaToDelete != null) {
                com.trilhacusto.ui.components.GlassConfirmDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    onConfirm = {
                        viewModel.excluirCategoria(categoriaToDelete!!)
                        showDeleteDialog = false
                    },
                    title = "Excluir Categoria",
                    text = "Deseja excluir esta categoria? As transações associadas a ela ficarão marcadas como 'Sem Categoria'.",
                    isDestructive = true
                )
            }
            
            com.trilhacusto.ui.components.GlassErrorSnackbar(
                errorMessage = errorState,
                onDismiss = { viewModel.clearError() },
                modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter).padding(bottom = 80.dp)
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

    com.trilhacusto.ui.components.GlassConfirmDialog(
        onDismissRequest = onDismiss,
        onConfirm = {
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
        },
        title = if (categoria == null) "Nova Categoria" else "Editar Categoria",
        isDarker = true,
        confirmButtonText = "Salvar",
        dismissButtonText = "Cancelar",
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                com.trilhacusto.ui.components.GlassTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome da Categoria") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
            }
        }
    )
}
