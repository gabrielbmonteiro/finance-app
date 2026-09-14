package com.narrapay.ui.rateio;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\u001e\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001cJ\u0015\u0010\u001f\u001a\u00020\u001a2\b\u0010 \u001a\u0004\u0018\u00010!\u00a2\u0006\u0002\u0010\"J\u0016\u0010#\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020!2\u0006\u0010%\u001a\u00020\u001cJ\u000e\u0010&\u001a\u00020\u001a2\u0006\u0010\'\u001a\u00020\u001cJ\u000e\u0010(\u001a\u00020\u001a2\u0006\u0010%\u001a\u00020\u001cJ\u000e\u0010)\u001a\u00020\u001a2\u0006\u0010*\u001a\u00020\u001cJ\u0006\u0010+\u001a\u00020\u001aJ\u0006\u0010,\u001a\u00020\u001aJ\u000e\u0010-\u001a\u00020\u001a2\u0006\u0010.\u001a\u00020/J\u0016\u00100\u001a\u00020\u001a2\u0006\u0010.\u001a\u00020/2\u0006\u00101\u001a\u00020\u001cJ\u0006\u00102\u001a\u00020\u001aJ\u000e\u00103\u001a\u00020\u001a2\u0006\u00104\u001a\u000205R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00100\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018\u00a8\u00066"}, d2 = {"Lcom/narrapay/ui/rateio/AtribuicaoViewModel;", "Landroidx/lifecycle/ViewModel;", "transacaoDao", "Lcom/narrapay/data/local/dao/TransacaoDao;", "pessoaDao", "Lcom/narrapay/data/local/dao/PessoaDao;", "categoriaDao", "Lcom/narrapay/data/local/dao/CategoriaDao;", "atribuirValoresUseCase", "Lcom/narrapay/domain/usecase/AtribuirValoresUseCase;", "(Lcom/narrapay/data/local/dao/TransacaoDao;Lcom/narrapay/data/local/dao/PessoaDao;Lcom/narrapay/data/local/dao/CategoriaDao;Lcom/narrapay/domain/usecase/AtribuirValoresUseCase;)V", "_eventos", "Lkotlinx/coroutines/channels/Channel;", "Lcom/narrapay/ui/rateio/AtribuicaoEvent;", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/narrapay/ui/rateio/RateioUiState;", "eventos", "Lkotlinx/coroutines/flow/Flow;", "getEventos", "()Lkotlinx/coroutines/flow/Flow;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "adicionarCategoria", "", "nome", "", "icone", "corHex", "atualizarCategoria", "categoriaId", "", "(Ljava/lang/Long;)V", "atualizarParte", "pessoaId", "valor", "atualizarTitulo", "titulo", "atualizarValorTotal", "carregarTransacao", "transacaoId", "clearError", "confirmarRateio", "deletarCategoria", "categoria", "Lcom/narrapay/data/local/entity/CategoriaEntity;", "editarCategoria", "novoNome", "ignorarTransacao", "togglePessoaNaDivida", "pessoa", "Lcom/narrapay/data/local/entity/PessoaEntity;", "app_debug"})
public final class AtribuicaoViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.TransacaoDao transacaoDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.PessoaDao pessoaDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.CategoriaDao categoriaDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.domain.usecase.AtribuirValoresUseCase atribuirValoresUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.narrapay.ui.rateio.RateioUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.rateio.RateioUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.channels.Channel<com.narrapay.ui.rateio.AtribuicaoEvent> _eventos = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.narrapay.ui.rateio.AtribuicaoEvent> eventos = null;
    
    public AtribuicaoViewModel(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.TransacaoDao transacaoDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.PessoaDao pessoaDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.CategoriaDao categoriaDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.domain.usecase.AtribuirValoresUseCase atribuirValoresUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.rateio.RateioUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.narrapay.ui.rateio.AtribuicaoEvent> getEventos() {
        return null;
    }
    
    public final void carregarTransacao(@org.jetbrains.annotations.NotNull()
    java.lang.String transacaoId) {
    }
    
    public final void atualizarTitulo(@org.jetbrains.annotations.NotNull()
    java.lang.String titulo) {
    }
    
    public final void atualizarValorTotal(@org.jetbrains.annotations.NotNull()
    java.lang.String valor) {
    }
    
    public final void atualizarCategoria(@org.jetbrains.annotations.Nullable()
    java.lang.Long categoriaId) {
    }
    
    public final void adicionarCategoria(@org.jetbrains.annotations.NotNull()
    java.lang.String nome, @org.jetbrains.annotations.NotNull()
    java.lang.String icone, @org.jetbrains.annotations.NotNull()
    java.lang.String corHex) {
    }
    
    public final void deletarCategoria(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.CategoriaEntity categoria) {
    }
    
    public final void editarCategoria(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.CategoriaEntity categoria, @org.jetbrains.annotations.NotNull()
    java.lang.String novoNome) {
    }
    
    public final void togglePessoaNaDivida(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.PessoaEntity pessoa) {
    }
    
    public final void atualizarParte(long pessoaId, @org.jetbrains.annotations.NotNull()
    java.lang.String valor) {
    }
    
    public final void clearError() {
    }
    
    public final void confirmarRateio() {
    }
    
    public final void ignorarTransacao() {
    }
}