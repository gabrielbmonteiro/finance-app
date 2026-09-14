package com.narrapay.ui.ajustes.categorias;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2 = {"Lcom/narrapay/ui/ajustes/categorias/CategoriaViewModel;", "Landroidx/lifecycle/ViewModel;", "categoriaDao", "Lcom/narrapay/data/local/dao/CategoriaDao;", "(Lcom/narrapay/data/local/dao/CategoriaDao;)V", "categorias", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/narrapay/data/local/entity/CategoriaEntity;", "getCategorias", "()Lkotlinx/coroutines/flow/StateFlow;", "excluirCategoria", "", "categoria", "salvarCategoria", "app_debug"})
public final class CategoriaViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.CategoriaDao categoriaDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.narrapay.data.local.entity.CategoriaEntity>> categorias = null;
    
    public CategoriaViewModel(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.CategoriaDao categoriaDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.narrapay.data.local.entity.CategoriaEntity>> getCategorias() {
        return null;
    }
    
    public final void salvarCategoria(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.CategoriaEntity categoria) {
    }
    
    public final void excluirCategoria(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.CategoriaEntity categoria) {
    }
}