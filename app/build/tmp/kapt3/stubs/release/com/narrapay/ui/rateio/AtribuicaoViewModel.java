package com.narrapay.ui.rateio;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cJ\u000e\u0010\u001d\u001a\u00020\u00182\u0006\u0010\u001e\u001a\u00020\u001cJ\u0006\u0010\u001f\u001a\u00020\u0018R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006 "}, d2 = {"Lcom/narrapay/ui/rateio/AtribuicaoViewModel;", "Landroidx/lifecycle/ViewModel;", "transacaoDao", "Lcom/narrapay/data/local/dao/TransacaoDao;", "pessoaDao", "Lcom/narrapay/data/local/dao/PessoaDao;", "atribuirValoresUseCase", "Lcom/narrapay/domain/usecase/AtribuirValoresUseCase;", "(Lcom/narrapay/data/local/dao/TransacaoDao;Lcom/narrapay/data/local/dao/PessoaDao;Lcom/narrapay/domain/usecase/AtribuirValoresUseCase;)V", "_eventos", "Lkotlinx/coroutines/channels/Channel;", "Lcom/narrapay/ui/rateio/AtribuicaoEvent;", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/narrapay/ui/rateio/RateioUiState;", "eventos", "Lkotlinx/coroutines/flow/Flow;", "getEventos", "()Lkotlinx/coroutines/flow/Flow;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "atualizarParte", "", "pessoaId", "", "valor", "", "carregarTransacao", "transacaoId", "confirmarRateio", "app_release"})
public final class AtribuicaoViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.TransacaoDao transacaoDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.PessoaDao pessoaDao = null;
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
    
    public final void atualizarParte(long pessoaId, @org.jetbrains.annotations.NotNull()
    java.lang.String valor) {
    }
    
    public final void confirmarRateio() {
    }
}