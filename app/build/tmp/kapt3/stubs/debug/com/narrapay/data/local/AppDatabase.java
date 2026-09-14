package com.narrapay.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&\u00a8\u0006\r"}, d2 = {"Lcom/narrapay/data/local/AppDatabase;", "Landroidx/room/RoomDatabase;", "()V", "categoriaDao", "Lcom/narrapay/data/local/dao/CategoriaDao;", "configuracoesDao", "Lcom/narrapay/data/local/dao/ConfiguracoesDao;", "pessoaDao", "Lcom/narrapay/data/local/dao/PessoaDao;", "regraAutomacaoDao", "Lcom/narrapay/data/local/dao/RegraAutomacaoDao;", "transacaoDao", "Lcom/narrapay/data/local/dao/TransacaoDao;", "app_debug"})
@androidx.room.Database(entities = {com.narrapay.data.local.entity.PessoaEntity.class, com.narrapay.data.local.entity.CategoriaEntity.class, com.narrapay.data.local.entity.TransacaoEntity.class, com.narrapay.data.local.entity.AtribuicaoTransacaoEntity.class, com.narrapay.data.local.entity.RegraAutomacaoEntity.class, com.narrapay.data.local.entity.ConfiguracoesEntity.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends androidx.room.RoomDatabase {
    
    public AppDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.narrapay.data.local.dao.TransacaoDao transacaoDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.narrapay.data.local.dao.PessoaDao pessoaDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.narrapay.data.local.dao.CategoriaDao categoriaDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.narrapay.data.local.dao.RegraAutomacaoDao regraAutomacaoDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.narrapay.data.local.dao.ConfiguracoesDao configuracoesDao();
}