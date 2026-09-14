package com.narrapay.ui.dashboard;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b.\b\u0086\b\u0018\u00002\u00020\u0001B\u00ab\u0001\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\b\u0012\b\b\u0002\u0010\n\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000e\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u0012\b\b\u0002\u0010\u0015\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0017\u001a\u00020\b\u0012\b\b\u0002\u0010\u0018\u001a\u00020\b\u00a2\u0006\u0002\u0010\u0019J\t\u0010.\u001a\u00020\u0003H\u00c6\u0003J\t\u0010/\u001a\u00020\u0003H\u00c6\u0003J\u000b\u00100\u001a\u0004\u0018\u00010\u0014H\u00c6\u0003J\t\u00101\u001a\u00020\u0003H\u00c6\u0003J\t\u00102\u001a\u00020\u0003H\u00c6\u0003J\t\u00103\u001a\u00020\bH\u00c6\u0003J\t\u00104\u001a\u00020\bH\u00c6\u0003J\u000b\u00105\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u00106\u001a\u00020\u0005H\u00c6\u0003J\t\u00107\u001a\u00020\bH\u00c6\u0003J\t\u00108\u001a\u00020\bH\u00c6\u0003J\t\u00109\u001a\u00020\u0005H\u00c6\u0003J\t\u0010:\u001a\u00020\fH\u00c6\u0003J\u000f\u0010;\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u00c6\u0003J\u000f\u0010<\u001a\b\u0012\u0004\u0012\u00020\u00110\u000eH\u00c6\u0003J\u00af\u0001\u0010=\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\b2\b\b\u0002\u0010\n\u001a\u00020\u00052\b\b\u0002\u0010\u000b\u001a\u00020\f2\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u000e\b\u0002\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000e2\b\b\u0002\u0010\u0012\u001a\u00020\u00032\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00142\b\b\u0002\u0010\u0015\u001a\u00020\u00032\b\b\u0002\u0010\u0016\u001a\u00020\u00032\b\b\u0002\u0010\u0017\u001a\u00020\b2\b\b\u0002\u0010\u0018\u001a\u00020\bH\u00c6\u0001J\u0013\u0010>\u001a\u00020\u00032\b\u0010?\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010@\u001a\u00020\bH\u00d6\u0001J\t\u0010A\u001a\u00020\u0005H\u00d6\u0001R\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u0017\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001bR\u0011\u0010\u0018\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001bR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010$R\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b%\u0010\u001fR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001bR\u0011\u0010\n\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010\u001fR\u0011\u0010\u0015\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010$R\u0011\u0010\u0016\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010$R\u0011\u0010\u0012\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010$R\u0013\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010#\u00a8\u0006B"}, d2 = {"Lcom/narrapay/ui/dashboard/DashboardUiState;", "", "isLoading", "", "error", "", "mesAtual", "mesSelecionado", "", "anoSelecionado", "periodoFatura", "faturaTotal", "", "gastosPessoas", "", "Lcom/narrapay/ui/dashboard/GastoPessoa;", "ultimasPendencias", "Lcom/narrapay/data/local/relation/TransacaoCompleta;", "showSyncLoading", "syncStatus", "Lcom/narrapay/ui/dashboard/SyncStatus;", "showFaturaConfigDialog", "showMesAnoDialog", "diaFechamento", "diaVencimento", "(ZLjava/lang/String;Ljava/lang/String;IILjava/lang/String;DLjava/util/List;Ljava/util/List;ZLcom/narrapay/ui/dashboard/SyncStatus;ZZII)V", "getAnoSelecionado", "()I", "getDiaFechamento", "getDiaVencimento", "getError", "()Ljava/lang/String;", "getFaturaTotal", "()D", "getGastosPessoas", "()Ljava/util/List;", "()Z", "getMesAtual", "getMesSelecionado", "getPeriodoFatura", "getShowFaturaConfigDialog", "getShowMesAnoDialog", "getShowSyncLoading", "getSyncStatus", "()Lcom/narrapay/ui/dashboard/SyncStatus;", "getUltimasPendencias", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class DashboardUiState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String mesAtual = null;
    private final int mesSelecionado = 0;
    private final int anoSelecionado = 0;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String periodoFatura = null;
    private final double faturaTotal = 0.0;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.narrapay.ui.dashboard.GastoPessoa> gastosPessoas = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> ultimasPendencias = null;
    private final boolean showSyncLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final com.narrapay.ui.dashboard.SyncStatus syncStatus = null;
    private final boolean showFaturaConfigDialog = false;
    private final boolean showMesAnoDialog = false;
    private final int diaFechamento = 0;
    private final int diaVencimento = 0;
    
    public DashboardUiState(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    java.lang.String mesAtual, int mesSelecionado, int anoSelecionado, @org.jetbrains.annotations.NotNull()
    java.lang.String periodoFatura, double faturaTotal, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.ui.dashboard.GastoPessoa> gastosPessoas, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> ultimasPendencias, boolean showSyncLoading, @org.jetbrains.annotations.Nullable()
    com.narrapay.ui.dashboard.SyncStatus syncStatus, boolean showFaturaConfigDialog, boolean showMesAnoDialog, int diaFechamento, int diaVencimento) {
        super();
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getMesAtual() {
        return null;
    }
    
    public final int getMesSelecionado() {
        return 0;
    }
    
    public final int getAnoSelecionado() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPeriodoFatura() {
        return null;
    }
    
    public final double getFaturaTotal() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.ui.dashboard.GastoPessoa> getGastosPessoas() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> getUltimasPendencias() {
        return null;
    }
    
    public final boolean getShowSyncLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.narrapay.ui.dashboard.SyncStatus getSyncStatus() {
        return null;
    }
    
    public final boolean getShowFaturaConfigDialog() {
        return false;
    }
    
    public final boolean getShowMesAnoDialog() {
        return false;
    }
    
    public final int getDiaFechamento() {
        return 0;
    }
    
    public final int getDiaVencimento() {
        return 0;
    }
    
    public DashboardUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    public final boolean component10() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.narrapay.ui.dashboard.SyncStatus component11() {
        return null;
    }
    
    public final boolean component12() {
        return false;
    }
    
    public final boolean component13() {
        return false;
    }
    
    public final int component14() {
        return 0;
    }
    
    public final int component15() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final int component5() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    public final double component7() {
        return 0.0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.ui.dashboard.GastoPessoa> component8() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.narrapay.ui.dashboard.DashboardUiState copy(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    java.lang.String mesAtual, int mesSelecionado, int anoSelecionado, @org.jetbrains.annotations.NotNull()
    java.lang.String periodoFatura, double faturaTotal, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.ui.dashboard.GastoPessoa> gastosPessoas, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> ultimasPendencias, boolean showSyncLoading, @org.jetbrains.annotations.Nullable()
    com.narrapay.ui.dashboard.SyncStatus syncStatus, boolean showFaturaConfigDialog, boolean showMesAnoDialog, int diaFechamento, int diaVencimento) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}