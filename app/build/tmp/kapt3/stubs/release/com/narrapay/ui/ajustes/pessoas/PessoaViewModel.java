package com.narrapay.ui.ajustes.pessoas;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2 = {"Lcom/narrapay/ui/ajustes/pessoas/PessoaViewModel;", "Landroidx/lifecycle/ViewModel;", "pessoaDao", "Lcom/narrapay/data/local/dao/PessoaDao;", "(Lcom/narrapay/data/local/dao/PessoaDao;)V", "pessoas", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/narrapay/data/local/entity/PessoaEntity;", "getPessoas", "()Lkotlinx/coroutines/flow/StateFlow;", "excluirPessoa", "", "pessoa", "salvarPessoa", "app_release"})
public final class PessoaViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.narrapay.data.local.dao.PessoaDao pessoaDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.narrapay.data.local.entity.PessoaEntity>> pessoas = null;
    
    public PessoaViewModel(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.dao.PessoaDao pessoaDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.narrapay.data.local.entity.PessoaEntity>> getPessoas() {
        return null;
    }
    
    public final void salvarPessoa(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.PessoaEntity pessoa) {
    }
    
    public final void excluirPessoa(@org.jetbrains.annotations.NotNull()
    com.narrapay.data.local.entity.PessoaEntity pessoa) {
    }
}