package com.trilhacusto.domain.repository

import com.trilhacusto.data.local.entity.AtribuicaoTransacaoEntity
import com.trilhacusto.data.local.entity.CategoriaEntity
import com.trilhacusto.data.local.entity.RegraAutomacaoEntity
import com.trilhacusto.data.local.entity.TransacaoEntity

import com.trilhacusto.data.local.relation.TransacaoCompleta
import kotlinx.coroutines.flow.Flow

interface TransacaoRepository {
    suspend fun salvarTransacao(transacao: TransacaoEntity)
    suspend fun salvarTransacaoComAtribuicoes(transacao: TransacaoEntity, atribuicoes: List<AtribuicaoTransacaoEntity>)
    suspend fun atualizarTransacao(transacao: TransacaoEntity)
    suspend fun buscarRegrasAutomacao(): List<RegraAutomacaoEntity>
    suspend fun getTransacaoById(id: String): TransacaoEntity?
    suspend fun getTransacaoCompletaById(id: String): TransacaoCompleta?
    fun buscarCategorias(): Flow<List<CategoriaEntity>>
    suspend fun atribuirRateio(
        transacaoId: String,
        transacaoAtualizada: TransacaoEntity,
        novasAtribuicoes: List<AtribuicaoTransacaoEntity>
    )
    
    suspend fun deletarTransacao(transacao: TransacaoEntity)
    suspend fun deletarTransacaoPorId(transacaoId: String)
    suspend fun countAtribuicoesDaPessoa(pessoaId: Long): Int
    suspend fun reverterTransacoesDaPessoa(pessoaId: Long)
    fun buscarTransacoesFiltradas(inicioTimestamp: Long, fimTimestamp: Long, queryBusca: String): kotlinx.coroutines.flow.Flow<List<com.trilhacusto.data.local.relation.TransacaoCompleta>>
    suspend fun salvarCategoria(categoria: CategoriaEntity)
    suspend fun deletarCategoria(categoria: CategoriaEntity)
}
