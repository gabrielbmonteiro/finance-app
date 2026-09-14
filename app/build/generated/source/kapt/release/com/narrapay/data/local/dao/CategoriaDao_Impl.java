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
import com.narrapay.data.local.entity.CategoriaEntity;
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
public final class CategoriaDao_Impl implements CategoriaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CategoriaEntity> __insertionAdapterOfCategoriaEntity;

  private final EntityDeletionOrUpdateAdapter<CategoriaEntity> __deletionAdapterOfCategoriaEntity;

  private final EntityDeletionOrUpdateAdapter<CategoriaEntity> __updateAdapterOfCategoriaEntity;

  public CategoriaDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCategoriaEntity = new EntityInsertionAdapter<CategoriaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `categorias` (`id`,`nome`,`icone`,`corHex`,`categoriaPaiId`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CategoriaEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNome() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNome());
        }
        if (entity.getIcone() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getIcone());
        }
        if (entity.getCorHex() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCorHex());
        }
        if (entity.getCategoriaPaiId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getCategoriaPaiId());
        }
      }
    };
    this.__deletionAdapterOfCategoriaEntity = new EntityDeletionOrUpdateAdapter<CategoriaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `categorias` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CategoriaEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfCategoriaEntity = new EntityDeletionOrUpdateAdapter<CategoriaEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `categorias` SET `id` = ?,`nome` = ?,`icone` = ?,`corHex` = ?,`categoriaPaiId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CategoriaEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getNome() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getNome());
        }
        if (entity.getIcone() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getIcone());
        }
        if (entity.getCorHex() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getCorHex());
        }
        if (entity.getCategoriaPaiId() == null) {
          statement.bindNull(5);
        } else {
          statement.bindLong(5, entity.getCategoriaPaiId());
        }
        statement.bindLong(6, entity.getId());
      }
    };
  }

  @Override
  public Object insert(final CategoriaEntity categoria,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfCategoriaEntity.insertAndReturnId(categoria);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final CategoriaEntity categoria,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCategoriaEntity.handle(categoria);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final CategoriaEntity categoria,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCategoriaEntity.handle(categoria);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CategoriaEntity>> getAll() {
    final String _sql = "SELECT * FROM categorias";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"categorias"}, new Callable<List<CategoriaEntity>>() {
      @Override
      @NonNull
      public List<CategoriaEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfNome = CursorUtil.getColumnIndexOrThrow(_cursor, "nome");
          final int _cursorIndexOfIcone = CursorUtil.getColumnIndexOrThrow(_cursor, "icone");
          final int _cursorIndexOfCorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "corHex");
          final int _cursorIndexOfCategoriaPaiId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoriaPaiId");
          final List<CategoriaEntity> _result = new ArrayList<CategoriaEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategoriaEntity _item;
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
            _item = new CategoriaEntity(_tmpId,_tmpNome,_tmpIcone,_tmpCorHex,_tmpCategoriaPaiId);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
