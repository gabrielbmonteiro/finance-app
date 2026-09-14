package com.narrapay.domain.repository

import com.narrapay.data.local.entity.AtribuicaoTransacaoEntity
import com.narrapay.data.local.entity.CategoriaEntity
import com.narrapay.data.local.entity.RegraAutomacaoEntity
import com.narrapay.data.local.entity.TransacaoEntity

import com.narrapay.data.local.relation.TransacaoCompleta
import kotlinx.coroutines.flow.Flow

interface TransacaoRepository {
    suspend fun salvarTransacao(transacao: TransacaoEntity)
    suspend fun atualizarTransacao(transacao: TransacaoEntity)
    suspend fun buscarRegrasAutomacao(): List<RegraAutomacaoEntity>
    suspend fun getTransacaoById(id: String): TransacaoEntity?
    fun buscarCategorias(): Flow<List<CategoriaEntity>>
    suspend fun atribuirRateio(
        transacaoId: String,
        transacaoAtualizada: TransacaoEntity,
        novasAtribuicoes: List<AtribuicaoTransacaoEntity>
    )
    
    suspend fun deletarTransacao(transacao: TransacaoEntity)
    suspend fun deletarTransacaoPorId(transacaoId: String)
    fun buscarTransacoesFiltradas(inicioTimestamp: Long, fimTimestamp: Long, queryBusca: String): kotlinx.coroutines.flow.Flow<List<com.narrapay.data.local.relation.TransacaoCompleta>>
}
