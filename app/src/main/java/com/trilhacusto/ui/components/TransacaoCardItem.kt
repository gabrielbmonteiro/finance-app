package com.trilhacusto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trilhacusto.data.local.relation.TransacaoCompleta
import com.trilhacusto.util.formatDateWithYearIfNeeded
import com.trilhacusto.util.toCurrencyString

@Composable
fun TransacaoCardItem(
    transacaoCompleta: TransacaoCompleta,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onExcluir: (() -> Unit)? = null,
    filtroPessoaId: Long? = null,
    isPrivacyModeEnabled: Boolean = false
) {
    val transacao = transacaoCompleta.transacao
    
    val baseTitulo = transacao.descricaoCustomizada ?: transacao.descricaoOriginal
    
    val parcelasStr = if (transacao.numeroParcela != null && transacao.totalParcelas != null) {
        " (${transacao.numeroParcela}/${transacao.totalParcelas})"
    } else ""
    
    val titulo = baseTitulo + parcelasStr
    val dataFormatada = transacao.dataHora.formatDateWithYearIfNeeded(includeTime = true)

    val temAtribuicoes = transacaoCompleta.atribuicoes.isNotEmpty()
    val isIgnorado = transacao.statusAtribuicao == "IGNORADO"
    
    val atribuicaoDoFiltro = if (filtroPessoaId != null) {
        transacaoCompleta.atribuicoes.find { it.pessoa.id == filtroPessoaId }
    } else null
    val valorPrincipal = atribuicaoDoFiltro?.atribuicao?.valorAtribuido ?: transacao.valorTotal
    
    com.trilhacusto.ui.components.GlassCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isIgnorado) Icons.Default.Clear else Icons.Default.ShoppingCart,
                        contentDescription = "Ícone",
                        tint = if (isIgnorado) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = titulo,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                    Text(
                        text = dataFormatada,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = valorPrincipal.toCurrencyString(isPrivacyModeEnabled),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (atribuicaoDoFiltro != null) {
                        Text(
                            text = "Total: ${transacao.valorTotal.toCurrencyString(isPrivacyModeEnabled)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
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
                    "${it.pessoa.nome}: ${it.atribuicao.valorAtribuido.toCurrencyString(isPrivacyModeEnabled)}"
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
