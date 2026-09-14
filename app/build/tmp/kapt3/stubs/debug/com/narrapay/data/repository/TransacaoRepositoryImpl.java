package com.narrapay.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\t\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ,\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012H\u0096@\u00a2\u0006\u0002\u0010\u0014J\u0016\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0017J\u0014\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001a0\u00120\u0019H\u0016J\u0014\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u0012H\u0096@\u00a2\u0006\u0002\u0010\u001dJ,\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001f0\u00120\u00192\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020!2\u0006\u0010#\u001a\u00020\u000eH\u0016J\u0016\u0010$\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0017J\u0016\u0010%\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010&J\u0018\u0010\'\u001a\u0004\u0018\u00010\u00102\u0006\u0010(\u001a\u00020\u000eH\u0096@\u00a2\u0006\u0002\u0010&J\u0016\u0010)\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0010H\u0096@\u00a2\u0006\u0002\u0010\u0017R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006*"}, d2 = {"Lcom/narrapay/data/repository/TransacaoRepositoryImpl;", "Lcom/narrapay/domain/repository/TransacaoRepository;", "transacaoDao", "Lcom/narrapay/data/local/dao/TransacaoDao;", "regraAutomacaoDao", "Lcom/narrapay/data/local/dao/RegraAutomacaoDao;", "categoriaDao", "Lcom/narrapay/data/local/dao/CategoriaDao;", "database", "Lcom/narrapay/data/local/AppDatabase;", "(Lcom/narrapay/data/local/dao/TransacaoDao;Lcom/narrapay/data/local/dao/RegraAutomacaoDao;Lcom/narrapay/data/local/dao/CategoriaDao;Lcom/narrapay/data/local/AppDatabase;)V", "atribuirRateio", "", "transacaoId", "", "transacaoAtualizada", "Lcom/narrapay/data/local/entity/TransacaoEntity;", "novasAtribuicoes", "", "Lcom/narrapay/data/local/entity/AtribuicaoTransacaoEntity;", "(Ljava/lang/String;Lcom/narrapay/data/local/entity/TransacaoEntity;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "atualizarTransacao", "transacao", "(Lcom/narrapay/data/local/entity/TransacaoEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buscarCategorias", "Lkotlinx/coroutines/flow/Flow;", "Lcom/narrapay/data/local/entity/CategoriaEntity;", "buscarRegrasAutomacao", "Lcom/narrapay/data/local/entity/RegraAutomacaoEntity;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buscarTransacoesFiltradas", "Lcom/narrapay/data/local/relation/TransacaoCompleta;", "inicioTimestamp", "", "fimTimestamp", "queryBusca", "deletarTransacao", "deletarTransacaoPorId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTransacaoById", "id", "salvarTransacao", "app_debug"})
public final class TransacaoRepositoryImpl implements com.narrapay.domain.repository.TransacaoRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.TransacaoDao transacaoDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.RegraAutomacaoDao regraAutomacaoDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.CategoriaDao categoriaDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.AppDatabase database = null;
    
    public TransacaoRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.TransacaoDao transacaoDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.RegraAutomacaoDao regraAutomacaoDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.CategoriaDao categoriaDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.AppDatabase database) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object salvarTransacao(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacao, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object atualizarTransacao(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacao, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object buscarRegrasAutomacao(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.narrapay.data.local.entity.RegraAutomacaoEntity>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTransacaoById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.narrapay.data.local.entity.TransacaoEntity> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.narrapay.data.local.entity.CategoriaEntity>> buscarCategorias() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object atribuirRateio(@org.jetbrains.annotations.NotNull()
    java.lang.String transacaoId, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacaoAtualizada, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.entity.AtribuicaoTransacaoEntity> novasAtribuicoes, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deletarTransacao(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacao, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object deletarTransacaoPorId(@org.jetbrains.annotations.NotNull()
    java.lang.String transacaoId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<java.util.List<com.narrapay.data.local.relation.TransacaoCompleta>> buscarTransacoesFiltradas(long inicioTimestamp, long fimTimestamp, @org.jetbrains.annotations.NotNull()
    java.lang.String queryBusca) {
        return null;
    }
}