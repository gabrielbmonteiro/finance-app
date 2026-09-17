package com.trilhacusto.ui.transacao

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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.trilhacusto.ui.components.GlassConfirmDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransacaoFormScreen(
    uiState: TransacaoFormUiState,
    onBackClicked: () -> Unit,
    onDescricaoChanged: (String) -> Unit,
    onValorChanged: (String) -> Unit,
    onParcelasChanged: (String) -> Unit,
    onCategoriaChanged: (Long) -> Unit,
    onDataHoraChanged: (Long) -> Unit,
    onSalvar: () -> Unit,
    onExcluir: () -> Unit,
    onLimparErro: () -> Unit,
    onSalvarCategoria: (String, String, Long?) -> Unit,
    onDeletarCategoria: (com.trilhacusto.data.local.entity.CategoriaEntity) -> Unit,
    onPessoaSugestaoChanged: (Long?) -> Unit,
    onValorSugestaoChanged: (String) -> Unit,
    onAdicionarResponsavel: (Long) -> Unit,
    onRemoverResponsavel: (Long) -> Unit,
    onParteChanged: (Long, String) -> Unit,
    onDividirIgualmente: () -> Unit,
    onNavigateToPessoas: () -> Unit,
    onAceitarSugestao: () -> Unit
) {
    var showAddCategoriaDialog by remember { mutableStateOf(false) }
    var categoriaEditando by remember { mutableStateOf<com.trilhacusto.data.local.entity.CategoriaEntity?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onBackClicked()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (uiState.isEditing) "Editar Despesa" else "Nova Despesa", fontWeight = FontWeight.Bold) },
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
                        containerColor = Color.Transparent,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
        bottomBar = {
            Box(modifier = Modifier.padding(16.dp)) {
                com.trilhacusto.ui.components.GlassButton(
                    text = if (uiState.isLoading) "SALVANDO..." else "SALVAR",
                    onClick = onSalvar,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoading && uiState.descricaoInput.isNotBlank() && uiState.valorInput.isNotBlank()
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            com.trilhacusto.ui.components.GlassTextField(
                value = uiState.descricaoInput,
                onValueChange = onDescricaoChanged,
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            var isValorFocused by remember { mutableStateOf(false) }

            com.trilhacusto.ui.components.GlassTextField(
                value = if (!isValorFocused && uiState.valorInput.isEmpty()) "0,00" else uiState.valorInput,
                onValueChange = { newValue ->
                    // Remove "0,00" se o usuário tentar apagar algo quando o campo acabou de focar
                    val actualNewValue = if (newValue == "0,0" && !isValorFocused) "" else newValue
                    val filtered = actualNewValue.filterIndexed { index, c -> c.isDigit() || c == '.' || c == ',' || (index == 0 && c == '-') }
                    onValorChanged(filtered)
                },
                label = { Text("Valor Total") },
                visualTransformation = CurrencyVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { state -> isValorFocused = state.isFocused },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
            )

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

            if (!uiState.isEditing) {
                com.trilhacusto.ui.components.GlassTextField(
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

            com.trilhacusto.ui.components.GlassCategorySelector(
                categorias = uiState.categorias,
                categoriaSelecionadaId = uiState.categoriaIdSelecionada,
                onCategoriaSelecionada = { onCategoriaChanged(it ?: 0L) },
                onEditarCategoria = { categoriaEditando = it },
                onRemoverCategoria = { onDeletarCategoria(it) },
                onAdicionarCategoria = { showAddCategoriaDialog = true },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Divisão por Pessoa", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                
                val valorNumerico = uiState.valorInput.replace(",", ".").toDoubleOrNull() ?: 0.0
                if (uiState.pessoasNaDivida.size > 1 && valorNumerico > 0) {
                    OutlinedButton(onClick = onDividirIgualmente) {
                        Text("Dividir Igualmente")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            val valorTotalNumerico = uiState.valorInput.replace(",", ".").toDoubleOrNull() ?: 0.0
            val soma = uiState.valoresInput.values.mapNotNull { it.replace(",", ".").toDoubleOrNull() }.sum()
            val diferenca = kotlin.math.abs(valorTotalNumerico - soma)
            val isFechado = kotlin.math.abs(soma - valorTotalNumerico) < 0.01

            if (uiState.pessoasNaDivida.isEmpty()) {
                Text("Nenhuma pessoa adicionada a esta dívida.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                uiState.pessoasNaDivida.forEach { pessoa ->
                    val inputValor = uiState.valoresInput[pessoa.id] ?: ""
                    val isSuggested = uiState.pessoaIdSugestao == pessoa.id
                    val suggestionText = if (isSuggested) uiState.valorSugestao else null
                    
                    com.trilhacusto.ui.components.RateioInputField(
                        label = pessoa.nome,
                        value = inputValor,
                        onValueChange = { novoValor -> onParteChanged(pessoa.id, novoValor) },
                        valorTotal = valorTotalNumerico,
                        suggestionText = suggestionText,
                        onSuggestionClick = if (isSuggested) onAceitarSugestao else null,
                        onRemoveClick = { onRemoverResponsavel(pessoa.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
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
                                    onAdicionarResponsavel(pessoa.id)
                                    expandPessoas = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.pessoasNaDivida.isNotEmpty()) {
                com.trilhacusto.ui.components.FeedbackCard(
                    soma = soma,
                    valorTotal = valorTotalNumerico,
                    diferenca = diferenca,
                    isFechado = isFechado
                )
            }
        }
    }
        com.trilhacusto.ui.components.GlassErrorSnackbar(
            errorMessage = uiState.error,
            onDismiss = onLimparErro,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp)
        )

        if (showDeleteDialog) {
            com.trilhacusto.ui.components.GlassConfirmDialog(
                onDismissRequest = { showDeleteDialog = false },
                onConfirm = {
                    onExcluir()
                    showDeleteDialog = false
                },
                title = "Excluir Transação",
                text = "Tem certeza que deseja excluir permanentemente esta transação? Esta ação não pode ser desfeita.",
                isDestructive = true
            )
        }
    }
    
    if (showAddCategoriaDialog) {
        var nomeCat by remember { mutableStateOf("") }
        GlassConfirmDialog(
            onDismissRequest = { showAddCategoriaDialog = false },
            onConfirm = {
                if (nomeCat.isNotBlank()) {
                    onSalvarCategoria(nomeCat, "#FFFFFF", null)
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
                    onSalvarCategoria(nomeCatEdit, categoriaEditando!!.corHex, categoriaEditando!!.id)
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

class CurrencyVisualTransformation : androidx.compose.ui.text.input.VisualTransformation {
    override fun filter(text: androidx.compose.ui.text.AnnotatedString): androidx.compose.ui.text.input.TransformedText {
        val isNegative = text.text.startsWith("-")
        val prefix = if (isNegative) "-R$ " else "R$ "
        val displayStr = if (isNegative) text.text.substring(1) else text.text
        
        val transformedText = androidx.compose.ui.text.AnnotatedString(prefix + displayStr)
        
        val offsetMapping = object : androidx.compose.ui.text.input.OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (isNegative && offset == 0) return 0
                val adjustment = if (isNegative) prefix.length - 1 else prefix.length
                return offset + adjustment
            }

            override fun transformedToOriginal(offset: Int): Int {
                val prefixLen = prefix.length
                if (offset < prefixLen) return if (isNegative && offset > 0) 1 else 0
                val adjustment = if (isNegative) prefixLen - 1 else prefixLen
                return offset - adjustment
            }
        }
        return androidx.compose.ui.text.input.TransformedText(transformedText, offsetMapping)
    }
}
