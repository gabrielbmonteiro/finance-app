package com.narrapay.ui.pendencias;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B5\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\u000bJ\t\u0010\u0013\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0014\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\u0015\u001a\u00020\u0007H\u00c6\u0003J\u000f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00c6\u0003J9\u0010\u0017\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tH\u00c6\u0001J\u0013\u0010\u0018\u001a\u00020\u00032\b\u0010\u0019\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001a\u001a\u00020\u001bH\u00d6\u0001J\t\u0010\u001c\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0010R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001d"}, d2 = {"Lcom/narrapay/ui/pendencias/PendenciasUiState;", "", "isLoading", "", "error", "", "filtroSelecionado", "Lcom/narrapay/ui/pendencias/FiltroPendencia;", "transacoesPendentes", "", "Lcom/narrapay/data/local/relation/TransacaoCompleta;", "(ZLjava/lang/String;Lcom/narrapay/ui/pendencias/FiltroPendencia;Ljava/util/List;)V", "getError", "()Ljava/lang/String;", "getFiltroSelecionado", "()Lcom/narrapay/ui/pendencias/FiltroPendencia;", "()Z", "getTransacoesPendentes", "()Ljava/util/List;", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "app_release"})
public final class PendenciasUiState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.ui.pendencias.FiltroPendencia filtroSelecionado = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> transacoesPendentes = null;
    
    public PendenciasUiState(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    com.narrapay.ui.pendencias.FiltroPendencia filtroSelecionado, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> transacoesPendentes) {
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
    public final com.narrapay.ui.pendencias.FiltroPendencia getFiltroSelecionado() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> getTransacoesPendentes() {
        return null;
    }
    
    public PendenciasUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.narrapay.ui.pendencias.FiltroPendencia component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.narrapay.ui.pendencias.PendenciasUiState copy(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    com.narrapay.ui.pendencias.FiltroPendencia filtroSelecionado, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> transacoesPendentes) {
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