package com.narrapay.ui.pendencias;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u000fJ\u000e\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u000fJ\b\u0010\u0013\u001a\u00020\rH\u0002J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u000fJ\u000e\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u0017R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0018"}, d2 = {"Lcom/narrapay/ui/pendencias/PendenciasViewModel;", "Landroidx/lifecycle/ViewModel;", "transacaoDao", "Lcom/narrapay/data/local/dao/TransacaoDao;", "(Lcom/narrapay/data/local/dao/TransacaoDao;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/narrapay/ui/pendencias/PendenciasUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "atualizarBusca", "", "texto", "", "excluirTransacao", "id", "ignorarTransacao", "observarPendencias", "reativarTransacao", "setFiltro", "filtro", "Lcom/narrapay/ui/pendencias/FiltroPendencia;", "app_debug"})
public final class PendenciasViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.TransacaoDao transacaoDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.narrapay.ui.pendencias.PendenciasUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.pendencias.PendenciasUiState> uiState = null;
    
    public PendenciasViewModel(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.TransacaoDao transacaoDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.pendencias.PendenciasUiState> getUiState() {
        return null;
    }
    
    private final void observarPendencias() {
    }
    
    public final void setFiltro(@org.jetbrains.annotations.NotNull()
    com.narrapay.ui.pendencias.FiltroPendencia filtro) {
    }
    
    public final void atualizarBusca(@org.jetbrains.annotations.NotNull()
    java.lang.String texto) {
    }
    
    public final void excluirTransacao(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }
    
    public final void reativarTransacao(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }
    
    public final void ignorarTransacao(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }
}