package com.narrapay.ui.rateio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.narrapay.ui.theme.GreenPositive
import com.narrapay.ui.theme.RedError
import com.narrapay.util.toCurrencyString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import com.narrapay.data.local.entity.PessoaEntity
import com.narrapay.data.local.entity.CategoriaEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateioScreen(
    uiState: RateioUiState,
    onBackClicked: () -> Unit,
    onConfirmarRateio: () -> Unit,
    onParteChanged: (Long, String) -> Unit,
    onTituloChanged: (String) -> Unit,
    onValorTotalChanged: (String) -> Unit,
    onCategoriaSelecionada: (Long?) -> Unit,
    onPessoaToggled: (PessoaEntity) -> Unit,
    onClearError: () -> Unit,
    onAddCategoria: (String) -> Unit,
    onEditCategoria: (CategoriaEntity, String) -> Unit,
    onRemoveCategoria: (CategoriaEntity) -> Unit,
    onNavigateToPessoas: () -> Unit
) {
    val valorTotalNumerico = uiState.valorTotalInput.replace(",", ".").toDoubleOrNull() ?: 0.0
    val soma = uiState.pessoasNaDivida.sumOf { pessoa -> 
        uiState.valoresInput[pessoa.id]?.replace(",", ".")?.toDoubleOrNull() ?: 0.0 
    }
    val diferenca = abs(valorTotalNumerico - soma)
    val isFechado = abs(valorTotalNumerico - soma) < 0.01 || soma == 0.0

    val snackbarHostState = remember { SnackbarHostState() }
    
    var showPessoasDialog by remember { mutableStateOf(false) }
    var showAddCategoriaDialog by remember { mutableStateOf(false) }
    var categoriaEditando by remember { mutableStateOf<CategoriaEntity?>(null) }
    var expandCategorias by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            snackbarHostState.showSnackbar(
                message = uiState.error,
                duration = SnackbarDuration.Long
            )
            onClearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Editar e Ratear", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { showPessoasDialog = true }) {
                        Icon(Icons.Default.Person, contentDescription = "Gerenciar Pessoas Envolvidas")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            Box(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = onConfirmarRateio,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = isFechado && uiState.tituloInput.isNotBlank() && uiState.valorTotalInput.isNotBlank()
                ) {
                    Text("SALVAR", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val sdf = SimpleDateFormat("dd 'de' MMMM, HH:mm", Locale("pt", "BR"))
            val dataFormatada = sdf.format(Date(uiState.dataHora))
            
            Text(text = dataFormatada, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.tituloInput,
                onValueChange = onTituloChanged,
                label = { Text("Nome da Dívida") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.valorTotalInput,
                onValueChange = onValorTotalChanged,
                label = { Text("Valor Total (R$)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            ExposedDropdownMenuBox(
                expanded = expandCategorias,
                onExpandedChange = { expandCategorias = !expandCategorias }
            ) {
                OutlinedTextField(
                    value = uiState.categorias.find { it.id == uiState.categoriaIdSelecionada }?.nome ?: "Sem Categoria",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoria") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandCategorias) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandCategorias,
                    onDismissRequest = { expandCategorias = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Sem Categoria") },
                        onClick = {
                            onCategoriaSelecionada(null)
                            expandCategorias = false
                        }
                    )
                    uiState.categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { 
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                    Text(cat.nome, modifier = Modifier.weight(1f))
                                    IconButton(onClick = { 
                                        categoriaEditando = cat 
                                        expandCategorias = false
                                    }, modifier = Modifier.size(24.dp)) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    IconButton(onClick = { onRemoveCategoria(cat) }, modifier = Modifier.size(24.dp)) {
                                        Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            },
                            onClick = {
                                onCategoriaSelecionada(cat.id)
                                expandCategorias = false
                            }
                        )
                    }
                    Divider()
                    DropdownMenuItem(
                        text = { Text("Adicionar Nova Categoria Rápida...", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) },
                        leadingIcon = { Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        onClick = {
                            expandCategorias = false
                            showAddCategoriaDialog = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Text("Divisão por Pessoa", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.pessoasNaDivida.isEmpty()) {
                Text("Nenhuma pessoa adicionada a esta dívida.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                uiState.pessoasNaDivida.forEach { pessoa ->
                    val inputValor = uiState.valoresInput[pessoa.id] ?: ""
                    RateioInputField(
                        label = pessoa.nome,
                        value = inputValor,
                        onValueChange = { novoValor -> onParteChanged(pessoa.id, novoValor) },
                        valorTotal = valorTotalNumerico
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                FeedbackCard(
                    soma = soma,
                    valorTotal = valorTotalNumerico,
                    diferenca = diferenca,
                    isFechado = isFechado
                )
            }
        }
    }

    if (showPessoasDialog) {
        AlertDialog(
            onDismissRequest = { showPessoasDialog = false },
            title = { Text("Pessoas Envolvidas") },
            text = {
                Column {
                    uiState.todasAsPessoas.forEach { pessoa ->
                        val isSelecionado = uiState.pessoasNaDivida.any { it.id == pessoa.id }
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Checkbox(checked = isSelecionado, onCheckedChange = { onPessoaToggled(pessoa) })
                            Text(pessoa.nome)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showPessoasDialog = false }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { 
                    showPessoasDialog = false
                    onNavigateToPessoas() 
                }) { Text("Gerenciar Pessoas") }
            }
        )
    }
    
    if (showAddCategoriaDialog) {
        var nomeCat by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddCategoriaDialog = false },
            title = { Text("Nova Categoria") },
            text = {
                OutlinedTextField(
                    value = nomeCat,
                    onValueChange = { nomeCat = it },
                    label = { Text("Nome da Categoria") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nomeCat.isNotBlank()) {
                        onAddCategoria(nomeCat)
                    }
                    showAddCategoriaDialog = false
                }) { Text("Criar") }
            },
            dismissButton = {
                TextButton(onClick = { showAddCategoriaDialog = false }) { Text("Cancelar") }
            }
        )
    }
    
    if (categoriaEditando != null) {
        var nomeCatEdit by remember { mutableStateOf(categoriaEditando!!.nome) }
        AlertDialog(
            onDismissRequest = { categoriaEditando = null },
            title = { Text("Editar Categoria") },
            text = {
                OutlinedTextField(
                    value = nomeCatEdit,
                    onValueChange = { nomeCatEdit = it },
                    label = { Text("Nome da Categoria") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (nomeCatEdit.isNotBlank()) {
                        onEditCategoria(categoriaEditando!!, nomeCatEdit)
                    }
                    categoriaEditando = null
                }) { Text("Salvar") }
            },
            dismissButton = {
                TextButton(onClick = { categoriaEditando = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun RateioInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    valorTotal: Double
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                val filtered = newValue.filter { it.isDigit() || it == '.' || it == ',' }
                onValueChange(filtered)
            },
            label = { Text(label) },
            prefix = { Text("R$ ") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AtalhoRateio(texto = "0%", onClick = { onValueChange("0") })
            AtalhoRateio(texto = "50%", onClick = { 
                val metade = valorTotal / 2
                onValueChange(String.format(Locale.US, "%.2f", metade)) 
            })
            AtalhoRateio(texto = "100%", onClick = { 
                onValueChange(String.format(Locale.US, "%.2f", valorTotal)) 
            })
        }
    }
}

@Composable
fun AtalhoRateio(texto: String, onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        Text(texto, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun FeedbackCard(soma: Double, valorTotal: Double, diferenca: Double, isFechado: Boolean) {
    val cardColor = if (isFechado) GreenPositive.copy(alpha = 0.1f) else RedError.copy(alpha = 0.1f)
    val textColor = if (isFechado) GreenPositive else RedError
    
    val mensagem = when {
        soma == 0.0 -> "Nenhum rateio aplicado. Salvará como pendente."
        isFechado -> "Total fechado perfeitamente!"
        soma < valorTotal -> "Faltam ${diferenca.toCurrencyString()} para fechar a conta"
        else -> "Passou ${diferenca.toCurrencyString()} do total da compra"
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
            Text(
                text = mensagem,
                color = textColor,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
