package com.narrapay.ui.extrato;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0016\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BQ\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0007\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0005\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\u0002\u0010\u000eJ\t\u0010\u0019\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001a\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J\u000f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\n0\u0007H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0005H\u00c6\u0003J\u0010\u0010\u001e\u001a\u0004\u0018\u00010\rH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0013JZ\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\u000e\b\u0002\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00052\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\rH\u00c6\u0001\u00a2\u0006\u0002\u0010 J\u0013\u0010!\u001a\u00020\u00032\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020$H\u00d6\u0001J\t\u0010%\u001a\u00020\u0005H\u00d6\u0001R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u000b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0015\u0010\f\u001a\u0004\u0018\u00010\r\u00a2\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0015R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0017\u00a8\u0006&"}, d2 = {"Lcom/narrapay/ui/extrato/ExtratoUiState;", "", "isLoading", "", "error", "", "transacoes", "", "Lcom/narrapay/data/local/relation/TransacaoCompleta;", "pessoas", "Lcom/narrapay/data/local/entity/PessoaEntity;", "filtroBusca", "filtroResponsavel", "", "(ZLjava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/lang/Long;)V", "getError", "()Ljava/lang/String;", "getFiltroBusca", "getFiltroResponsavel", "()Ljava/lang/Long;", "Ljava/lang/Long;", "()Z", "getPessoas", "()Ljava/util/List;", "getTransacoes", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "(ZLjava/lang/String;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/lang/Long;)Lcom/narrapay/ui/extrato/ExtratoUiState;", "equals", "other", "hashCode", "", "toString", "app_debug"})
public final class ExtratoUiState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> transacoes = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.narrapay.data.local.entity.PessoaEntity> pessoas = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String filtroBusca = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long filtroResponsavel = null;
    
    public ExtratoUiState(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> transacoes, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.entity.PessoaEntity> pessoas, @org.jetbrains.annotations.NotNull()
    java.lang.String filtroBusca, @org.jetbrains.annotations.Nullable()
    java.lang.Long filtroResponsavel) {
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
    public final java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> getTransacoes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.data.local.entity.PessoaEntity> getPessoas() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFiltroBusca() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getFiltroResponsavel() {
        return null;
    }
    
    public ExtratoUiState() {
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
    public final java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.data.local.entity.PessoaEntity> component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.narrapay.ui.extrato.ExtratoUiState copy(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.relation.TransacaoCompleta> transacoes, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.entity.PessoaEntity> pessoas, @org.jetbrains.annotations.NotNull()
    java.lang.String filtroBusca, @org.jetbrains.annotations.Nullable()
    java.lang.Long filtroResponsavel) {
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