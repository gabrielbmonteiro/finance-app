package com.narrapay.ui.extrato;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\tJ\u0015\u0010\u0015\u001a\u00020\u00132\b\u0010\u0016\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\u0017J\u000e\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\tR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u001a"}, d2 = {"Lcom/narrapay/ui/extrato/ExtratoViewModel;", "Landroidx/lifecycle/ViewModel;", "transacaoDao", "Lcom/narrapay/data/local/dao/TransacaoDao;", "pessoaDao", "Lcom/narrapay/data/local/dao/PessoaDao;", "(Lcom/narrapay/data/local/dao/TransacaoDao;Lcom/narrapay/data/local/dao/PessoaDao;)V", "_filtroBuscaFlow", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_filtroResponsavelFlow", "", "_uiState", "Lcom/narrapay/ui/extrato/ExtratoUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "atualizarBusca", "", "query", "atualizarResponsavel", "pessoaId", "(Ljava/lang/Long;)V", "excluirTransacao", "id", "app_release"})
public final class ExtratoViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.TransacaoDao transacaoDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.PessoaDao pessoaDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _filtroBuscaFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Long> _filtroResponsavelFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.narrapay.ui.extrato.ExtratoUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.extrato.ExtratoUiState> uiState = null;
    
    public ExtratoViewModel(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.TransacaoDao transacaoDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.PessoaDao pessoaDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.extrato.ExtratoUiState> getUiState() {
        return null;
    }
    
    public final void atualizarBusca(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public final void atualizarResponsavel(@org.jetbrains.annotations.Nullable()
    java.lang.Long pessoaId) {
    }
    
    public final void excluirTransacao(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
    }
}