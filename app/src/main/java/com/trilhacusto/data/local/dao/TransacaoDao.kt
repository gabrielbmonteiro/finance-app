package com.trilhacusto.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.trilhacusto.data.local.entity.AtribuicaoTransacaoEntity
import com.trilhacusto.data.local.entity.TransacaoEntity
import com.trilhacusto.data.local.relation.TransacaoCompleta
import kotlinx.coroutines.flow.Flow

data class GastoAgrupado(
    val pessoaId: Long,
    val totalGasto: Double
)

@Dao
interface TransacaoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransacao(transacao: TransacaoEntity): Long

    @Update
    suspend fun updateTransacao(transacao: TransacaoEntity)

    @Query("SELECT * FROM transacoes WHERE id = :id LIMIT 1")
    suspend fun getTransacaoById(id: String): TransacaoEntity?

    @Transaction
    @Query("SELECT * FROM transacoes WHERE id = :id LIMIT 1")
    suspend fun getTransacaoCompletaById(id: String): TransacaoCompleta?

    @Transaction
    @Query("SELECT * FROM transacoes WHERE statusAtribuicao IN ('PENDENTE', 'IGNORADO') ORDER BY dataHora DESC")
    fun getTransacoesPendentes(): Flow<List<TransacaoCompleta>>

    @Transaction
    @Query("SELECT * FROM transacoes WHERE statusAtribuicao IN ('PENDENTE', 'IGNORADO') ORDER BY dataHora DESC")
    suspend fun getTransacoesPendentesList(): List<TransacaoCompleta>

    @Query("""
        SELECT a.pessoaId, COALESCE(SUM(a.valorAtribuido), 0.0) as totalGasto
        FROM atribuicoes_transacao a 
        INNER JOIN transacoes t ON a.transacaoId = t.id 
        WHERE t.dataHora BETWEEN :inicioTimestamp AND :fimTimestamp
        GROUP BY a.pessoaId
    """)
    fun getGastosAgrupadosPorPessoa(inicioTimestamp: Long, fimTimestamp: Long): Flow<List<GastoAgrupado>>
    
    @Query("""
        SELECT COALESCE(SUM(a.valorAtribuido), 0.0) 
        FROM atribuicoes_transacao a 
        INNER JOIN transacoes t ON a.transacaoId = t.id 
        WHERE a.pessoaId = :pessoaId 
        AND t.dataHora BETWEEN :inicioTimestamp AND :fimTimestamp
    """)
    fun getGastoTotalPorPessoa(pessoaId: Long, inicioTimestamp: Long, fimTimestamp: Long): Flow<Double>

    @Query("""
        SELECT COALESCE(SUM(valorTotal), 0.0)
        FROM transacoes
        WHERE dataHora BETWEEN :inicioTimestamp AND :fimTimestamp
        AND statusAtribuicao != 'IGNORADO'
    """)
    fun getGastoTotalMes(inicioTimestamp: Long, fimTimestamp: Long): Flow<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAtribuicoes(atribuicoes: List<AtribuicaoTransacaoEntity>)

    @Query("SELECT COUNT(transacaoId) FROM atribuicoes_transacao WHERE pessoaId = :pessoaId")
    suspend fun countAtribuicoesByPessoa(pessoaId: Long): Int

    @Query("SELECT transacaoId FROM atribuicoes_transacao WHERE pessoaId = :pessoaId")
    suspend fun getTransacoesIdsByPessoa(pessoaId: Long): List<String>

    @Query("UPDATE transacoes SET statusAtribuicao = 'PENDENTE' WHERE id IN (:transacoesIds)")
    suspend fun updateStatusParaPendente(transacoesIds: List<String>)

    @Query("DELETE FROM atribuicoes_transacao WHERE transacaoId = :transacaoId")
    suspend fun deleteAtribuicoesDaTransacao(transacaoId: String)

    @Delete
    suspend fun deleteTransacao(transacao: TransacaoEntity)

    @Query("DELETE FROM transacoes WHERE id = :transacaoId")
    suspend fun deleteTransacaoById(transacaoId: String)

    @Transaction
    @Query("""
        SELECT * FROM transacoes 
        WHERE dataHora BETWEEN :inicioTimestamp AND :fimTimestamp 
        AND statusAtribuicao != 'IGNORADO'
        AND (descricaoOriginal LIKE '%' || :queryBusca || '%' 
             OR descricaoCustomizada LIKE '%' || :queryBusca || '%' 
             OR nota LIKE '%' || :queryBusca || '%')
    """)
    fun getTransacoesFiltradas(inicioTimestamp: Long, fimTimestamp: Long, queryBusca: String): Flow<List<TransacaoCompleta>>

    @Transaction
    @Query("""
        SELECT DISTINCT t.* FROM transacoes t
        INNER JOIN atribuicoes_transacao a ON t.id = a.transacaoId
        WHERE t.dataHora BETWEEN :inicioTimestamp AND :fimTimestamp 
        AND t.statusAtribuicao != 'IGNORADO'
        AND a.pessoaId IN (:pessoasIds)
        ORDER BY t.dataHora DESC
    """)
    suspend fun getTransacoesCompletasFiltradasPorDataEPessoas(
        inicioTimestamp: Long, 
        fimTimestamp: Long, 
        pessoasIds: List<Long>
    ): List<TransacaoCompleta>

    @Transaction
    @Query("""
        SELECT * FROM transacoes 
        WHERE dataHora BETWEEN :inicioTimestamp AND :fimTimestamp 
        AND statusAtribuicao != 'IGNORADO'
        ORDER BY dataHora DESC
    """)
    suspend fun getTransacoesCompletasDoMes(inicioTimestamp: Long, fimTimestamp: Long): List<TransacaoCompleta>
}
