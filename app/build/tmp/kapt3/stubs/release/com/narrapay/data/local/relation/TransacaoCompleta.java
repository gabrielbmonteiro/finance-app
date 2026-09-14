package com.narrapay.data.local.relation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u0011\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u000f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0003J/\u0010\u0013\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u000e\b\u0002\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00c6\u0001J\u0013\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0017\u001a\u00020\u0018H\u00d6\u0001J\t\u0010\u0019\u001a\u00020\u001aH\u00d6\u0001R\u001c\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u00078\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0018\u0010\u0004\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u001b"}, d2 = {"Lcom/narrapay/data/local/relation/TransacaoCompleta;", "", "transacao", "Lcom/narrapay/data/local/entity/TransacaoEntity;", "categoria", "Lcom/narrapay/data/local/entity/CategoriaEntity;", "atribuicoes", "", "Lcom/narrapay/data/local/relation/AtribuicaoComPessoa;", "(Lcom/narrapay/data/local/entity/TransacaoEntity;Lcom/narrapay/data/local/entity/CategoriaEntity;Ljava/util/List;)V", "getAtribuicoes", "()Ljava/util/List;", "getCategoria", "()Lcom/narrapay/data/local/entity/CategoriaEntity;", "getTransacao", "()Lcom/narrapay/data/local/entity/TransacaoEntity;", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_release"})
public final class TransacaoCompleta {
    @androidx.room.Embedded()
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.entity.TransacaoEntity transacao = null;
    @androidx.room.Relation(parentColumn = "categoriaId", entityColumn = "id")
    @org.jetbrains.annotations.Nullable()
    private final com.narrapay.data.local.entity.CategoriaEntity categoria = null;
    @androidx.room.Relation(entity = com.narrapay.data.local.entity.AtribuicaoTransacaoEntity.class, parentColumn = "id", entityColumn = "transacaoId")
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.narrapay.data.local.relation.AtribuicaoComPessoa> atribuicoes = null;
    
    public TransacaoCompleta(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacao, @org.jetbrains.annotations.Nullable()
    com.narrapay.data.local.entity.CategoriaEntity categoria, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.relation.AtribuicaoComPessoa> atribuicoes) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.narrapay.data.local.entity.TransacaoEntity getTransacao() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.narrapay.data.local.entity.CategoriaEntity getCategoria() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.data.local.relation.AtribuicaoComPessoa> getAtribuicoes() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.narrapay.data.local.entity.TransacaoEntity component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.narrapay.data.local.entity.CategoriaEntity component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.narrapay.data.local.relation.AtribuicaoComPessoa> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.narrapay.data.local.relation.TransacaoCompleta copy(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.TransacaoEntity transacao, @org.jetbrains.annotations.Nullable()
    com.narrapay.data.local.entity.CategoriaEntity categoria, @org.jetbrains.annotations.NotNull()
    java.util.List<com.narrapay.data.local.relation.AtribuicaoComPessoa> atribuicoes) {
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