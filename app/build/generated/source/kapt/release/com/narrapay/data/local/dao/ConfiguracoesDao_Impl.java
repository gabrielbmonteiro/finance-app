package com.narrapay.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.narrapay.data.local.entity.ConfiguracoesEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ConfiguracoesDao_Impl implements ConfiguracoesDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ConfiguracoesEntity> __insertionAdapterOfConfiguracoesEntity;

  public ConfiguracoesDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfConfiguracoesEntity = new EntityInsertionAdapter<ConfiguracoesEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `configuracoes` (`id`,`diaFechamento`,`diaVencimento`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ConfiguracoesEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getDiaFechamento());
        statement.bindLong(3, entity.getDiaVencimento());
      }
    };
  }

  @Override
  public Object insertOrUpdate(final ConfiguracoesEntity configuracoes,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfConfiguracoesEntity.insert(configuracoes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ConfiguracoesEntity> getConfiguracoes() {
    final String _sql = "SELECT * FROM configuracoes WHERE id = 1 LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"configuracoes"}, new Callable<ConfiguracoesEntity>() {
      @Override
      @Nullable
      public ConfiguracoesEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDiaFechamento = CursorUtil.getColumnIndexOrThrow(_cursor, "diaFechamento");
          final int _cursorIndexOfDiaVencimento = CursorUtil.getColumnIndexOrThrow(_cursor, "diaVencimento");
          final ConfiguracoesEntity _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final int _tmpDiaFechamento;
            _tmpDiaFechamento = _cursor.getInt(_cursorIndexOfDiaFechamento);
            final int _tmpDiaVencimento;
            _tmpDiaVencimento = _cursor.getInt(_cursorIndexOfDiaVencimento);
            _result = new ConfiguracoesEntity(_tmpId,_tmpDiaFechamento,_tmpDiaVencimento);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
