package com.narrapay.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.narrapay.data.local.dao.CategoriaDao;
import com.narrapay.data.local.dao.CategoriaDao_Impl;
import com.narrapay.data.local.dao.ConfiguracoesDao;
import com.narrapay.data.local.dao.ConfiguracoesDao_Impl;
import com.narrapay.data.local.dao.PessoaDao;
import com.narrapay.data.local.dao.PessoaDao_Impl;
import com.narrapay.data.local.dao.RegraAutomacaoDao;
import com.narrapay.data.local.dao.RegraAutomacaoDao_Impl;
import com.narrapay.data.local.dao.TransacaoDao;
import com.narrapay.data.local.dao.TransacaoDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TransacaoDao _transacaoDao;

  private volatile PessoaDao _pessoaDao;

  private volatile CategoriaDao _categoriaDao;

  private volatile RegraAutomacaoDao _regraAutomacaoDao;

  private volatile ConfiguracoesDao _configuracoesDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `pessoas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nome` TEXT NOT NULL, `corHex` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `categorias` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nome` TEXT NOT NULL, `icone` TEXT NOT NULL, `corHex` TEXT NOT NULL, `categoriaPaiId` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `transacoes` (`id` TEXT NOT NULL, `valorTotal` REAL NOT NULL, `descricaoOriginal` TEXT NOT NULL, `descricaoCustomizada` TEXT, `dataHora` INTEGER NOT NULL, `categoriaId` INTEGER, `nota` TEXT, `latitude` REAL, `longitude` REAL, `statusAtribuicao` TEXT NOT NULL, `numeroParcela` INTEGER, `totalParcelas` INTEGER, PRIMARY KEY(`id`), FOREIGN KEY(`categoriaId`) REFERENCES `categorias`(`id`) ON UPDATE NO ACTION ON DELETE SET NULL )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_transacoes_categoriaId` ON `transacoes` (`categoriaId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `atribuicoes_transacao` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `transacaoId` TEXT NOT NULL, `pessoaId` INTEGER NOT NULL, `valorAtribuido` REAL NOT NULL, FOREIGN KEY(`transacaoId`) REFERENCES `transacoes`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`pessoaId`) REFERENCES `pessoas`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_atribuicoes_transacao_transacaoId` ON `atribuicoes_transacao` (`transacaoId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_atribuicoes_transacao_pessoaId` ON `atribuicoes_transacao` (`pessoaId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `regras_automacao` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `termoBusca` TEXT NOT NULL, `categoriaIdPadrao` INTEGER, `pessoaIdPadrao` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `configuracoes` (`id` INTEGER NOT NULL, `diaFechamento` INTEGER NOT NULL, `diaVencimento` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'ce9eda642e7e3cb4d3c6917f090164d0')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `pessoas`");
        db.execSQL("DROP TABLE IF EXISTS `categorias`");
        db.execSQL("DROP TABLE IF EXISTS `transacoes`");
        db.execSQL("DROP TABLE IF EXISTS `atribuicoes_transacao`");
        db.execSQL("DROP TABLE IF EXISTS `regras_automacao`");
        db.execSQL("DROP TABLE IF EXISTS `configuracoes`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPessoas = new HashMap<String, TableInfo.Column>(3);
        _columnsPessoas.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPessoas.put("nome", new TableInfo.Column("nome", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPessoas.put("corHex", new TableInfo.Column("corHex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPessoas = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPessoas = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPessoas = new TableInfo("pessoas", _columnsPessoas, _foreignKeysPessoas, _indicesPessoas);
        final TableInfo _existingPessoas = TableInfo.read(db, "pessoas");
        if (!_infoPessoas.equals(_existingPessoas)) {
          return new RoomOpenHelper.ValidationResult(false, "pessoas(com.narrapay.data.local.entity.PessoaEntity).\n"
                  + " Expected:\n" + _infoPessoas + "\n"
                  + " Found:\n" + _existingPessoas);
        }
        final HashMap<String, TableInfo.Column> _columnsCategorias = new HashMap<String, TableInfo.Column>(5);
        _columnsCategorias.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategorias.put("nome", new TableInfo.Column("nome", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategorias.put("icone", new TableInfo.Column("icone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategorias.put("corHex", new TableInfo.Column("corHex", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCategorias.put("categoriaPaiId", new TableInfo.Column("categoriaPaiId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCategorias = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCategorias = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCategorias = new TableInfo("categorias", _columnsCategorias, _foreignKeysCategorias, _indicesCategorias);
        final TableInfo _existingCategorias = TableInfo.read(db, "categorias");
        if (!_infoCategorias.equals(_existingCategorias)) {
          return new RoomOpenHelper.ValidationResult(false, "categorias(com.narrapay.data.local.entity.CategoriaEntity).\n"
                  + " Expected:\n" + _infoCategorias + "\n"
                  + " Found:\n" + _existingCategorias);
        }
        final HashMap<String, TableInfo.Column> _columnsTransacoes = new HashMap<String, TableInfo.Column>(12);
        _columnsTransacoes.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransacoes.put("valorTotal", new TableInfo.Column("valorTotal", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransacoes.put("descricaoOriginal", new TableInfo.Column("descricaoOriginal", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransacoes.put("descricaoCustomizada", new TableInfo.Column("descricaoCustomizada", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransacoes.put("dataHora", new TableInfo.Column("dataHora", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransacoes.put("categoriaId", new TableInfo.Column("categoriaId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransacoes.put("nota", new TableInfo.Column("nota", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransacoes.put("latitude", new TableInfo.Column("latitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransacoes.put("longitude", new TableInfo.Column("longitude", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransacoes.put("statusAtribuicao", new TableInfo.Column("statusAtribuicao", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransacoes.put("numeroParcela", new TableInfo.Column("numeroParcela", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTransacoes.put("totalParcelas", new TableInfo.Column("totalParcelas", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTransacoes = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTransacoes.add(new TableInfo.ForeignKey("categorias", "SET NULL", "NO ACTION", Arrays.asList("categoriaId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesTransacoes = new HashSet<TableInfo.Index>(1);
        _indicesTransacoes.add(new TableInfo.Index("index_transacoes_categoriaId", false, Arrays.asList("categoriaId"), Arrays.asList("ASC")));
        final TableInfo _infoTransacoes = new TableInfo("transacoes", _columnsTransacoes, _foreignKeysTransacoes, _indicesTransacoes);
        final TableInfo _existingTransacoes = TableInfo.read(db, "transacoes");
        if (!_infoTransacoes.equals(_existingTransacoes)) {
          return new RoomOpenHelper.ValidationResult(false, "transacoes(com.narrapay.data.local.entity.TransacaoEntity).\n"
                  + " Expected:\n" + _infoTransacoes + "\n"
                  + " Found:\n" + _existingTransacoes);
        }
        final HashMap<String, TableInfo.Column> _columnsAtribuicoesTransacao = new HashMap<String, TableInfo.Column>(4);
        _columnsAtribuicoesTransacao.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAtribuicoesTransacao.put("transacaoId", new TableInfo.Column("transacaoId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAtribuicoesTransacao.put("pessoaId", new TableInfo.Column("pessoaId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAtribuicoesTransacao.put("valorAtribuido", new TableInfo.Column("valorAtribuido", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAtribuicoesTransacao = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysAtribuicoesTransacao.add(new TableInfo.ForeignKey("transacoes", "CASCADE", "NO ACTION", Arrays.asList("transacaoId"), Arrays.asList("id")));
        _foreignKeysAtribuicoesTransacao.add(new TableInfo.ForeignKey("pessoas", "CASCADE", "NO ACTION", Arrays.asList("pessoaId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesAtribuicoesTransacao = new HashSet<TableInfo.Index>(2);
        _indicesAtribuicoesTransacao.add(new TableInfo.Index("index_atribuicoes_transacao_transacaoId", false, Arrays.asList("transacaoId"), Arrays.asList("ASC")));
        _indicesAtribuicoesTransacao.add(new TableInfo.Index("index_atribuicoes_transacao_pessoaId", false, Arrays.asList("pessoaId"), Arrays.asList("ASC")));
        final TableInfo _infoAtribuicoesTransacao = new TableInfo("atribuicoes_transacao", _columnsAtribuicoesTransacao, _foreignKeysAtribuicoesTransacao, _indicesAtribuicoesTransacao);
        final TableInfo _existingAtribuicoesTransacao = TableInfo.read(db, "atribuicoes_transacao");
        if (!_infoAtribuicoesTransacao.equals(_existingAtribuicoesTransacao)) {
          return new RoomOpenHelper.ValidationResult(false, "atribuicoes_transacao(com.narrapay.data.local.entity.AtribuicaoTransacaoEntity).\n"
                  + " Expected:\n" + _infoAtribuicoesTransacao + "\n"
                  + " Found:\n" + _existingAtribuicoesTransacao);
        }
        final HashMap<String, TableInfo.Column> _columnsRegrasAutomacao = new HashMap<String, TableInfo.Column>(4);
        _columnsRegrasAutomacao.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegrasAutomacao.put("termoBusca", new TableInfo.Column("termoBusca", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegrasAutomacao.put("categoriaIdPadrao", new TableInfo.Column("categoriaIdPadrao", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRegrasAutomacao.put("pessoaIdPadrao", new TableInfo.Column("pessoaIdPadrao", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRegrasAutomacao = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRegrasAutomacao = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRegrasAutomacao = new TableInfo("regras_automacao", _columnsRegrasAutomacao, _foreignKeysRegrasAutomacao, _indicesRegrasAutomacao);
        final TableInfo _existingRegrasAutomacao = TableInfo.read(db, "regras_automacao");
        if (!_infoRegrasAutomacao.equals(_existingRegrasAutomacao)) {
          return new RoomOpenHelper.ValidationResult(false, "regras_automacao(com.narrapay.data.local.entity.RegraAutomacaoEntity).\n"
                  + " Expected:\n" + _infoRegrasAutomacao + "\n"
                  + " Found:\n" + _existingRegrasAutomacao);
        }
        final HashMap<String, TableInfo.Column> _columnsConfiguracoes = new HashMap<String, TableInfo.Column>(3);
        _columnsConfiguracoes.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConfiguracoes.put("diaFechamento", new TableInfo.Column("diaFechamento", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsConfiguracoes.put("diaVencimento", new TableInfo.Column("diaVencimento", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysConfiguracoes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesConfiguracoes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoConfiguracoes = new TableInfo("configuracoes", _columnsConfiguracoes, _foreignKeysConfiguracoes, _indicesConfiguracoes);
        final TableInfo _existingConfiguracoes = TableInfo.read(db, "configuracoes");
        if (!_infoConfiguracoes.equals(_existingConfiguracoes)) {
          return new RoomOpenHelper.ValidationResult(false, "configuracoes(com.narrapay.data.local.entity.ConfiguracoesEntity).\n"
                  + " Expected:\n" + _infoConfiguracoes + "\n"
                  + " Found:\n" + _existingConfiguracoes);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "ce9eda642e7e3cb4d3c6917f090164d0", "69394989e49f5a6ace3bc675ce688bcb");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "pessoas","categorias","transacoes","atribuicoes_transacao","regras_automacao","configuracoes");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `pessoas`");
      _db.execSQL("DELETE FROM `categorias`");
      _db.execSQL("DELETE FROM `transacoes`");
      _db.execSQL("DELETE FROM `atribuicoes_transacao`");
      _db.execSQL("DELETE FROM `regras_automacao`");
      _db.execSQL("DELETE FROM `configuracoes`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TransacaoDao.class, TransacaoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PessoaDao.class, PessoaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CategoriaDao.class, CategoriaDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RegraAutomacaoDao.class, RegraAutomacaoDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ConfiguracoesDao.class, ConfiguracoesDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TransacaoDao transacaoDao() {
    if (_transacaoDao != null) {
      return _transacaoDao;
    } else {
      synchronized(this) {
        if(_transacaoDao == null) {
          _transacaoDao = new TransacaoDao_Impl(this);
        }
        return _transacaoDao;
      }
    }
  }

  @Override
  public PessoaDao pessoaDao() {
    if (_pessoaDao != null) {
      return _pessoaDao;
    } else {
      synchronized(this) {
        if(_pessoaDao == null) {
          _pessoaDao = new PessoaDao_Impl(this);
        }
        return _pessoaDao;
      }
    }
  }

  @Override
  public CategoriaDao categoriaDao() {
    if (_categoriaDao != null) {
      return _categoriaDao;
    } else {
      synchronized(this) {
        if(_categoriaDao == null) {
          _categoriaDao = new CategoriaDao_Impl(this);
        }
        return _categoriaDao;
      }
    }
  }

  @Override
  public RegraAutomacaoDao regraAutomacaoDao() {
    if (_regraAutomacaoDao != null) {
      return _regraAutomacaoDao;
    } else {
      synchronized(this) {
        if(_regraAutomacaoDao == null) {
          _regraAutomacaoDao = new RegraAutomacaoDao_Impl(this);
        }
        return _regraAutomacaoDao;
      }
    }
  }

  @Override
  public ConfiguracoesDao configuracoesDao() {
    if (_configuracoesDao != null) {
      return _configuracoesDao;
    } else {
      synchronized(this) {
        if(_configuracoesDao == null) {
          _configuracoesDao = new ConfiguracoesDao_Impl(this);
        }
        return _configuracoesDao;
      }
    }
  }
}
