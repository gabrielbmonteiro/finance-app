package com.trilhacusto.data.repository

import androidx.room.withTransaction
import com.trilhacusto.data.local.AppDatabase
import com.trilhacusto.data.local.dao.CategoriaDao
import com.trilhacusto.data.local.dao.RegraAutomacaoDao
import com.trilhacusto.data.local.dao.TransacaoDao
import com.trilhacusto.data.local.entity.AtribuicaoTransacaoEntity
import com.trilhacusto.data.local.entity.RegraAutomacaoEntity
import com.trilhacusto.data.local.entity.TransacaoEntity
import com.trilhacusto.domain.repository.TransacaoRepository

class TransacaoRepositoryImpl(
    private val transacaoDao: TransacaoDao,
    private val regraAutomacaoDao: RegraAutomacaoDao,
    private val categoriaDao: CategoriaDao,
    private val database: AppDatabase
) : TransacaoRepository {

    override suspend fun salvarTransacao(transacao: TransacaoEntity) {
        transacaoDao.insertTransacao(transacao)
    }

    override suspend fun salvarTransacaoComAtribuicoes(transacao: TransacaoEntity, atribuicoes: List<AtribuicaoTransacaoEntity>) {
        database.withTransaction {
            transacaoDao.insertTransacao(transacao)
            transacaoDao.insertAtribuicoes(atribuicoes)
        }
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

    override suspend fun getTransacaoCompletaById(id: String): com.trilhacusto.data.local.relation.TransacaoCompleta? {
        return transacaoDao.getTransacaoCompletaById(id)
    }

    override fun buscarCategorias(): kotlinx.coroutines.flow.Flow<List<com.trilhacusto.data.local.entity.CategoriaEntity>> {
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
    ): kotlinx.coroutines.flow.Flow<List<com.trilhacusto.data.local.relation.TransacaoCompleta>> {
        return transacaoDao.getTransacoesFiltradas(inicioTimestamp, fimTimestamp, queryBusca)
    }

    override suspend fun salvarCategoria(categoria: com.trilhacusto.data.local.entity.CategoriaEntity) {
        categoriaDao.insert(categoria)
    }

    override suspend fun deletarCategoria(categoria: com.trilhacusto.data.local.entity.CategoriaEntity) {
        categoriaDao.delete(categoria)
    }

    override suspend fun countAtribuicoesDaPessoa(pessoaId: Long): Int {
        return transacaoDao.countAtribuicoesByPessoa(pessoaId)
    }

    override suspend fun reverterTransacoesDaPessoa(pessoaId: Long) {
        val transacoesAfetadas = transacaoDao.getTransacoesIdsByPessoa(pessoaId)
        if (transacoesAfetadas.isNotEmpty()) {
            transacaoDao.updateStatusParaPendente(transacoesAfetadas)
        }
    }

}
