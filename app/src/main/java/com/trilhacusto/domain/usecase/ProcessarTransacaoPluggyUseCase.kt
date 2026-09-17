package com.trilhacusto.domain.usecase

import com.trilhacusto.data.local.entity.TransacaoEntity
import com.trilhacusto.domain.repository.TransacaoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class TransacaoPluggyDto(
    val id: String,
    val amount: Double,
    val description: String,
    val date: Long,
    val numeroParcela: Int? = null,
    val totalParcelas: Int? = null
)

class ProcessarTransacaoPluggyUseCase(
    private val repository: TransacaoRepository
) {
    suspend operator fun invoke(transacoesDto: List<TransacaoPluggyDto>) {
        withContext(Dispatchers.IO) {
            val regras = repository.buscarRegrasAutomacao()

            for (dto in transacoesDto) {
                val existente = repository.getTransacaoById(dto.id)

                if (existente != null) {
                    val atualizada = existente.copy(
                        valorTotal = dto.amount,
                        dataHora = dto.date,

                        descricaoOriginal = dto.description,
                        numeroParcela = dto.numeroParcela,
                        totalParcelas = dto.totalParcelas
                    )
                    repository.atualizarTransacao(atualizada)
                } else {
                    var categoriaIdPadrao: Long? = null

                    for (regra in regras) {
                        if (dto.description.contains(regra.termoBusca, ignoreCase = true)) {
                            categoriaIdPadrao = regra.categoriaIdPadrao
                            break
                        }
                    }

                    val transacaoEntity = TransacaoEntity(
                        id = dto.id,
                        valorTotal = dto.amount,
                        descricaoOriginal = dto.description,
                        dataHora = dto.date,
                        categoriaId = categoriaIdPadrao,
                        statusAtribuicao = "PENDENTE",
                        numeroParcela = dto.numeroParcela,
                        totalParcelas = dto.totalParcelas
                    )

                    repository.salvarTransacao(transacaoEntity)
                }
            }
        }
    }
}
