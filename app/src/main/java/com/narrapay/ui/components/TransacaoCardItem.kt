package com.narrapay.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.narrapay.data.local.relation.TransacaoCompleta
import com.narrapay.util.toCurrencyString
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete

@Composable
fun TransacaoCardItem(
    transacaoCompleta: TransacaoCompleta,
    onClick: () -> Unit,
    onExcluir: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val transacao = transacaoCompleta.transacao
    val categoria = transacaoCompleta.categoria
    
    val baseTitulo = transacao.descricaoCustomizada ?: transacao.descricaoOriginal
    
    val parcelasStr = if (transacao.numeroParcela != null && transacao.totalParcelas != null) {
        " (${transacao.numeroParcela}/${transacao.totalParcelas})"
    } else ""
    
    val titulo = baseTitulo + parcelasStr
    
    val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale("pt", "BR"))
    val dataFormatada = sdf.format(Date(transacao.dataHora))

    val temAtribuicoes = transacaoCompleta.atribuicoes.isNotEmpty()
    
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = dataFormatada,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = transacao.valorTotal.toCurrencyString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (onExcluir != null) {
                        IconButton(onClick = onExcluir, modifier = Modifier.size(32.dp)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Excluir",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            if (temAtribuicoes) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(8.dp))
                
                val textoRateio = transacaoCompleta.atribuicoes.joinToString(" / ") {
                    "${it.pessoa.nome}: ${it.atribuicao.valorAtribuido.toCurrencyString()}"
                }
                
                Text(
                    text = textoRateio,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
