package com.narrapay.data.repository

import com.narrapay.data.local.AppDatabase
import androidx.room.withTransaction
import com.narrapay.data.local.dao.RegraAutomacaoDao
import com.narrapay.data.local.dao.TransacaoDao
import com.narrapay.data.local.dao.CategoriaDao
import com.narrapay.data.local.entity.AtribuicaoTransacaoEntity
import com.narrapay.data.local.entity.RegraAutomacaoEntity
import com.narrapay.data.local.entity.TransacaoEntity
import com.narrapay.domain.repository.TransacaoRepository

class TransacaoRepositoryImpl(
    private val transacaoDao: TransacaoDao,
    private val regraAutomacaoDao: RegraAutomacaoDao,
    private val categoriaDao: CategoriaDao,
    private val database: AppDatabase
) : TransacaoRepository {

    override suspend fun salvarTransacao(transacao: TransacaoEntity) {
        transacaoDao.insertTransacao(transacao)
    }

    override suspend fun atualizarTransacao(transacao: TransacaoEntity) {
        transacaoDao.updateTransacao(transacao)
    }

    override suspend fun buscarRegrasAutomacao(): List<RegraAutomacaoEntity> {
        return regraAutomacaoDao.getAllRegrasSync()
    }

    override suspend fun getTransacaoById(id: String): TransacaoEntity? {
        return transacaoDao.getTransacaoById(id)
    }

    override fun buscarCategorias(): kotlinx.coroutines.flow.Flow<List<com.narrapay.data.local.entity.CategoriaEntity>> {
        return categoriaDao.getAll()
    }

    override suspend fun atribuirRateio(
        transacaoId: String,
        transacaoAtualizada: TransacaoEntity,
        novasAtribuicoes: List<AtribuicaoTransacaoEntity>
    ) {
        database.withTransaction {
            transacaoDao.deleteAtribuicoesDaTransacao(transacaoId)
            transacaoDao.insertAtribuicoes(novasAtribuicoes)
            transacaoDao.updateTransacao(transacaoAtualizada)
        }
    }

    override suspend fun deletarTransacao(transacao: TransacaoEntity) {
        transacaoDao.deleteTransacao(transacao)
    }

    override suspend fun deletarTransacaoPorId(transacaoId: String) {
        transacaoDao.deleteTransacaoById(transacaoId)
    }

    override fun buscarTransacoesFiltradas(
        inicioTimestamp: Long,
        fimTimestamp: Long,
        queryBusca: String
    ): kotlinx.coroutines.flow.Flow<List<com.narrapay.data.local.relation.TransacaoCompleta>> {
        return transacaoDao.getTransacoesFiltradas(inicioTimestamp, fimTimestamp, queryBusca)
    }
}
