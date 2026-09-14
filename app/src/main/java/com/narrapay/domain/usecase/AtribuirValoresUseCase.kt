package com.narrapay.domain.usecase

import com.narrapay.data.local.entity.AtribuicaoTransacaoEntity
import com.narrapay.data.local.relation.TransacaoCompleta
import com.narrapay.domain.exception.AtribuicaoDivergenteException
import com.narrapay.domain.repository.TransacaoRepository
import kotlin.math.abs

import java.math.BigDecimal
import java.math.RoundingMode

class AtribuirValoresUseCase(
    private val repository: TransacaoRepository
) {
    suspend operator fun invoke(
        transacaoCompleta: TransacaoCompleta?,
        novasAtribuicoes: Map<Long, Double>
    ) {
        if (transacaoCompleta == null || transacaoCompleta.transacao.id.isBlank()) {
            throw IllegalArgumentException("Transação inválida ou inexistente.")
        }
        
        val transacao = transacaoCompleta.transacao
        

        if (novasAtribuicoes.values.any { it < 0.0 }) {
            throw IllegalArgumentException("Não é permitido atribuir valores negativos.")
        }


        val somaAtribuicoes = novasAtribuicoes.values
            .map { BigDecimal(it.toString()) }
            .fold(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP)
            
        val valorTotalBD = BigDecimal(transacao.valorTotal.toString())
            .setScale(2, RoundingMode.HALF_UP)


        if (somaAtribuicoes.compareTo(BigDecimal.ZERO) == 0) {
            val novoStatus = if (transacao.statusAtribuicao == "IGNORADO") "IGNORADO" else "PENDENTE"
            val transacaoAtualizada = transacao.copy(statusAtribuicao = novoStatus)
            repository.atribuirRateio(
                transacaoId = transacao.id,
                transacaoAtualizada = transacaoAtualizada,
                novasAtribuicoes = emptyList()
            )
            return
        }

        if (somaAtribuicoes.compareTo(valorTotalBD) != 0) {
            throw AtribuicaoDivergenteException(
                "A soma das atribuições ($somaAtribuicoes) não bate com o valor total da transação ($valorTotalBD)."
            )
        }

        val listaAtribuicoes = novasAtribuicoes.map { (pessoaId, valor) ->
            AtribuicaoTransacaoEntity(
                transacaoId = transacao.id,
                pessoaId = pessoaId,
                valorAtribuido = valor
            )
        }

        val transacaoAtualizada = transacao.copy(statusAtribuicao = "TOTALMENTE_ATRIBUIDO")

        repository.atribuirRateio(
            transacaoId = transacao.id,
            transacaoAtualizada = transacaoAtualizada,
            novasAtribuicoes = listaAtribuicoes
        )
    }
}
