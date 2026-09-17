package com.trilhacusto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.trilhacusto.ui.theme.GlassBorder
import com.trilhacusto.ui.theme.GreenPositive
import com.trilhacusto.ui.theme.NavySecondary
import com.trilhacusto.ui.theme.PurpleSecondary
import com.trilhacusto.ui.theme.RedError
import com.trilhacusto.ui.transacao.CurrencyVisualTransformation
import com.trilhacusto.util.toCurrencyString

@Composable
fun RateioInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    valorTotal: Double,
    suggestionText: String? = null,
    onSuggestionClick: (() -> Unit)? = null,
    onRemoveClick: (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.weight(1f)) {
                GlassTextField(
                    value = value,
                    onValueChange = { newValue ->
                        val filtered = newValue.filterIndexed { index, c -> c.isDigit() || c == '.' || c == ',' || (index == 0 && c == '-') }
                        onValueChange(filtered)
                    },
                    label = { Text(label) },
                    visualTransformation = CurrencyVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            }
            if (onRemoveClick != null) {
                IconButton(onClick = onRemoveClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Remover", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AtalhoRateio(texto = "0%", onClick = { onValueChange("0") })
            AtalhoRateio(texto = "50%", onClick = { 
                val metade = valorTotal / 2
                onValueChange(String.format(java.util.Locale.US, "%.2f", metade)) 
            })
            AtalhoRateio(texto = "100%", onClick = { 
                onValueChange(String.format(java.util.Locale.US, "%.2f", valorTotal)) 
            })
            
            if (suggestionText != null && onSuggestionClick != null) {
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .shadow(4.dp, RoundedCornerShape(percent = 50))
                        .clip(RoundedCornerShape(percent = 50))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(NavySecondary, PurpleSecondary)
                            )
                        )
                        .border(1.dp, GlassBorder, RoundedCornerShape(percent = 50))
                        .clickable { onSuggestionClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("✨ R$ $suggestionText?", fontWeight = FontWeight.Bold, color = androidx.compose.ui.graphics.Color.White)
                }
            }
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

    GlassCard(
        containerColor = cardColor,
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
