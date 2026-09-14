package com.narrapay.ui.transacao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u001e\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001Bu\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0005\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u0012\b\b\u0002\u0010\f\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\u0002\u0010\u0011J\t\u0010\u001f\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010 \u001a\u0004\u0018\u00010\u000bH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0013J\u000b\u0010!\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\t\u0010$\u001a\u00020\u0005H\u00c6\u0003J\t\u0010%\u001a\u00020\u0005H\u00c6\u0003J\t\u0010&\u001a\u00020\u000bH\u00c6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010(\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eH\u00c6\u0003J~\u0010)\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00032\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u00052\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u00032\u000e\b\u0002\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\n\b\u0002\u0010\u0010\u001a\u0004\u0018\u00010\u000bH\u00c6\u0001\u00a2\u0006\u0002\u0010*J\u0013\u0010+\u001a\u00020\u00032\b\u0010,\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010-\u001a\u00020.H\u00d6\u0001J\t\u0010/\u001a\u00020\u0005H\u00d6\u0001R\u0015\u0010\u0010\u001a\u0004\u0018\u00010\u000b\u00a2\u0006\n\n\u0002\u0010\u0014\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\b\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001aR\u0011\u0010\f\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u001cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u001cR\u0011\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u001cR\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001aR\u0011\u0010\t\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001a\u00a8\u00060"}, d2 = {"Lcom/narrapay/ui/transacao/TransacaoFormUiState;", "", "isLoading", "", "error", "", "isSuccess", "transacaoId", "descricaoInput", "valorInput", "dataHora", "", "isEditing", "categorias", "", "Lcom/narrapay/data/local/entity/CategoriaEntity;", "categoriaIdSelecionada", "(ZLjava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;JZLjava/util/List;Ljava/lang/Long;)V", "getCategoriaIdSelecionada", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getCategorias", "()Ljava/util/List;", "getDataHora", "()J", "getDescricaoInput", "()Ljava/lang/String;", "getError", "()Z", "getTransacaoId", "getValorInput", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(ZLjava/lang/String;ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;JZLjava/util/List;Ljava/lang/Long;)Lcom/narrapay/ui/transacao/TransacaoFormUiState;", "equals", "other", "hashCode", "", "toString", "app_release"})
public final class TransacaoFormUiState {
    private final boolean isLoading = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String error = null;
    private final boolean isSuccess = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String transacaoId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String descricaoInput = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String valorInput = null;
    private final long dataHora = 0L;
    private final boolean isEditing = false;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.narrapay.data.local.entity.CategoriaEntity> categorias = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long categoriaIdSelecionada = null;
    
    public TransacaoFormUiState(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, boolean isSuccess, @org.jetbrains.annotations.Nullable()
    java.lang.String transacaoId, @org.jetbrains.annotations.NotNull()
    java.lang.String descricaoInput, @org.jetbrains.annotations.NotNull()
    java.lang.String valorInput, long dataHora, boolean isEditing, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.entity.CategoriaEntity> categorias, @org.jetbrains.annotations.Nullable()
    java.lang.Long categoriaIdSelecionada) {
        super();
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getError() {
        return null;
    }
    
    public final boolean isSuccess() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTransacaoId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDescricaoInput() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getValorInput() {
        return null;
    }
    
    public final long getDataHora() {
        return 0L;
    }
    
    public final boolean isEditing() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.data.local.entity.CategoriaEntity> getCategorias() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getCategoriaIdSelecionada() {
        return null;
    }
    
    public TransacaoFormUiState() {
        super();
    }
    
    public final boolean component1() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component10() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component2() {
        return null;
    }
    
    public final boolean component3() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    public final long component7() {
        return 0L;
    }
    
    public final boolean component8() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.data.local.entity.CategoriaEntity> component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.narrapay.ui.transacao.TransacaoFormUiState copy(boolean isLoading, @org.jetbrains.annotations.Nullable()
    java.lang.String error, boolean isSuccess, @org.jetbrains.annotations.Nullable()
    java.lang.String transacaoId, @org.jetbrains.annotations.NotNull()
    java.lang.String descricaoInput, @org.jetbrains.annotations.NotNull()
    java.lang.String valorInput, long dataHora, boolean isEditing, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.entity.CategoriaEntity> categorias, @org.jetbrains.annotations.Nullable()
    java.lang.Long categoriaIdSelecionada) {
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