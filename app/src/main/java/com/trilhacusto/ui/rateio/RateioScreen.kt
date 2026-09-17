package com.trilhacusto.ui.rateio

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.trilhacusto.data.local.entity.CategoriaEntity
import com.trilhacusto.data.local.entity.PessoaEntity
import com.trilhacusto.ui.components.FeedbackCard
import com.trilhacusto.ui.components.GlassConfirmDialog
import com.trilhacusto.ui.components.RateioInputField
import kotlin.math.abs

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
    onDataHoraChanged: (Long) -> Unit,
    onPessoaToggled: (PessoaEntity) -> Unit,
    onClearError: () -> Unit,
    onAddCategoria: (String) -> Unit,
    onEditCategoria: (CategoriaEntity, String) -> Unit,
    onRemoveCategoria: (CategoriaEntity) -> Unit,
    onNavigateToPessoas: () -> Unit,
    onDividirIgualmente: () -> Unit,
    onAceitarSugestao: () -> Unit,
    onDropdownPessoaChanged: (Long?) -> Unit,
    onDropdownValorChanged: (String) -> Unit,
    onAdicionarResponsavelDropdown: (Long) -> Unit,
    onRemoverResponsavelDropdown: (Long) -> Unit
) {
    val valorTotalNumerico = uiState.valorTotalInput.replace(",", ".").toDoubleOrNull() ?: 0.0
    val soma = uiState.pessoasNaDivida.sumOf { pessoa -> 
        uiState.valoresInput[pessoa.id]?.replace(",", ".")?.toDoubleOrNull() ?: 0.0 
    }
    val diferenca = abs(valorTotalNumerico - soma)
    val isFechado = abs(valorTotalNumerico - soma) < 0.01 || soma == 0.0

    val snackbarHostState = remember { SnackbarHostState() }
    var showAddCategoriaDialog by remember { mutableStateOf(false) }
    var categoriaEditando by remember { mutableStateOf<CategoriaEntity?>(null) }

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
                    IconButton(onClick = onNavigateToPessoas) {
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
                com.trilhacusto.ui.components.GlassButton(
                    text = "SALVAR",
                    onClick = onConfirmarRateio,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFechado && uiState.tituloInput.isNotBlank() && uiState.valorTotalInput.isNotBlank()
                )
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
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                var showDatePicker by remember { mutableStateOf(false) }
                var showTimePicker by remember { mutableStateOf(false) }
                val datePattern = "dd MMM yy"
                val sdfDate = java.text.SimpleDateFormat(datePattern, java.util.Locale("pt", "BR"))
                val sdfTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale("pt", "BR"))

                com.trilhacusto.ui.components.GlassCard(
                    modifier = Modifier.weight(1f).clickable { showDatePicker = true }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Data", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        Text(sdfDate.format(java.util.Date(uiState.dataHora)).replace(".", ""), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                    }
                }

                com.trilhacusto.ui.components.GlassCard(
                    modifier = Modifier.weight(1f).clickable { showTimePicker = true }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Hora", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        Text(sdfTime.format(java.util.Date(uiState.dataHora)), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    }
                }

                if (showDatePicker) {
                    var selectedMillis by remember { mutableStateOf(uiState.dataHora) }
                    GlassConfirmDialog(
                        onDismissRequest = { showDatePicker = false },
                        onConfirm = {
                            showDatePicker = false
                            onDataHoraChanged(selectedMillis)
                        },
                        title = "Selecionar Data",
                        isDarker = true,
                        contentPadding = PaddingValues(vertical = 24.dp, horizontal = 0.dp),
                        confirmButtonText = "OK",
                        dismissButtonText = "Cancelar",
                        content = {
                            com.trilhacusto.ui.components.GlassDatePicker(
                                initialDateMillis = selectedMillis,
                                onDateSelected = { millis -> selectedMillis = millis }
                            )
                        }
                    )
                }

                if (showTimePicker) {
                    val initialCal = java.util.Calendar.getInstance().apply { timeInMillis = uiState.dataHora }
                    val timePickerState = rememberTimePickerState(
                        initialHour = initialCal.get(java.util.Calendar.HOUR_OF_DAY),
                        initialMinute = initialCal.get(java.util.Calendar.MINUTE),
                        is24Hour = true
                    )
                    GlassConfirmDialog(
                        onDismissRequest = { showTimePicker = false },
                        onConfirm = {
                            showTimePicker = false
                            val newCal = java.util.Calendar.getInstance().apply { timeInMillis = uiState.dataHora }
                            newCal.set(java.util.Calendar.HOUR_OF_DAY, timePickerState.hour)
                            newCal.set(java.util.Calendar.MINUTE, timePickerState.minute)
                            onDataHoraChanged(newCal.timeInMillis)
                        },
                        title = "Selecionar Horário",
                        isDarker = true,
                        confirmButtonText = "OK",
                        dismissButtonText = "Cancelar",
                        content = {
                            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                TimePicker(state = timePickerState)
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            com.trilhacusto.ui.components.GlassTextField(
                value = uiState.tituloInput,
                onValueChange = onTituloChanged,
                label = { Text("Nome da Dívida") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            com.trilhacusto.ui.components.GlassTextField(
                value = uiState.valorTotalInput,
                onValueChange = onValorTotalChanged,
                label = { Text("Valor Total") },
                visualTransformation = com.trilhacusto.ui.transacao.CurrencyVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            com.trilhacusto.ui.components.GlassCategorySelector(
                categorias = uiState.categorias,
                categoriaSelecionadaId = uiState.categoriaIdSelecionada,
                onCategoriaSelecionada = onCategoriaSelecionada,
                onEditarCategoria = { categoriaEditando = it },
                onRemoverCategoria = onRemoveCategoria,
                onAdicionarCategoria = { showAddCategoriaDialog = true },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Divisão por Pessoa", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                if (uiState.pessoasNaDivida.size > 1 && valorTotalNumerico > 0) {
                    OutlinedButton(onClick = onDividirIgualmente) {
                        Text("Dividir Igualmente")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.pessoasNaDivida.isEmpty()) {
                Text("Nenhuma pessoa adicionada a esta dívida.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                uiState.pessoasNaDivida.forEach { pessoa ->
                    val inputValor = uiState.valoresInput[pessoa.id] ?: ""
                    val isSuggested = uiState.pessoaIdSugestao == pessoa.id
                    val suggestionText = if (isSuggested) uiState.valorSugestao else null
                    
                    RateioInputField(
                        label = pessoa.nome,
                        value = inputValor,
                        onValueChange = { novoValor -> onParteChanged(pessoa.id, novoValor) },
                        valorTotal = valorTotalNumerico,
                        suggestionText = suggestionText,
                        onSuggestionClick = if (isSuggested) onAceitarSugestao else null,
                        onRemoveClick = { onRemoverResponsavelDropdown(pessoa.id) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            var expandPessoas by remember { mutableStateOf(false) }
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    val selectedName = "Selecionar para Adicionar..."
                    OutlinedButton(onClick = { expandPessoas = true }, modifier = Modifier.fillMaxWidth()) {
                        Text(selectedName)
                    }
                    DropdownMenu(expanded = expandPessoas, onDismissRequest = { expandPessoas = false }) {
                        uiState.todasAsPessoas.filter { it.id !in uiState.pessoasNaDivida.map { p -> p.id } }.forEach { pessoa ->
                            DropdownMenuItem(
                                text = { Text(pessoa.nome) },
                                onClick = {
                                    onAdicionarResponsavelDropdown(pessoa.id)
                                    expandPessoas = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.pessoasNaDivida.isNotEmpty()) {
                FeedbackCard(
                    soma = soma,
                    valorTotal = valorTotalNumerico,
                    diferenca = diferenca,
                    isFechado = isFechado
                )
            }
        }
    }

    if (showAddCategoriaDialog) {
        var nomeCat by remember { mutableStateOf("") }
        GlassConfirmDialog(
            onDismissRequest = { showAddCategoriaDialog = false },
            onConfirm = {
                if (nomeCat.isNotBlank()) {
                    onAddCategoria(nomeCat)
                }
                showAddCategoriaDialog = false
            },
            title = "Nova Categoria",
            isDarker = true,
            confirmButtonText = "Criar",
            dismissButtonText = "Cancelar",
            content = {
                com.trilhacusto.ui.components.GlassTextField(
                    value = nomeCat,
                    onValueChange = { nomeCat = it },
                    label = { Text("Nome da Categoria") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
    
    if (categoriaEditando != null) {
        var nomeCatEdit by remember { mutableStateOf(categoriaEditando!!.nome) }
        GlassConfirmDialog(
            onDismissRequest = { categoriaEditando = null },
            onConfirm = {
                if (nomeCatEdit.isNotBlank()) {
                    onEditCategoria(categoriaEditando!!, nomeCatEdit)
                }
                categoriaEditando = null
            },
            title = "Editar Categoria",
            isDarker = true,
            confirmButtonText = "Salvar",
            dismissButtonText = "Cancelar",
            content = {
                com.trilhacusto.ui.components.GlassTextField(
                    value = nomeCatEdit,
                    onValueChange = { nomeCatEdit = it },
                    label = { Text("Nome da Categoria") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}


