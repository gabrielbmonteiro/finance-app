package com.narrapay.ui.transacao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\n\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0015\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u00a2\u0006\u0002\u0010\u0010J\u000e\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u0015\u001a\u00020\u0013J\u000e\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\u0013J\u0010\u0010\u0018\u001a\u00020\r2\b\u0010\u0019\u001a\u0004\u0018\u00010\u0013J\u0006\u0010\u001a\u001a\u00020\rJ\u0006\u0010\u001b\u001a\u00020\rJ\u0006\u0010\u001c\u001a\u00020\rR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u001d"}, d2 = {"Lcom/narrapay/ui/transacao/TransacaoFormViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/narrapay/domain/repository/TransacaoRepository;", "(Lcom/narrapay/domain/repository/TransacaoRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/narrapay/ui/transacao/TransacaoFormUiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "atualizarCategoria", "", "categoriaId", "", "(Ljava/lang/Long;)V", "atualizarDescricao", "descricao", "", "atualizarParcelas", "parcelas", "atualizarValor", "valor", "carregarTransacao", "id", "excluirTransacao", "limparErro", "salvarTransacao", "app_debug"})
public final class TransacaoFormViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.domain.repository.TransacaoRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.narrapay.ui.transacao.TransacaoFormUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.transacao.TransacaoFormUiState> uiState = null;
    
    public TransacaoFormViewModel(@org.jetbrains.annotations.NotNull()
    com.narrapay.domain.repository.TransacaoRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.transacao.TransacaoFormUiState> getUiState() {
        return null;
    }
    
    public final void carregarTransacao(@org.jetbrains.annotations.Nullable()
    java.lang.String id) {
    }
    
    public final void atualizarDescricao(@org.jetbrains.annotations.NotNull()
    java.lang.String descricao) {
    }
    
    public final void atualizarValor(@org.jetbrains.annotations.NotNull()
    java.lang.String valor) {
    }
    
    public final void atualizarCategoria(@org.jetbrains.annotations.Nullable()
    java.lang.Long categoriaId) {
    }
    
    public final void atualizarParcelas(@org.jetbrains.annotations.NotNull()
    java.lang.String parcelas) {
    }
    
    public final void salvarTransacao() {
    }
    
    public final void excluirTransacao() {
    }
    
    public final void limparErro() {
    }
}