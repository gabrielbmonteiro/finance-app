package com.narrapay.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltrosBottomSheet(
    onDismissRequest: () -> Unit,
    onApplyFilters: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Filtros Avançados", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Filtros de Data, Categoria, e Pessoa virão aqui.", color = MaterialTheme.colorScheme.onSurface.copy(alpha=0.6f))
            
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { 
                    onApplyFilters()
                    onDismissRequest() 
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Aplicar Filtros")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
