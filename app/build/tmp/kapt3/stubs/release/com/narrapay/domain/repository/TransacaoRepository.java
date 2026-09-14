package com.narrapay.domain.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\t\bf\u0018\u00002\u00020\u0001J,\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00a6@\u00a2\u0006\u0002\u0010\u000bJ\u0016\u0010\f\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00110\t0\u0010H&J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\tH\u00a6@\u00a2\u0006\u0002\u0010\u0014J,\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\t0\u00102\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u001a\u001a\u00020\u0005H&J\u0016\u0010\u001b\u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\u000eJ\u0016\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u001dJ\u0018\u0010\u001e\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u001f\u001a\u00020\u0005H\u00a6@\u00a2\u0006\u0002\u0010\u001dJ\u0016\u0010 \u001a\u00020\u00032\u0006\u0010\r\u001a\u00020\u0007H\u00a6@\u00a2\u0006\u0002\u0010\u000e\u00a8\u0006!"}, d2 = {"Lcom/narrapay/domain/repository/TransacaoRepository;", "", "atribuirRateio", "", "transacaoId", "", "transacaoAtualizada", "Lcom/narrapay/data/local/entity/TransacaoEntity;", "novasAtribuicoes", "", "Lcom/narrapay/data/local/entity/AtribuicaoTransacaoEntity;", "(Ljava/lang/String;Lcom/narrapay/data/local/entity/TransacaoEntity;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "atualizarTransacao", "transacao", "(Lcom/narrapay/data/local/entity/TransacaoEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buscarCategorias", "Lkotlinx/coroutines/flow/Flow;", "Lcom/narrapay/data/local/entity/CategoriaEntity;", "buscarRegrasAutomacao", "Lcom/narrapay/data/local/entity/RegraAutomacaoEntity;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buscarTransacoesFiltradas", "Lcom/narrapay/data/local/relation/TransacaoCompleta;", "inicioTimestamp", "", "fimTimestamp", "queryBusca", "deletarTransacao", "deletarTransacaoPorId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTransacaoById", "id", "salvarTransacao", "app_release"})
public abstract interface TransacaoRepository {
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object salvarTransacao(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacao, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object atualizarTransacao(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacao, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object buscarRegrasAutomacao(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.narrapay.data.local.entity.RegraAutomacaoEntity>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTransacaoById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.narrapay.data.local.entity.TransacaoEntity> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.narrapay.data.local.entity.CategoriaEntity>> buscarCategorias();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object atribuirRateio(@org.jetbrains.annotations.NotNull()
    java.lang.String transacaoId, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacaoAtualizada, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.entity.AtribuicaoTransacaoEntity> novasAtribuicoes, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deletarTransacao(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacao, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deletarTransacaoPorId(@org.jetbrains.annotations.NotNull()
    java.lang.String transacaoId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.narrapay.data.local.relation.TransacaoCompleta>> buscarTransacoesFiltradas(long inicioTimestamp, long fimTimestamp, @org.jetbrains.annotations.NotNull()
    java.lang.String queryBusca);
}