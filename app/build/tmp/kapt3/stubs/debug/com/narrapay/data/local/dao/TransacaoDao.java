package com.narrapay.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\'J&\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\'J$\u0010\u0014\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00150\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\'J\u0018\u0010\u0017\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0018\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0019\u001a\u0004\u0018\u00010\u001a2\u0006\u0010\u0018\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J,\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00150\r2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u0005H\'J\u0014\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00150\rH\'J\u001c\u0010\u001e\u001a\u00020\u00032\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020 0\u0015H\u00a7@\u00a2\u0006\u0002\u0010!J\u0016\u0010\"\u001a\u00020\u00102\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010#\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\n\u00a8\u0006$"}, d2 = {"Lcom/narrapay/data/local/dao/TransacaoDao;", "", "deleteAtribuicoesDaTransacao", "", "transacaoId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTransacao", "transacao", "Lcom/narrapay/data/local/entity/TransacaoEntity;", "(Lcom/narrapay/data/local/entity/TransacaoEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTransacaoById", "getGastoTotalMes", "Lkotlinx/coroutines/flow/Flow;", "", "inicioTimestamp", "", "fimTimestamp", "getGastoTotalPorPessoa", "pessoaId", "getGastosAgrupadosPorPessoa", "", "Lcom/narrapay/data/local/dao/GastoAgrupado;", "getTransacaoById", "id", "getTransacaoCompletaById", "Lcom/narrapay/data/local/relation/TransacaoCompleta;", "getTransacoesFiltradas", "queryBusca", "getTransacoesPendentes", "insertAtribuicoes", "atribuicoes", "Lcom/narrapay/data/local/entity/AtribuicaoTransacaoEntity;", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertTransacao", "updateTransacao", "app_debug"})
@androidx.room.Dao()
public abstract interface TransacaoDao {
    
    @androidx.room.Insert(onConflict = 5)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertTransacao(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacao, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateTransacao(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacao, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM transacoes WHERE id = :id LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTransacaoById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.narrapay.data.local.entity.TransacaoEntity> $completion);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "SELECT * FROM transacoes WHERE id = :id LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTransacaoCompletaById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.narrapay.data.local.relation.TransacaoCompleta> $completion);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "SELECT * FROM transacoes WHERE statusAtribuicao IN (\'PENDENTE\', \'IGNORADO\')")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.narrapay.data.local.relation.TransacaoCompleta>> getTransacoesPendentes();
    
    @androidx.room.Query(value = "\n        SELECT a.pessoaId, COALESCE(SUM(a.valorAtribuido), 0.0) as totalGasto\n        FROM atribuicoes_transacao a \n        INNER JOIN transacoes t ON a.transacaoId = t.id \n        WHERE t.dataHora BETWEEN :inicioTimestamp AND :fimTimestamp\n        GROUP BY a.pessoaId\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.narrapay.data.local.dao.GastoAgrupado>> getGastosAgrupadosPorPessoa(long inicioTimestamp, long fimTimestamp);
    
    @androidx.room.Query(value = "\n        SELECT COALESCE(SUM(a.valorAtribuido), 0.0) \n        FROM atribuicoes_transacao a \n        INNER JOIN transacoes t ON a.transacaoId = t.id \n        WHERE a.pessoaId = :pessoaId \n        AND t.dataHora BETWEEN :inicioTimestamp AND :fimTimestamp\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Double> getGastoTotalPorPessoa(long pessoaId, long inicioTimestamp, long fimTimestamp);
    
    @androidx.room.Query(value = "\n        SELECT COALESCE(SUM(valorTotal), 0.0)\n        FROM transacoes\n        WHERE dataHora BETWEEN :inicioTimestamp AND :fimTimestamp\n        AND statusAtribuicao != \'IGNORADO\'\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Double> getGastoTotalMes(long inicioTimestamp, long fimTimestamp);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAtribuicoes(@org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.entity.AtribuicaoTransacaoEntity> atribuicoes, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM atribuicoes_transacao WHERE transacaoId = :transacaoId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAtribuicoesDaTransacao(@org.jetbrains.annotations.NotNull()
    java.lang.String transacaoId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTransacao(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacao, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM transacoes WHERE id = :transacaoId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteTransacaoById(@org.jetbrains.annotations.NotNull()
    java.lang.String transacaoId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Transaction()
    @androidx.room.Query(value = "\n        SELECT * FROM transacoes \n        WHERE dataHora BETWEEN :inicioTimestamp AND :fimTimestamp \n        AND statusAtribuicao != \'IGNORADO\'\n        AND (descricaoOriginal LIKE \'%\' || :queryBusca || \'%\' \n             OR descricaoCustomizada LIKE \'%\' || :queryBusca || \'%\' \n             OR nota LIKE \'%\' || :queryBusca || \'%\')\n    ")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.narrapay.data.local.relation.TransacaoCompleta>> getTransacoesFiltradas(long inicioTimestamp, long fimTimestamp, @org.jetbrains.annotations.NotNull()
    java.lang.String queryBusca);
}