package com.trilhacusto.ui.export

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trilhacusto.ui.components.GlassCard
import com.trilhacusto.ui.components.GlassErrorSnackbar
import com.trilhacusto.ui.theme.GreenPositive
import com.trilhacusto.ui.theme.PrimaryAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    viewModel: ExportViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var showSuccessModal by remember { mutableStateOf(false) }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(if (uiState.formato == ExportFormat.PDF) "application/pdf" else "image/jpeg")
    ) { uri: Uri? ->
        uri?.let {
            viewModel.exportar(context, isShare = false, targetUri = it)
        }
    }

    LaunchedEffect(uiState.exportSuccessUri) {
        uiState.exportSuccessUri?.let { uri ->
            if (uiState.isShare) {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = if (uiState.formato == ExportFormat.PDF) "application/pdf" else "image/jpeg"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Compartilhar Fatura"))
            } else {
                showSuccessModal = true
            }
            viewModel.limparMensagem()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exportar Fatura", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Filtros (Glass Card)
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Quem incluir no extrato?",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item {
                                FilterChipCustom(
                                    text = "Todas",
                                    isSelected = uiState.pessoasSelecionadas.isEmpty(),
                                    onClick = { viewModel.selecionarTodasPessoas() }
                                )
                            }
                            items(uiState.pessoas) { pessoa ->
                                FilterChipCustom(
                                    text = pessoa.nome,
                                    isSelected = uiState.pessoasSelecionadas.contains(pessoa.id),
                                    onClick = { viewModel.togglePessoa(pessoa.id) }
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Formato (Glass Card)
                GlassCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Formato do arquivo",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FormatOption(
                                text = "PDF",
                                isSelected = uiState.formato == ExportFormat.PDF,
                                onClick = { viewModel.setFormato(ExportFormat.PDF) },
                                modifier = Modifier.weight(1f)
                            )
                            FormatOption(
                                text = "JPG",
                                isSelected = uiState.formato == ExportFormat.JPG,
                                onClick = { viewModel.setFormato(ExportFormat.JPG) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Rodapé (Ações)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { 
                            val sdf = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault())
                            val dateStr = sdf.format(java.util.Date())
                            val ext = if (uiState.formato == ExportFormat.PDF) ".pdf" else ".jpg"
                            val filename = "Fatura_TrilhaCusto_$dateStr$ext"
                            createDocumentLauncher.launch(filename)
                        },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f))
                    ) {
                        Text("BAIXAR", color = MaterialTheme.colorScheme.onPrimary)
                    }
                    
                    Button(
                        onClick = { viewModel.exportar(context, isShare = true) },
                        modifier = Modifier.weight(1f).height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "COMPARTILHAR", 
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryAccent)
                }
            }
            
            GlassErrorSnackbar(
                errorMessage = uiState.errorMessage,
                onDismiss = { viewModel.limparMensagem() },
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp)
            )

            if (showSuccessModal) {
                androidx.compose.ui.window.Dialog(onDismissRequest = { showSuccessModal = false }) {
                    GlassCard(
                        containerColor = Color.Black.copy(alpha = 0.6f)
                    ) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GreenPositive, modifier = Modifier.size(48.dp))
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Arquivo salvo com sucesso!", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipCustom(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) PrimaryAccent else Color.White.copy(alpha = 0.1f)
    val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
    
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text, 
            color = textColor, 
            fontWeight = FontWeight.SemiBold, 
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun FormatOption(text: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val borderColor = if (isSelected) PrimaryAccent else Color.White.copy(alpha = 0.2f)
    val bgColor = if (isSelected) PrimaryAccent.copy(alpha = 0.1f) else Color.Transparent
    
    Box(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            if (isSelected) {
                Icon(Icons.Default.Check, contentDescription = null, tint = PrimaryAccent, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = text, 
                color = MaterialTheme.colorScheme.onPrimary, 
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
