package com.trilhacusto.ui.ajustes.pessoas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Banner explicativo do recurso de Favoritar
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = Color(0xFF131525),
                    border = BorderStroke(1.dp, Color(0xFF252845)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFB800),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Divisão Automática",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Pessoas favoritadas (★) são adicionadas na Divisão por Pessoa automaticamente.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }

            items(pessoas, key = { it.id }) { pessoa ->
                val pColor = try {
                    Color(android.graphics.Color.parseColor(pessoa.corHex))
                } catch (e: Exception) {
                    MaterialTheme.colorScheme.primary
                }

                ListItem(
                    headlineContent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = pessoa.nome,
                                fontWeight = if (pessoa.isFavorito) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    },
                    leadingContent = {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(pColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = pessoa.nome.take(1).uppercase(),
                                fontWeight = FontWeight.ExtraBold,
                                color = pColor
                            )
                        }
                    },
                    trailingContent = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Botão de Favoritar
                            IconButton(onClick = { viewModel.toggleFavorito(pessoa) }) {
                                Icon(
                                    imageVector = if (pessoa.isFavorito) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = if (pessoa.isFavorito) "Desfavoritar" else "Favoritar",
                                    tint = if (pessoa.isFavorito) Color(0xFFFFB800) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                )
                            }

                            // Botão de Editar
                            IconButton(onClick = {
                                pessoaEditando = pessoa
                                showDialog = true
                            }) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Botão de Excluir
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    hasDebts = viewModel.checkHasDebts(pessoa.id)
                                    pessoaToDelete = pessoa
                                    showDeleteDialog = true
                                }
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Excluir",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
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
    var isFavorito by remember { mutableStateOf(pessoa?.isFavorito ?: false) }

    com.trilhacusto.ui.components.GlassConfirmDialog(
        onDismissRequest = onDismiss,
        onConfirm = {
            if (nome.isNotBlank()) {
                val coresVibrantes = listOf(
                    "#E91E63",
                    "#9C27B0",
                    "#3F51B5",
                    "#2196F3",
                    "#00BCD4",
                    "#009688",
                    "#4CAF50",
                    "#FF9800",
                    "#FF5722"
                )
                val availableColors = coresVibrantes.filter { it !in existingColors }
                val corPadrao =
                    if (availableColors.isNotEmpty()) availableColors.random() else coresVibrantes.random()
                onSave(
                    PessoaEntity(
                        id = pessoa?.id ?: 0L,
                        nome = nome,
                        corHex = pessoa?.corHex ?: corPadrao,
                        isFavorito = isFavorito
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                // Opção de favoritar dentro do modal
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { isFavorito = !isFavorito },
                    color = if (isFavorito) Color(0xFF282312) else Color(0xFF161726),
                    border = BorderStroke(
                        1.dp,
                        if (isFavorito) Color(0xFFFFB800).copy(alpha = 0.5f) else Color(0xFF282A45)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isFavorito) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null,
                            tint = if (isFavorito) Color(0xFFFFB800) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Favoritar para divisão automática",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isFavorito) Color(0xFFFFE082) else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Incluir esta pessoa automaticamente ao ratear pendências",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = if (isFavorito) Color(0xFFFFE082).copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    )
}
