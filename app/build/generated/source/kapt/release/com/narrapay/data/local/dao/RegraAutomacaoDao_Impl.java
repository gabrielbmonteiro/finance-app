package com.narrapay.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.narrapay.data.local.entity.RegraAutomacaoEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class RegraAutomacaoDao_Impl implements RegraAutomacaoDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RegraAutomacaoEntity> __insertionAdapterOfRegraAutomacaoEntity;

  private final EntityDeletionOrUpdateAdapter<RegraAutomacaoEntity> __deletionAdapterOfRegraAutomacaoEntity;

  private final EntityDeletionOrUpdateAdapter<RegraAutomacaoEntity> __updateAdapterOfRegraAutomacaoEntity;

  public RegraAutomacaoDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRegraAutomacaoEntity = new EntityInsertionAdapter<RegraAutomacaoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `regras_automacao` (`id`,`termoBusca`,`categoriaIdPadrao`,`pessoaIdPadrao`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RegraAutomacaoEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTermoBusca() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTermoBusca());
        }
        if (entity.getCategoriaIdPadrao() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getCategoriaIdPadrao());
        }
        if (entity.getPessoaIdPadrao() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getPessoaIdPadrao());
        }
      }
    };
    this.__deletionAdapterOfRegraAutomacaoEntity = new EntityDeletionOrUpdateAdapter<RegraAutomacaoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `regras_automacao` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RegraAutomacaoEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfRegraAutomacaoEntity = new EntityDeletionOrUpdateAdapter<RegraAutomacaoEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `regras_automacao` SET `id` = ?,`termoBusca` = ?,`categoriaIdPadrao` = ?,`pessoaIdPadrao` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RegraAutomacaoEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getTermoBusca() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getTermoBusca());
        }
        if (entity.getCategoriaIdPadrao() == null) {
          statement.bindNull(3);
        } else {
          statement.bindLong(3, entity.getCategoriaIdPadrao());
        }
        if (entity.getPessoaIdPadrao() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getPessoaIdPadrao());
        }
        statement.bindLong(5, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final RegraAutomacaoEntity regra,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfRegraAutomacaoEntity.insertAndReturnId(regra);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final RegraAutomacaoEntity regra,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRegraAutomacaoEntity.handle(regra);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final RegraAutomacaoEntity regra,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRegraAutomacaoEntity.handle(regra);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<RegraAutomacaoEntity>> getAll() {
    final String _sql = "SELECT * FROM regras_automacao";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"regras_automacao"}, new Callable<List<RegraAutomacaoEntity>>() {
      @Override
      @NonNull
      public List<RegraAutomacaoEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTermoBusca = CursorUtil.getColumnIndexOrThrow(_cursor, "termoBusca");
          final int _cursorIndexOfCategoriaIdPadrao = CursorUtil.getColumnIndexOrThrow(_cursor, "categoriaIdPadrao");
          final int _cursorIndexOfPessoaIdPadrao = CursorUtil.getColumnIndexOrThrow(_cursor, "pessoaIdPadrao");
          final List<RegraAutomacaoEntity> _result = new ArrayList<RegraAutomacaoEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final RegraAutomacaoEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpTermoBusca;
            if (_cursor.isNull(_cursorIndexOfTermoBusca)) {
              _tmpTermoBusca = null;
            } else {
              _tmpTermoBusca = _cursor.getString(_cursorIndexOfTermoBusca);
            }
            final Long _tmpCategoriaIdPadrao;
            if (_cursor.isNull(_cursorIndexOfCategoriaIdPadrao)) {
              _tmpCategoriaIdPadrao = null;
            } else {
              _tmpCategoriaIdPadrao = _cursor.getLong(_cursorIndexOfCategoriaIdPadrao);
            }
            final Long _tmpPessoaIdPadrao;
            if (_cursor.isNull(_cursorIndexOfPessoaIdPadrao)) {
              _tmpPessoaIdPadrao = null;
            } else {
              _tmpPessoaIdPadrao = _cursor.getLong(_cursorIndexOfPessoaIdPadrao);
            }
            _item = new RegraAutomacaoEntity(_tmpId,_tmpTermoBusca,_tmpCategoriaIdPadrao,_tmpPessoaIdPadrao);
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
  public List<RegraAutomacaoEntity> getAllRegrasSync() {
    final String _sql = "SELECT * FROM regras_automacao";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTermoBusca = CursorUtil.getColumnIndexOrThrow(_cursor, "termoBusca");
      final int _cursorIndexOfCategoriaIdPadrao = CursorUtil.getColumnIndexOrThrow(_cursor, "categoriaIdPadrao");
      final int _cursorIndexOfPessoaIdPadrao = CursorUtil.getColumnIndexOrThrow(_cursor, "pessoaIdPadrao");
      final List<RegraAutomacaoEntity> _result = new ArrayList<RegraAutomacaoEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final RegraAutomacaoEntity _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final String _tmpTermoBusca;
        if (_cursor.isNull(_cursorIndexOfTermoBusca)) {
          _tmpTermoBusca = null;
        } else {
          _tmpTermoBusca = _cursor.getString(_cursorIndexOfTermoBusca);
        }
        final Long _tmpCategoriaIdPadrao;
        if (_cursor.isNull(_cursorIndexOfCategoriaIdPadrao)) {
          _tmpCategoriaIdPadrao = null;
        } else {
          _tmpCategoriaIdPadrao = _cursor.getLong(_cursorIndexOfCategoriaIdPadrao);
        }
        final Long _tmpPessoaIdPadrao;
        if (_cursor.isNull(_cursorIndexOfPessoaIdPadrao)) {
          _tmpPessoaIdPadrao = null;
        } else {
          _tmpPessoaIdPadrao = _cursor.getLong(_cursorIndexOfPessoaIdPadrao);
        }
        _item = new RegraAutomacaoEntity(_tmpId,_tmpTermoBusca,_tmpCategoriaIdPadrao,_tmpPessoaIdPadrao);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
