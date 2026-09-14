package com.narrapay.ui.dashboard;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ2\u0010\u0017\u001a\u0014\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u001a0\u00182\u0006\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u001d\u001a\u00020\u0012H\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0002J\u0006\u0010 \u001a\u00020\u001fJ\u000e\u0010!\u001a\u00020\u001f2\u0006\u0010\"\u001a\u00020\u0012J\u0016\u0010#\u001a\u00020\u001f2\u0006\u0010$\u001a\u00020\u00122\u0006\u0010%\u001a\u00020\u0012J\u0016\u0010&\u001a\u00020\u001f2\u0006\u0010\'\u001a\u00020\u00122\u0006\u0010(\u001a\u00020\u0012J\u000e\u0010)\u001a\u00020\u001f2\u0006\u0010*\u001a\u00020+J\u000e\u0010,\u001a\u00020\u001f2\u0006\u0010*\u001a\u00020+J\u0006\u0010-\u001a\u00020\u001fR\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0010\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0012\u0012\u0004\u0012\u00020\u00120\u00110\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006."}, d2 = {"Lcom/narrapay/ui/dashboard/DashboardViewModel;", "Landroidx/lifecycle/ViewModel;", "transacaoDao", "Lcom/narrapay/data/local/dao/TransacaoDao;", "configuracoesDao", "Lcom/narrapay/data/local/dao/ConfiguracoesDao;", "pessoaDao", "Lcom/narrapay/data/local/dao/PessoaDao;", "processarTransacaoPluggyUseCase", "Lcom/narrapay/domain/usecase/ProcessarTransacaoPluggyUseCase;", "pluggyNetworkRepository", "Lcom/narrapay/data/remote/repository/PluggyNetworkRepository;", "(Lcom/narrapay/data/local/dao/TransacaoDao;Lcom/narrapay/data/local/dao/ConfiguracoesDao;Lcom/narrapay/data/local/dao/PessoaDao;Lcom/narrapay/domain/usecase/ProcessarTransacaoPluggyUseCase;Lcom/narrapay/data/remote/repository/PluggyNetworkRepository;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/narrapay/ui/dashboard/DashboardUiState;", "mesAnoFlow", "Lkotlin/Pair;", "", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "calcularLimitesFatura", "Lkotlin/Triple;", "", "", "diaFechamento", "mesSelecionado", "anoSelecionado", "carregarDashboard", "", "injetarDadosDeTeste", "mudarMes", "offset", "salvarConfiguracaoFatura", "fechamento", "vencimento", "selecionarMesAno", "mes", "ano", "setShowConfigDialog", "show", "", "setShowMesAnoDialog", "sincronizarPluggy", "app_debug"})
public final class DashboardViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.TransacaoDao transacaoDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.ConfiguracoesDao configuracoesDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.PessoaDao pessoaDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.domain.usecase.ProcessarTransacaoPluggyUseCase processarTransacaoPluggyUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.remote.repository.PluggyNetworkRepository pluggyNetworkRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.narrapay.ui.dashboard.DashboardUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.dashboard.DashboardUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<kotlin.Pair<java.lang.Integer, java.lang.Integer>> mesAnoFlow = null;
    
    public DashboardViewModel(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.TransacaoDao transacaoDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.ConfiguracoesDao configuracoesDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.PessoaDao pessoaDao, @org.jetbrains.annotations.NotNull()
    com.narrapay.domain.usecase.ProcessarTransacaoPluggyUseCase processarTransacaoPluggyUseCase, @org.jetbrains.annotations.NotNull()
    com.narrapay.data.remote.repository.PluggyNetworkRepository pluggyNetworkRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.narrapay.ui.dashboard.DashboardUiState> getUiState() {
        return null;
    }
    
    private final void carregarDashboard() {
    }
    
    private final kotlin.Triple<java.lang.Long, java.lang.Long, java.lang.String> calcularLimitesFatura(int diaFechamento, int mesSelecionado, int anoSelecionado) {
        return null;
    }
    
    public final void mudarMes(int offset) {
    }
    
    public final void selecionarMesAno(int mes, int ano) {
    }
    
    public final void setShowMesAnoDialog(boolean show) {
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