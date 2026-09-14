package com.narrapay.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.narrapay.data.local.entity.AtribuicaoTransacaoEntity;
import com.narrapay.data.local.entity.CategoriaEntity;
import com.narrapay.data.local.entity.PessoaEntity;
import com.narrapay.data.local.entity.TransacaoEntity;
import com.narrapay.data.local.relation.AtribuicaoComPessoa;
import com.narrapay.data.local.relation.TransacaoCompleta;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TransacaoDao_Impl implements TransacaoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TransacaoEntity> __insertionAdapterOfTransacaoEntity;

  private final EntityInsertionAdapter<AtribuicaoTransacaoEntity> __insertionAdapterOfAtribuicaoTransacaoEntity;

  private final EntityDeletionOrUpdateAdapter<TransacaoEntity> __deletionAdapterOfTransacaoEntity;

  private final EntityDeletionOrUpdateAdapter<TransacaoEntity> __updateAdapterOfTransacaoEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAtribuicoesDaTransacao;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTransacaoById;

  public TransacaoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransacaoEntity = new EntityInsertionAdapter<TransacaoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `transacoes` (`id`,`valorTotal`,`descricaoOriginal`,`descricaoCustomizada`,`dataHora`,`categoriaId`,`nota`,`latitude`,`longitude`,`statusAtribuicao`,`numeroParcela`,`totalParcelas`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransacaoEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        statement.bindDouble(2, entity.getValorTotal());
        if (entity.getDescricaoOriginal() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescricaoOriginal());
        }
        if (entity.getDescricaoCustomizada() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescricaoCustomizada());
        }
        statement.bindLong(5, entity.getDataHora());
        if (entity.getCategoriaId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getCategoriaId());
        }
        if (entity.getNota() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNota());
        }
        if (entity.getLatitude() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getLongitude());
        }
        if (entity.getStatusAtribuicao() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getStatusAtribuicao());
        }
        if (entity.getNumeroParcela() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getNumeroParcela());
        }
        if (entity.getTotalParcelas() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getTotalParcelas());
        }
      }
    };
    this.__insertionAdapterOfAtribuicaoTransacaoEntity = new EntityInsertionAdapter<AtribuicaoTransacaoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `atribuicoes_transacao` (`id`,`transacaoId`,`pessoaId`,`valorAtribuido`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AtribuicaoTransacaoEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTransacaoId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTransacaoId());
        }
        statement.bindLong(3, entity.getPessoaId());
        statement.bindDouble(4, entity.getValorAtribuido());
      }
    };
    this.__deletionAdapterOfTransacaoEntity = new EntityDeletionOrUpdateAdapter<TransacaoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `transacoes` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransacaoEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__updateAdapterOfTransacaoEntity = new EntityDeletionOrUpdateAdapter<TransacaoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `transacoes` SET `id` = ?,`valorTotal` = ?,`descricaoOriginal` = ?,`descricaoCustomizada` = ?,`dataHora` = ?,`categoriaId` = ?,`nota` = ?,`latitude` = ?,`longitude` = ?,`statusAtribuicao` = ?,`numeroParcela` = ?,`totalParcelas` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TransacaoEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        statement.bindDouble(2, entity.getValorTotal());
        if (entity.getDescricaoOriginal() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getDescricaoOriginal());
        }
        if (entity.getDescricaoCustomizada() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescricaoCustomizada());
        }
        statement.bindLong(5, entity.getDataHora());
        if (entity.getCategoriaId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getCategoriaId());
        }
        if (entity.getNota() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNota());
        }
        if (entity.getLatitude() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getLatitude());
        }
        if (entity.getLongitude() == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.getLongitude());
        }
        if (entity.getStatusAtribuicao() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getStatusAtribuicao());
        }
        if (entity.getNumeroParcela() == null) {
          statement.bindNull(11);
        } else {
          statement.bindLong(11, entity.getNumeroParcela());
        }
        if (entity.getTotalParcelas() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getTotalParcelas());
        }
        if (entity.getId() == null) {
          statement.bindNull(13);
        } else {
          statement.bindString(13, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteAtribuicoesDaTransacao = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM atribuicoes_transacao WHERE transacaoId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteTransacaoById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transacoes WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTransacao(final TransacaoEntity transacao,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTransacaoEntity.insertAndReturnId(transacao);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAtribuicoes(final List<AtribuicaoTransacaoEntity> atribuicoes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAtribuicaoTransacaoEntity.insert(atribuicoes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTransacao(final TransacaoEntity transacao,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTransacaoEntity.handle(transacao);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTransacao(final TransacaoEntity transacao,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTransacaoEntity.handle(transacao);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAtribuicoesDaTransacao(final String transacaoId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAtribuicoesDaTransacao.acquire();
        int _argIndex = 1;
        if (transacaoId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, transacaoId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAtribuicoesDaTransacao.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTransacaoById(final String transacaoId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTransacaoById.acquire();
        int _argIndex = 1;
        if (transacaoId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, transacaoId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteTransacaoById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getTransacaoById(final String id,
      final Continuation<? super TransacaoEntity> $completion) {
    final String _sql = "SELECT * FROM transacoes WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<TransacaoEntity>() {
      @Override
      @Nullable
      public TransacaoEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfValorTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "valorTotal");
          final int _cursorIndexOfDescricaoOriginal = CursorUtil.getColumnIndexOrThrow(_cursor, "descricaoOriginal");
          final int _cursorIndexOfDescricaoCustomizada = CursorUtil.getColumnIndexOrThrow(_cursor, "descricaoCustomizada");
          final int _cursorIndexOfDataHora = CursorUtil.getColumnIndexOrThrow(_cursor, "dataHora");
          final int _cursorIndexOfCategoriaId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoriaId");
          final int _cursorIndexOfNota = CursorUtil.getColumnIndexOrThrow(_cursor, "nota");
          final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
          final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
          final int _cursorIndexOfStatusAtribuicao = CursorUtil.getColumnIndexOrThrow(_cursor, "statusAtribuicao");
          final int _cursorIndexOfNumeroParcela = CursorUtil.getColumnIndexOrThrow(_cursor, "numeroParcela");
          final int _cursorIndexOfTotalParcelas = CursorUtil.getColumnIndexOrThrow(_cursor, "totalParcelas");
          final TransacaoEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final double _tmpValorTotal;
            _tmpValorTotal = _cursor.getDouble(_cursorIndexOfValorTotal);
            final String _tmpDescricaoOriginal;
            if (_cursor.isNull(_cursorIndexOfDescricaoOriginal)) {
              _tmpDescricaoOriginal = null;
            } else {
              _tmpDescricaoOriginal = _cursor.getString(_cursorIndexOfDescricaoOriginal);
            }
            final String _tmpDescricaoCustomizada;
            if (_cursor.isNull(_cursorIndexOfDescricaoCustomizada)) {
              _tmpDescricaoCustomizada = null;
            } else {
              _tmpDescricaoCustomizada = _cursor.getString(_cursorIndexOfDescricaoCustomizada);
            }
            final long _tmpDataHora;
            _tmpDataHora = _cursor.getLong(_cursorIndexOfDataHora);
            final Long _tmpCategoriaId;
            if (_cursor.isNull(_cursorIndexOfCategoriaId)) {
              _tmpCategoriaId = null;
            } else {
              _tmpCategoriaId = _cursor.getLong(_cursorIndexOfCategoriaId);
            }
            final String _tmpNota;
            if (_cursor.isNull(_cursorIndexOfNota)) {
              _tmpNota = null;
            } else {
              _tmpNota = _cursor.getString(_cursorIndexOfNota);
            }
            final Double _tmpLatitude;
            if (_cursor.isNull(_cursorIndexOfLatitude)) {
              _tmpLatitude = null;
            } else {
              _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
            }
            final Double _tmpLongitude;
            if (_cursor.isNull(_cursorIndexOfLongitude)) {
              _tmpLongitude = null;
            } else {
              _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
            }
            final String _tmpStatusAtribuicao;
            if (_cursor.isNull(_cursorIndexOfStatusAtribuicao)) {
              _tmpStatusAtribuicao = null;
            } else {
              _tmpStatusAtribuicao = _cursor.getString(_cursorIndexOfStatusAtribuicao);
            }
            final Integer _tmpNumeroParcela;
            if (_cursor.isNull(_cursorIndexOfNumeroParcela)) {
              _tmpNumeroParcela = null;
            } else {
              _tmpNumeroParcela = _cursor.getInt(_cursorIndexOfNumeroParcela);
            }
            final Integer _tmpTotalParcelas;
            if (_cursor.isNull(_cursorIndexOfTotalParcelas)) {
              _tmpTotalParcelas = null;
            } else {
              _tmpTotalParcelas = _cursor.getInt(_cursorIndexOfTotalParcelas);
            }
            _result = new TransacaoEntity(_tmpId,_tmpValorTotal,_tmpDescricaoOriginal,_tmpDescricaoCustomizada,_tmpDataHora,_tmpCategoriaId,_tmpNota,_tmpLatitude,_tmpLongitude,_tmpStatusAtribuicao,_tmpNumeroParcela,_tmpTotalParcelas);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getTransacaoCompletaById(final String id,
      final Continuation<? super TransacaoCompleta> $completion) {
    final String _sql = "SELECT * FROM transacoes WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, true, _cancellationSignal, new Callable<TransacaoCompleta>() {
      @Override
      @Nullable
      public TransacaoCompleta call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfValorTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "valorTotal");
            final int _cursorIndexOfDescricaoOriginal = CursorUtil.getColumnIndexOrThrow(_cursor, "descricaoOriginal");
            final int _cursorIndexOfDescricaoCustomizada = CursorUtil.getColumnIndexOrThrow(_cursor, "descricaoCustomizada");
            final int _cursorIndexOfDataHora = CursorUtil.getColumnIndexOrThrow(_cursor, "dataHora");
            final int _cursorIndexOfCategoriaId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoriaId");
            final int _cursorIndexOfNota = CursorUtil.getColumnIndexOrThrow(_cursor, "nota");
            final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
            final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
            final int _cursorIndexOfStatusAtribuicao = CursorUtil.getColumnIndexOrThrow(_cursor, "statusAtribuicao");
            final int _cursorIndexOfNumeroParcela = CursorUtil.getColumnIndexOrThrow(_cursor, "numeroParcela");
            final int _cursorIndexOfTotalParcelas = CursorUtil.getColumnIndexOrThrow(_cursor, "totalParcelas");
            final LongSparseArray<CategoriaEntity> _collectionCategoria = new LongSparseArray<CategoriaEntity>();
            final ArrayMap<String, ArrayList<AtribuicaoComPessoa>> _collectionAtribuicoes = new ArrayMap<String, ArrayList<AtribuicaoComPessoa>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoriaId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoriaId);
              }
              if (_tmpKey != null) {
                _collectionCategoria.put(_tmpKey, null);
              }
              final String _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfId)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getString(_cursorIndexOfId);
              }
              if (_tmpKey_1 != null) {
                if (!_collectionAtribuicoes.containsKey(_tmpKey_1)) {
                  _collectionAtribuicoes.put(_tmpKey_1, new ArrayList<AtribuicaoComPessoa>());
                }
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriasAscomNarrapayDataLocalEntityCategoriaEntity(_collectionCategoria);
            __fetchRelationshipatribuicoesTransacaoAscomNarrapayDataLocalRelationAtribuicaoComPessoa(_collectionAtribuicoes);
            final TransacaoCompleta _result;
            if (_cursor.moveToFirst()) {
              final TransacaoEntity _tmpTransacao;
              final String _tmpId;
              if (_cursor.isNull(_cursorIndexOfId)) {
                _tmpId = null;
              } else {
                _tmpId = _cursor.getString(_cursorIndexOfId);
              }
              final double _tmpValorTotal;
              _tmpValorTotal = _cursor.getDouble(_cursorIndexOfValorTotal);
              final String _tmpDescricaoOriginal;
              if (_cursor.isNull(_cursorIndexOfDescricaoOriginal)) {
                _tmpDescricaoOriginal = null;
              } else {
                _tmpDescricaoOriginal = _cursor.getString(_cursorIndexOfDescricaoOriginal);
              }
              final String _tmpDescricaoCustomizada;
              if (_cursor.isNull(_cursorIndexOfDescricaoCustomizada)) {
                _tmpDescricaoCustomizada = null;
              } else {
                _tmpDescricaoCustomizada = _cursor.getString(_cursorIndexOfDescricaoCustomizada);
              }
              final long _tmpDataHora;
              _tmpDataHora = _cursor.getLong(_cursorIndexOfDataHora);
              final Long _tmpCategoriaId;
              if (_cursor.isNull(_cursorIndexOfCategoriaId)) {
                _tmpCategoriaId = null;
              } else {
                _tmpCategoriaId = _cursor.getLong(_cursorIndexOfCategoriaId);
              }
              final String _tmpNota;
              if (_cursor.isNull(_cursorIndexOfNota)) {
                _tmpNota = null;
              } else {
                _tmpNota = _cursor.getString(_cursorIndexOfNota);
              }
              final Double _tmpLatitude;
              if (_cursor.isNull(_cursorIndexOfLatitude)) {
                _tmpLatitude = null;
              } else {
                _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
              }
              final Double _tmpLongitude;
              if (_cursor.isNull(_cursorIndexOfLongitude)) {
                _tmpLongitude = null;
              } else {
                _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
              }
              final String _tmpStatusAtribuicao;
              if (_cursor.isNull(_cursorIndexOfStatusAtribuicao)) {
                _tmpStatusAtribuicao = null;
              } else {
                _tmpStatusAtribuicao = _cursor.getString(_cursorIndexOfStatusAtribuicao);
              }
              final Integer _tmpNumeroParcela;
              if (_cursor.isNull(_cursorIndexOfNumeroParcela)) {
                _tmpNumeroParcela = null;
              } else {
                _tmpNumeroParcela = _cursor.getInt(_cursorIndexOfNumeroParcela);
              }
              final Integer _tmpTotalParcelas;
              if (_cursor.isNull(_cursorIndexOfTotalParcelas)) {
                _tmpTotalParcelas = null;
              } else {
                _tmpTotalParcelas = _cursor.getInt(_cursorIndexOfTotalParcelas);
              }
              _tmpTransacao = new TransacaoEntity(_tmpId,_tmpValorTotal,_tmpDescricaoOriginal,_tmpDescricaoCustomizada,_tmpDataHora,_tmpCategoriaId,_tmpNota,_tmpLatitude,_tmpLongitude,_tmpStatusAtribuicao,_tmpNumeroParcela,_tmpTotalParcelas);
              final CategoriaEntity _tmpCategoria;
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfCategoriaId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfCategoriaId);
              }
              if (_tmpKey_2 != null) {
                _tmpCategoria = _collectionCategoria.get(_tmpKey_2);
              } else {
                _tmpCategoria = null;
              }
              final ArrayList<AtribuicaoComPessoa> _tmpAtribuicoesCollection;
              final String _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getString(_cursorIndexOfId);
              }
              if (_tmpKey_3 != null) {
                _tmpAtribuicoesCollection = _collectionAtribuicoes.get(_tmpKey_3);
              } else {
                _tmpAtribuicoesCollection = new ArrayList<AtribuicaoComPessoa>();
              }
              _result = new TransacaoCompleta(_tmpTransacao,_tmpCategoria,_tmpAtribuicoesCollection);
            } else {
              _result = null;
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
            _statement.release();
          }
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<TransacaoCompleta>> getTransacoesPendentes() {
    final String _sql = "SELECT * FROM transacoes WHERE statusAtribuicao IN ('PENDENTE', 'IGNORADO')";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categorias", "pessoas",
        "atribuicoes_transacao", "transacoes"}, new Callable<List<TransacaoCompleta>>() {
      @Override
      @NonNull
      public List<TransacaoCompleta> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfValorTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "valorTotal");
            final int _cursorIndexOfDescricaoOriginal = CursorUtil.getColumnIndexOrThrow(_cursor, "descricaoOriginal");
            final int _cursorIndexOfDescricaoCustomizada = CursorUtil.getColumnIndexOrThrow(_cursor, "descricaoCustomizada");
            final int _cursorIndexOfDataHora = CursorUtil.getColumnIndexOrThrow(_cursor, "dataHora");
            final int _cursorIndexOfCategoriaId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoriaId");
            final int _cursorIndexOfNota = CursorUtil.getColumnIndexOrThrow(_cursor, "nota");
            final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
            final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
            final int _cursorIndexOfStatusAtribuicao = CursorUtil.getColumnIndexOrThrow(_cursor, "statusAtribuicao");
            final int _cursorIndexOfNumeroParcela = CursorUtil.getColumnIndexOrThrow(_cursor, "numeroParcela");
            final int _cursorIndexOfTotalParcelas = CursorUtil.getColumnIndexOrThrow(_cursor, "totalParcelas");
            final LongSparseArray<CategoriaEntity> _collectionCategoria = new LongSparseArray<CategoriaEntity>();
            final ArrayMap<String, ArrayList<AtribuicaoComPessoa>> _collectionAtribuicoes = new ArrayMap<String, ArrayList<AtribuicaoComPessoa>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoriaId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoriaId);
              }
              if (_tmpKey != null) {
                _collectionCategoria.put(_tmpKey, null);
              }
              final String _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfId)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getString(_cursorIndexOfId);
              }
              if (_tmpKey_1 != null) {
                if (!_collectionAtribuicoes.containsKey(_tmpKey_1)) {
                  _collectionAtribuicoes.put(_tmpKey_1, new ArrayList<AtribuicaoComPessoa>());
                }
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriasAscomNarrapayDataLocalEntityCategoriaEntity(_collectionCategoria);
            __fetchRelationshipatribuicoesTransacaoAscomNarrapayDataLocalRelationAtribuicaoComPessoa(_collectionAtribuicoes);
            final List<TransacaoCompleta> _result = new ArrayList<TransacaoCompleta>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransacaoCompleta _item;
              final TransacaoEntity _tmpTransacao;
              final String _tmpId;
              if (_cursor.isNull(_cursorIndexOfId)) {
                _tmpId = null;
              } else {
                _tmpId = _cursor.getString(_cursorIndexOfId);
              }
              final double _tmpValorTotal;
              _tmpValorTotal = _cursor.getDouble(_cursorIndexOfValorTotal);
              final String _tmpDescricaoOriginal;
              if (_cursor.isNull(_cursorIndexOfDescricaoOriginal)) {
                _tmpDescricaoOriginal = null;
              } else {
                _tmpDescricaoOriginal = _cursor.getString(_cursorIndexOfDescricaoOriginal);
              }
              final String _tmpDescricaoCustomizada;
              if (_cursor.isNull(_cursorIndexOfDescricaoCustomizada)) {
                _tmpDescricaoCustomizada = null;
              } else {
                _tmpDescricaoCustomizada = _cursor.getString(_cursorIndexOfDescricaoCustomizada);
              }
              final long _tmpDataHora;
              _tmpDataHora = _cursor.getLong(_cursorIndexOfDataHora);
              final Long _tmpCategoriaId;
              if (_cursor.isNull(_cursorIndexOfCategoriaId)) {
                _tmpCategoriaId = null;
              } else {
                _tmpCategoriaId = _cursor.getLong(_cursorIndexOfCategoriaId);
              }
              final String _tmpNota;
              if (_cursor.isNull(_cursorIndexOfNota)) {
                _tmpNota = null;
              } else {
                _tmpNota = _cursor.getString(_cursorIndexOfNota);
              }
              final Double _tmpLatitude;
              if (_cursor.isNull(_cursorIndexOfLatitude)) {
                _tmpLatitude = null;
              } else {
                _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
              }
              final Double _tmpLongitude;
              if (_cursor.isNull(_cursorIndexOfLongitude)) {
                _tmpLongitude = null;
              } else {
                _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
              }
              final String _tmpStatusAtribuicao;
              if (_cursor.isNull(_cursorIndexOfStatusAtribuicao)) {
                _tmpStatusAtribuicao = null;
              } else {
                _tmpStatusAtribuicao = _cursor.getString(_cursorIndexOfStatusAtribuicao);
              }
              final Integer _tmpNumeroParcela;
              if (_cursor.isNull(_cursorIndexOfNumeroParcela)) {
                _tmpNumeroParcela = null;
              } else {
                _tmpNumeroParcela = _cursor.getInt(_cursorIndexOfNumeroParcela);
              }
              final Integer _tmpTotalParcelas;
              if (_cursor.isNull(_cursorIndexOfTotalParcelas)) {
                _tmpTotalParcelas = null;
              } else {
                _tmpTotalParcelas = _cursor.getInt(_cursorIndexOfTotalParcelas);
              }
              _tmpTransacao = new TransacaoEntity(_tmpId,_tmpValorTotal,_tmpDescricaoOriginal,_tmpDescricaoCustomizada,_tmpDataHora,_tmpCategoriaId,_tmpNota,_tmpLatitude,_tmpLongitude,_tmpStatusAtribuicao,_tmpNumeroParcela,_tmpTotalParcelas);
              final CategoriaEntity _tmpCategoria;
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfCategoriaId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfCategoriaId);
              }
              if (_tmpKey_2 != null) {
                _tmpCategoria = _collectionCategoria.get(_tmpKey_2);
              } else {
                _tmpCategoria = null;
              }
              final ArrayList<AtribuicaoComPessoa> _tmpAtribuicoesCollection;
              final String _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getString(_cursorIndexOfId);
              }
              if (_tmpKey_3 != null) {
                _tmpAtribuicoesCollection = _collectionAtribuicoes.get(_tmpKey_3);
              } else {
                _tmpAtribuicoesCollection = new ArrayList<AtribuicaoComPessoa>();
              }
              _item = new TransacaoCompleta(_tmpTransacao,_tmpCategoria,_tmpAtribuicoesCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<GastoAgrupado>> getGastosAgrupadosPorPessoa(final long inicioTimestamp,
      final long fimTimestamp) {
    final String _sql = "\n"
            + "        SELECT a.pessoaId, COALESCE(SUM(a.valorAtribuido), 0.0) as totalGasto\n"
            + "        FROM atribuicoes_transacao a \n"
            + "        INNER JOIN transacoes t ON a.transacaoId = t.id \n"
            + "        WHERE t.dataHora BETWEEN ? AND ?\n"
            + "        GROUP BY a.pessoaId\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, inicioTimestamp);
    _argIndex = 2;
    _statement.bindLong(_argIndex, fimTimestamp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"atribuicoes_transacao",
        "transacoes"}, new Callable<List<GastoAgrupado>>() {
      @Override
      @NonNull
      public List<GastoAgrupado> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPessoaId = 0;
          final int _cursorIndexOfTotalGasto = 1;
          final List<GastoAgrupado> _result = new ArrayList<GastoAgrupado>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GastoAgrupado _item;
            final long _tmpPessoaId;
            _tmpPessoaId = _cursor.getLong(_cursorIndexOfPessoaId);
            final double _tmpTotalGasto;
            _tmpTotalGasto = _cursor.getDouble(_cursorIndexOfTotalGasto);
            _item = new GastoAgrupado(_tmpPessoaId,_tmpTotalGasto);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getGastoTotalPorPessoa(final long pessoaId, final long inicioTimestamp,
      final long fimTimestamp) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(a.valorAtribuido), 0.0) \n"
            + "        FROM atribuicoes_transacao a \n"
            + "        INNER JOIN transacoes t ON a.transacaoId = t.id \n"
            + "        WHERE a.pessoaId = ? \n"
            + "        AND t.dataHora BETWEEN ? AND ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, pessoaId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, inicioTimestamp);
    _argIndex = 3;
    _statement.bindLong(_argIndex, fimTimestamp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"atribuicoes_transacao",
        "transacoes"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getGastoTotalMes(final long inicioTimestamp, final long fimTimestamp) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(valorTotal), 0.0)\n"
            + "        FROM transacoes\n"
            + "        WHERE dataHora BETWEEN ? AND ?\n"
            + "        AND statusAtribuicao != 'IGNORADO'\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, inicioTimestamp);
    _argIndex = 2;
    _statement.bindLong(_argIndex, fimTimestamp);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transacoes"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final Double _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getDouble(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<TransacaoCompleta>> getTransacoesFiltradas(final long inicioTimestamp,
      final long fimTimestamp, final String queryBusca) {
    final String _sql = "\n"
            + "        SELECT * FROM transacoes \n"
            + "        WHERE dataHora BETWEEN ? AND ? \n"
            + "        AND statusAtribuicao != 'IGNORADO'\n"
            + "        AND (descricaoOriginal LIKE '%' || ? || '%' \n"
            + "             OR descricaoCustomizada LIKE '%' || ? || '%' \n"
            + "             OR nota LIKE '%' || ? || '%')\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, inicioTimestamp);
    _argIndex = 2;
    _statement.bindLong(_argIndex, fimTimestamp);
    _argIndex = 3;
    if (queryBusca == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, queryBusca);
    }
    _argIndex = 4;
    if (queryBusca == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, queryBusca);
    }
    _argIndex = 5;
    if (queryBusca == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, queryBusca);
    }
    return CoroutinesRoom.createFlow(__db, true, new String[] {"categorias", "pessoas",
        "atribuicoes_transacao", "transacoes"}, new Callable<List<TransacaoCompleta>>() {
      @Override
      @NonNull
      public List<TransacaoCompleta> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfValorTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "valorTotal");
            final int _cursorIndexOfDescricaoOriginal = CursorUtil.getColumnIndexOrThrow(_cursor, "descricaoOriginal");
            final int _cursorIndexOfDescricaoCustomizada = CursorUtil.getColumnIndexOrThrow(_cursor, "descricaoCustomizada");
            final int _cursorIndexOfDataHora = CursorUtil.getColumnIndexOrThrow(_cursor, "dataHora");
            final int _cursorIndexOfCategoriaId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoriaId");
            final int _cursorIndexOfNota = CursorUtil.getColumnIndexOrThrow(_cursor, "nota");
            final int _cursorIndexOfLatitude = CursorUtil.getColumnIndexOrThrow(_cursor, "latitude");
            final int _cursorIndexOfLongitude = CursorUtil.getColumnIndexOrThrow(_cursor, "longitude");
            final int _cursorIndexOfStatusAtribuicao = CursorUtil.getColumnIndexOrThrow(_cursor, "statusAtribuicao");
            final int _cursorIndexOfNumeroParcela = CursorUtil.getColumnIndexOrThrow(_cursor, "numeroParcela");
            final int _cursorIndexOfTotalParcelas = CursorUtil.getColumnIndexOrThrow(_cursor, "totalParcelas");
            final LongSparseArray<CategoriaEntity> _collectionCategoria = new LongSparseArray<CategoriaEntity>();
            final ArrayMap<String, ArrayList<AtribuicaoComPessoa>> _collectionAtribuicoes = new ArrayMap<String, ArrayList<AtribuicaoComPessoa>>();
            while (_cursor.moveToNext()) {
              final Long _tmpKey;
              if (_cursor.isNull(_cursorIndexOfCategoriaId)) {
                _tmpKey = null;
              } else {
                _tmpKey = _cursor.getLong(_cursorIndexOfCategoriaId);
              }
              if (_tmpKey != null) {
                _collectionCategoria.put(_tmpKey, null);
              }
              final String _tmpKey_1;
              if (_cursor.isNull(_cursorIndexOfId)) {
                _tmpKey_1 = null;
              } else {
                _tmpKey_1 = _cursor.getString(_cursorIndexOfId);
              }
              if (_tmpKey_1 != null) {
                if (!_collectionAtribuicoes.containsKey(_tmpKey_1)) {
                  _collectionAtribuicoes.put(_tmpKey_1, new ArrayList<AtribuicaoComPessoa>());
                }
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipcategoriasAscomNarrapayDataLocalEntityCategoriaEntity(_collectionCategoria);
            __fetchRelationshipatribuicoesTransacaoAscomNarrapayDataLocalRelationAtribuicaoComPessoa(_collectionAtribuicoes);
            final List<TransacaoCompleta> _result = new ArrayList<TransacaoCompleta>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final TransacaoCompleta _item;
              final TransacaoEntity _tmpTransacao;
              final String _tmpId;
              if (_cursor.isNull(_cursorIndexOfId)) {
                _tmpId = null;
              } else {
                _tmpId = _cursor.getString(_cursorIndexOfId);
              }
              final double _tmpValorTotal;
              _tmpValorTotal = _cursor.getDouble(_cursorIndexOfValorTotal);
              final String _tmpDescricaoOriginal;
              if (_cursor.isNull(_cursorIndexOfDescricaoOriginal)) {
                _tmpDescricaoOriginal = null;
              } else {
                _tmpDescricaoOriginal = _cursor.getString(_cursorIndexOfDescricaoOriginal);
              }
              final String _tmpDescricaoCustomizada;
              if (_cursor.isNull(_cursorIndexOfDescricaoCustomizada)) {
                _tmpDescricaoCustomizada = null;
              } else {
                _tmpDescricaoCustomizada = _cursor.getString(_cursorIndexOfDescricaoCustomizada);
              }
              final long _tmpDataHora;
              _tmpDataHora = _cursor.getLong(_cursorIndexOfDataHora);
              final Long _tmpCategoriaId;
              if (_cursor.isNull(_cursorIndexOfCategoriaId)) {
                _tmpCategoriaId = null;
              } else {
                _tmpCategoriaId = _cursor.getLong(_cursorIndexOfCategoriaId);
              }
              final String _tmpNota;
              if (_cursor.isNull(_cursorIndexOfNota)) {
                _tmpNota = null;
              } else {
                _tmpNota = _cursor.getString(_cursorIndexOfNota);
              }
              final Double _tmpLatitude;
              if (_cursor.isNull(_cursorIndexOfLatitude)) {
                _tmpLatitude = null;
              } else {
                _tmpLatitude = _cursor.getDouble(_cursorIndexOfLatitude);
              }
              final Double _tmpLongitude;
              if (_cursor.isNull(_cursorIndexOfLongitude)) {
                _tmpLongitude = null;
              } else {
                _tmpLongitude = _cursor.getDouble(_cursorIndexOfLongitude);
              }
              final String _tmpStatusAtribuicao;
              if (_cursor.isNull(_cursorIndexOfStatusAtribuicao)) {
                _tmpStatusAtribuicao = null;
              } else {
                _tmpStatusAtribuicao = _cursor.getString(_cursorIndexOfStatusAtribuicao);
              }
              final Integer _tmpNumeroParcela;
              if (_cursor.isNull(_cursorIndexOfNumeroParcela)) {
                _tmpNumeroParcela = null;
              } else {
                _tmpNumeroParcela = _cursor.getInt(_cursorIndexOfNumeroParcela);
              }
              final Integer _tmpTotalParcelas;
              if (_cursor.isNull(_cursorIndexOfTotalParcelas)) {
                _tmpTotalParcelas = null;
              } else {
                _tmpTotalParcelas = _cursor.getInt(_cursorIndexOfTotalParcelas);
              }
              _tmpTransacao = new TransacaoEntity(_tmpId,_tmpValorTotal,_tmpDescricaoOriginal,_tmpDescricaoCustomizada,_tmpDataHora,_tmpCategoriaId,_tmpNota,_tmpLatitude,_tmpLongitude,_tmpStatusAtribuicao,_tmpNumeroParcela,_tmpTotalParcelas);
              final CategoriaEntity _tmpCategoria;
              final Long _tmpKey_2;
              if (_cursor.isNull(_cursorIndexOfCategoriaId)) {
                _tmpKey_2 = null;
              } else {
                _tmpKey_2 = _cursor.getLong(_cursorIndexOfCategoriaId);
              }
              if (_tmpKey_2 != null) {
                _tmpCategoria = _collectionCategoria.get(_tmpKey_2);
              } else {
                _tmpCategoria = null;
              }
              final ArrayList<AtribuicaoComPessoa> _tmpAtribuicoesCollection;
              final String _tmpKey_3;
              if (_cursor.isNull(_cursorIndexOfId)) {
                _tmpKey_3 = null;
              } else {
                _tmpKey_3 = _cursor.getString(_cursorIndexOfId);
              }
              if (_tmpKey_3 != null) {
                _tmpAtribuicoesCollection = _collectionAtribuicoes.get(_tmpKey_3);
              } else {
                _tmpAtribuicoesCollection = new ArrayList<AtribuicaoComPessoa>();
              }
              _item = new TransacaoCompleta(_tmpTransacao,_tmpCategoria,_tmpAtribuicoesCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipcategoriasAscomNarrapayDataLocalEntityCategoriaEntity(
      @NonNull final LongSparseArray<CategoriaEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshipcategoriasAscomNarrapayDataLocalEntityCategoriaEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`nome`,`icone`,`corHex`,`categoriaPaiId` FROM `categorias` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfNome = 1;
      final int _cursorIndexOfIcone = 2;
      final int _cursorIndexOfCorHex = 3;
      final int _cursorIndexOfCategoriaPaiId = 4;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final CategoriaEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpNome;
          if (_cursor.isNull(_cursorIndexOfNome)) {
            _tmpNome = null;
          } else {
            _tmpNome = _cursor.getString(_cursorIndexOfNome);
          }
          final String _tmpIcone;
          if (_cursor.isNull(_cursorIndexOfIcone)) {
            _tmpIcone = null;
          } else {
            _tmpIcone = _cursor.getString(_cursorIndexOfIcone);
          }
          final String _tmpCorHex;
          if (_cursor.isNull(_cursorIndexOfCorHex)) {
            _tmpCorHex = null;
          } else {
            _tmpCorHex = _cursor.getString(_cursorIndexOfCorHex);
          }
          final Long _tmpCategoriaPaiId;
          if (_cursor.isNull(_cursorIndexOfCategoriaPaiId)) {
            _tmpCategoriaPaiId = null;
          } else {
            _tmpCategoriaPaiId = _cursor.getLong(_cursorIndexOfCategoriaPaiId);
          }
          _item_1 = new CategoriaEntity(_tmpId,_tmpNome,_tmpIcone,_tmpCorHex,_tmpCategoriaPaiId);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshippessoasAscomNarrapayDataLocalEntityPessoaEntity(
      @NonNull final LongSparseArray<PessoaEntity> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, false, (map) -> {
        __fetchRelationshippessoasAscomNarrapayDataLocalEntityPessoaEntity(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`nome`,`corHex` FROM `pessoas` WHERE `id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfNome = 1;
      final int _cursorIndexOfCorHex = 2;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        if (_map.containsKey(_tmpKey)) {
          final PessoaEntity _item_1;
          final long _tmpId;
          _tmpId = _cursor.getLong(_cursorIndexOfId);
          final String _tmpNome;
          if (_cursor.isNull(_cursorIndexOfNome)) {
            _tmpNome = null;
          } else {
            _tmpNome = _cursor.getString(_cursorIndexOfNome);
          }
          final String _tmpCorHex;
          if (_cursor.isNull(_cursorIndexOfCorHex)) {
            _tmpCorHex = null;
          } else {
            _tmpCorHex = _cursor.getString(_cursorIndexOfCorHex);
          }
          _item_1 = new PessoaEntity(_tmpId,_tmpNome,_tmpCorHex);
          _map.put(_tmpKey, _item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }

  private void __fetchRelationshipatribuicoesTransacaoAscomNarrapayDataLocalRelationAtribuicaoComPessoa(
      @NonNull final ArrayMap<String, ArrayList<AtribuicaoComPessoa>> _map) {
    final Set<String> __mapKeySet = _map.keySet();
    if (__mapKeySet.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchArrayMap(_map, true, (map) -> {
        __fetchRelationshipatribuicoesTransacaoAscomNarrapayDataLocalRelationAtribuicaoComPessoa(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `id`,`transacaoId`,`pessoaId`,`valorAtribuido` FROM `atribuicoes_transacao` WHERE `transacaoId` IN (");
    final int _inputSize = __mapKeySet == null ? 1 : __mapKeySet.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    if (__mapKeySet == null) {
      _stmt.bindNull(_argIndex);
    } else {
      for (String _item : __mapKeySet) {
        if (_item == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _item);
        }
        _argIndex++;
      }
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, true, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "transacaoId");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfId = 0;
      final int _cursorIndexOfTransacaoId = 1;
      final int _cursorIndexOfPessoaId = 2;
      final int _cursorIndexOfValorAtribuido = 3;
      final LongSparseArray<PessoaEntity> _collectionPessoa = new LongSparseArray<PessoaEntity>();
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_cursorIndexOfPessoaId);
        _collectionPessoa.put(_tmpKey, null);
      }
      _cursor.moveToPosition(-1);
      __fetchRelationshippessoasAscomNarrapayDataLocalEntityPessoaEntity(_collectionPessoa);
      while (_cursor.moveToNext()) {
        final String _tmpKey_1;
        if (_cursor.isNull(_itemKeyIndex)) {
          _tmpKey_1 = null;
        } else {
          _tmpKey_1 = _cursor.getString(_itemKeyIndex);
        }
        if (_tmpKey_1 != null) {
          final ArrayList<AtribuicaoComPessoa> _tmpRelation = _map.get(_tmpKey_1);
          if (_tmpRelation != null) {
            final AtribuicaoComPessoa _item_1;
            final AtribuicaoTransacaoEntity _tmpAtribuicao;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTransacaoId;
            if (_cursor.isNull(_cursorIndexOfTransacaoId)) {
              _tmpTransacaoId = null;
            } else {
              _tmpTransacaoId = _cursor.getString(_cursorIndexOfTransacaoId);
            }
            final long _tmpPessoaId;
            _tmpPessoaId = _cursor.getLong(_cursorIndexOfPessoaId);
            final double _tmpValorAtribuido;
            _tmpValorAtribuido = _cursor.getDouble(_cursorIndexOfValorAtribuido);
            _tmpAtribuicao = new AtribuicaoTransacaoEntity(_tmpId,_tmpTransacaoId,_tmpPessoaId,_tmpValorAtribuido);
            final PessoaEntity _tmpPessoa;
            final long _tmpKey_2;
            _tmpKey_2 = _cursor.getLong(_cursorIndexOfPessoaId);
            _tmpPessoa = _collectionPessoa.get(_tmpKey_2);
            _item_1 = new AtribuicaoComPessoa(_tmpAtribuicao,_tmpPessoa);
            _tmpRelation.add(_item_1);
          }
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
