package com.narrapay.data.local.dao

import androidx.room.*
import com.narrapay.data.local.entity.AtribuicaoTransacaoEntity
import com.narrapay.data.local.entity.TransacaoEntity
import com.narrapay.data.local.relation.TransacaoCompleta
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
    @Query("SELECT * FROM transacoes WHERE statusAtribuicao IN ('PENDENTE', 'IGNORADO')")
    fun getTransacoesPendentes(): Flow<List<TransacaoCompleta>>

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
}
