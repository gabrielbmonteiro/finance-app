package com.narrapay.ui.dashboard;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ*\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\u00150\u00132\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\rH\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\u0006\u0010\u001a\u001a\u00020\u0019J\u000e\u0010\u001b\u001a\u00020\u00192\u0006\u0010\u0017\u001a\u00020\rJ\u0016\u0010\u001c\u001a\u00020\u00192\u0006\u0010\u001d\u001a\u00020\r2\u0006\u0010\u001e\u001a\u00020\rJ\u000e\u0010\u001f\u001a\u00020\u00192\u0006\u0010 \u001a\u00020!J\u0006\u0010\"\u001a\u00020\u0019R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000b0\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006#"}, d2 = {"Lcom/narrapay/ui/dashboard/DashboardViewModel;", "Landroidx/lifecycle/ViewModel;", "transacaoDao", "Lcom/narrapay/data/local/dao/TransacaoDao;", "configuracoesDao", "Lcom/narrapay/data/local/dao/ConfiguracoesDao;", "processarTransacaoPluggyUseCase", "Lcom/narrapay/domain/usecase/ProcessarTransacaoPluggyUseCase;", "(Lcom/narrapay/data/local/dao/TransacaoDao;Lcom/narrapay/data/local/dao/ConfiguracoesDao;Lcom/narrapay/domain/usecase/ProcessarTransacaoPluggyUseCase;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/narrapay/ui/dashboard/DashboardUiState;", "mesOffsetFlow", "", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "calcularLimitesFatura", "Lkotlin/Triple;", "", "", "diaFechamento", "offset", "carregarDashboard", "", "injetarDadosDeTeste", "mudarMes", "salvarConfiguracaoFatura", "fechamento", "vencimento", "setShowConfigDialog", "show", "", "sincronizarPluggy", "app_release"})
public final class DashboardViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.TransacaoDao transacaoDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.ConfiguracoesDao configuracoesDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.domain.usecase.ProcessarTransacaoPluggyUseCase processarTransacaoPluggyUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.narrapay.ui.dashboard.DashboardUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.dashboard.DashboardUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Integer> mesOffsetFlow = null;
    
    public DashboardViewModel(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.TransacaoDao transacaoDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.ConfiguracoesDao configuracoesDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.domain.usecase.ProcessarTransacaoPluggyUseCase processarTransacaoPluggyUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.dashboard.DashboardUiState> getUiState() {
        return null;
    }
    
    private final void carregarDashboard() {
    }
    
    private final kotlin.Triple<java.lang.Long, java.lang.Long, java.lang.String> calcularLimitesFatura(int diaFechamento, int offset) {
        return null;
    }
    
    public final void mudarMes(int offset) {
    }
    
    public final void setShowConfigDialog(boolean show) {
    }
    
    public final void salvarConfiguracaoFatura(int fechamento, int vencimento) {
    }
    
    public final void sincronizarPluggy() {
    }
    
    public final void injetarDadosDeTeste() {
    }
}