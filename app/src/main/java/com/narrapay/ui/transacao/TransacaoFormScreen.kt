package com.narrapay.ui.transacao

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransacaoFormScreen(
    uiState: TransacaoFormUiState,
    onBackClicked: () -> Unit,
    onDescricaoChanged: (String) -> Unit,
    onValorChanged: (String) -> Unit,
    onParcelasChanged: (String) -> Unit,
    onCategoriaChanged: (Long) -> Unit,
    onSalvar: () -> Unit,
    onExcluir: () -> Unit,
    onLimparErro: () -> Unit
) {
    var expanded by remember { androidx.compose.runtime.mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onBackClicked()
        }
    }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(uiState.error)
            onLimparErro()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (uiState.isEditing) "Editar Transação" else "Nova Transação", 
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    if (uiState.isEditing) {
                        IconButton(onClick = onExcluir) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = onSalvar,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !uiState.isLoading && uiState.descricaoInput.isNotBlank() && uiState.valorInput.isNotBlank()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("SALVAR", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.descricaoInput,
                onValueChange = onDescricaoChanged,
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.valorInput,
                onValueChange = { newValue ->
                    val filtered = newValue.filter { it.isDigit() || it == '.' || it == ',' }
                    onValorChanged(filtered)
                },
                label = { Text("Valor Total (R$)") },
                prefix = { Text("R$ ") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            if (!uiState.isEditing) {
                OutlinedTextField(
                    value = uiState.parcelasInput,
                    onValueChange = { newValue ->
                        val filtered = newValue.filter { it.isDigit() }
                        onParcelasChanged(filtered)
                    },
                    label = { Text("Número de Parcelas (Opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                val selectedCat = uiState.categorias.find { it.id == uiState.categoriaIdSelecionada }
                val label = if (selectedCat != null) selectedCat.nome else "Selecione uma Categoria"
                
                OutlinedTextField(
                    readOnly = true,
                    value = label,
                    onValueChange = { },
                    label = { Text("Categoria") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    uiState.categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.nome) },
                            onClick = {
                                onCategoriaChanged(categoria.id)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
