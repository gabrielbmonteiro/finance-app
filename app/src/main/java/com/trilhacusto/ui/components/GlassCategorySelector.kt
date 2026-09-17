package com.trilhacusto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trilhacusto.data.local.entity.CategoriaEntity
import com.trilhacusto.ui.theme.GlassBackgroundFaint
import com.trilhacusto.ui.theme.PrimaryAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassCategorySelector(
    categorias: List<CategoriaEntity>,
    categoriaSelecionadaId: Long?,
    onCategoriaSelecionada: (Long?) -> Unit,
    onEditarCategoria: (CategoriaEntity) -> Unit,
    onRemoverCategoria: (CategoriaEntity) -> Unit,
    onAdicionarCategoria: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    val nomeCategoriaSelecionada = categorias.find { it.id == categoriaSelecionadaId }?.nome ?: "Sem Categoria"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        GlassTextField(
            value = nomeCategoriaSelecionada,
            onValueChange = {},
            readOnly = true,
            label = { Text("Categoria") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        
        // Custom styling for the dropdown menu
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                surface = com.trilhacusto.ui.theme.BackgroundGradientStart,
                onSurface = com.trilhacusto.ui.theme.TextPrimary
            )
        ) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(GlassBackgroundFaint)
            ) {
                DropdownMenuItem(
                    text = { Text("Sem Categoria") },
                    onClick = {
                        onCategoriaSelecionada(null)
                        expanded = false
                    }
                )
                categorias.forEach { cat ->
                    DropdownMenuItem(
                        text = { 
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                Text(cat.nome, modifier = Modifier.weight(1f))
                                IconButton(onClick = { 
                                    onEditarCategoria(cat)
                                    expanded = false
                                }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = PrimaryAccent)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = { 
                                    onRemoverCategoria(cat)
                                    // Don't close so they can see it was removed
                                }, modifier = Modifier.size(24.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        },
                        onClick = {
                            onCategoriaSelecionada(cat.id)
                            expanded = false
                        }
                    )
                }
                Divider(color = com.trilhacusto.ui.theme.GlassBorder)
                DropdownMenuItem(
                    text = { Text("Adicionar Nova Categoria Rápida...", color = PrimaryAccent, fontWeight = FontWeight.Bold) },
                    leadingIcon = { Icon(Icons.Default.Add, contentDescription = null, tint = PrimaryAccent) },
                    onClick = {
                        expanded = false
                        onAdicionarCategoria()
                    }
                )
            }
        }
    }
}
