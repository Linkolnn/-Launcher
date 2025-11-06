package com.customlauncher.app.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.customlauncher.app.data.model.HomeItemModel;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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
public final class HomeItemDao_Impl implements HomeItemDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<HomeItem> __insertionAdapterOfHomeItem;

  private final ItemTypeConverter __itemTypeConverter = new ItemTypeConverter();

  private final EntityDeletionOrUpdateAdapter<HomeItem> __deletionAdapterOfHomeItem;

  private final EntityDeletionOrUpdateAdapter<HomeItem> __updateAdapterOfHomeItem;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByPackage;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public HomeItemDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHomeItem = new EntityInsertionAdapter<HomeItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `home_items` (`id`,`type`,`packageName`,`componentName`,`label`,`widgetId`,`cellX`,`cellY`,`spanX`,`spanY`,`screen`,`position`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HomeItem entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __itemTypeConverter.fromItemType(entity.getType());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        if (entity.getPackageName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPackageName());
        }
        if (entity.getComponentName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getComponentName());
        }
        if (entity.getLabel() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLabel());
        }
        if (entity.getWidgetId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getWidgetId());
        }
        statement.bindLong(7, entity.getCellX());
        statement.bindLong(8, entity.getCellY());
        statement.bindLong(9, entity.getSpanX());
        statement.bindLong(10, entity.getSpanY());
        statement.bindLong(11, entity.getScreen());
        statement.bindLong(12, entity.getPosition());
      }
    };
    this.__deletionAdapterOfHomeItem = new EntityDeletionOrUpdateAdapter<HomeItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `home_items` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HomeItem entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfHomeItem = new EntityDeletionOrUpdateAdapter<HomeItem>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `home_items` SET `id` = ?,`type` = ?,`packageName` = ?,`componentName` = ?,`label` = ?,`widgetId` = ?,`cellX` = ?,`cellY` = ?,`spanX` = ?,`spanY` = ?,`screen` = ?,`position` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HomeItem entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __itemTypeConverter.fromItemType(entity.getType());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        if (entity.getPackageName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPackageName());
        }
        if (entity.getComponentName() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getComponentName());
        }
        if (entity.getLabel() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLabel());
        }
        if (entity.getWidgetId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getWidgetId());
        }
        statement.bindLong(7, entity.getCellX());
        statement.bindLong(8, entity.getCellY());
        statement.bindLong(9, entity.getSpanX());
        statement.bindLong(10, entity.getSpanY());
        statement.bindLong(11, entity.getScreen());
        statement.bindLong(12, entity.getPosition());
        statement.bindLong(13, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM home_items WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByPackage = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM home_items WHERE packageName = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM home_items";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final HomeItem item, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHomeItem.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<HomeItem> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHomeItem.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final HomeItem item, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHomeItem.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final HomeItem item, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfHomeItem.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateAll(final List<HomeItem> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfHomeItem.handleMultiple(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object moveItem(final long itemId, final int newX, final int newY,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> HomeItemDao.DefaultImpls.moveItem(HomeItemDao_Impl.this, itemId, newX, newY, __cont), $completion);
  }

  @Override
  public Object swapItems(final long itemId1, final long itemId2,
      final Continuation<? super Unit> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> HomeItemDao.DefaultImpls.swapItems(HomeItemDao_Impl.this, itemId1, itemId2, __cont), $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
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
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteByPackage(final String packageName,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByPackage.acquire();
        int _argIndex = 1;
        if (packageName == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, packageName);
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
          __preparedStmtOfDeleteByPackage.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<HomeItem>> getAllItems() {
    final String _sql = "SELECT * FROM home_items ORDER BY screen, cellY, cellX";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"home_items"}, new Callable<List<HomeItem>>() {
      @Override
      @NonNull
      public List<HomeItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfComponentName = CursorUtil.getColumnIndexOrThrow(_cursor, "componentName");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfWidgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "widgetId");
          final int _cursorIndexOfCellX = CursorUtil.getColumnIndexOrThrow(_cursor, "cellX");
          final int _cursorIndexOfCellY = CursorUtil.getColumnIndexOrThrow(_cursor, "cellY");
          final int _cursorIndexOfSpanX = CursorUtil.getColumnIndexOrThrow(_cursor, "spanX");
          final int _cursorIndexOfSpanY = CursorUtil.getColumnIndexOrThrow(_cursor, "spanY");
          final int _cursorIndexOfScreen = CursorUtil.getColumnIndexOrThrow(_cursor, "screen");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final List<HomeItem> _result = new ArrayList<HomeItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HomeItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final HomeItemModel.ItemType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __itemTypeConverter.toItemType(_tmp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final String _tmpComponentName;
            if (_cursor.isNull(_cursorIndexOfComponentName)) {
              _tmpComponentName = null;
            } else {
              _tmpComponentName = _cursor.getString(_cursorIndexOfComponentName);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final Integer _tmpWidgetId;
            if (_cursor.isNull(_cursorIndexOfWidgetId)) {
              _tmpWidgetId = null;
            } else {
              _tmpWidgetId = _cursor.getInt(_cursorIndexOfWidgetId);
            }
            final int _tmpCellX;
            _tmpCellX = _cursor.getInt(_cursorIndexOfCellX);
            final int _tmpCellY;
            _tmpCellY = _cursor.getInt(_cursorIndexOfCellY);
            final int _tmpSpanX;
            _tmpSpanX = _cursor.getInt(_cursorIndexOfSpanX);
            final int _tmpSpanY;
            _tmpSpanY = _cursor.getInt(_cursorIndexOfSpanY);
            final int _tmpScreen;
            _tmpScreen = _cursor.getInt(_cursorIndexOfScreen);
            final int _tmpPosition;
            _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
            _item = new HomeItem(_tmpId,_tmpType,_tmpPackageName,_tmpComponentName,_tmpLabel,_tmpWidgetId,_tmpCellX,_tmpCellY,_tmpSpanX,_tmpSpanY,_tmpScreen,_tmpPosition);
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
  public Object getAllItemsOnce(final Continuation<? super List<HomeItem>> $completion) {
    final String _sql = "SELECT * FROM home_items ORDER BY screen, cellY, cellX";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HomeItem>>() {
      @Override
      @NonNull
      public List<HomeItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfComponentName = CursorUtil.getColumnIndexOrThrow(_cursor, "componentName");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfWidgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "widgetId");
          final int _cursorIndexOfCellX = CursorUtil.getColumnIndexOrThrow(_cursor, "cellX");
          final int _cursorIndexOfCellY = CursorUtil.getColumnIndexOrThrow(_cursor, "cellY");
          final int _cursorIndexOfSpanX = CursorUtil.getColumnIndexOrThrow(_cursor, "spanX");
          final int _cursorIndexOfSpanY = CursorUtil.getColumnIndexOrThrow(_cursor, "spanY");
          final int _cursorIndexOfScreen = CursorUtil.getColumnIndexOrThrow(_cursor, "screen");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final List<HomeItem> _result = new ArrayList<HomeItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HomeItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final HomeItemModel.ItemType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __itemTypeConverter.toItemType(_tmp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final String _tmpComponentName;
            if (_cursor.isNull(_cursorIndexOfComponentName)) {
              _tmpComponentName = null;
            } else {
              _tmpComponentName = _cursor.getString(_cursorIndexOfComponentName);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final Integer _tmpWidgetId;
            if (_cursor.isNull(_cursorIndexOfWidgetId)) {
              _tmpWidgetId = null;
            } else {
              _tmpWidgetId = _cursor.getInt(_cursorIndexOfWidgetId);
            }
            final int _tmpCellX;
            _tmpCellX = _cursor.getInt(_cursorIndexOfCellX);
            final int _tmpCellY;
            _tmpCellY = _cursor.getInt(_cursorIndexOfCellY);
            final int _tmpSpanX;
            _tmpSpanX = _cursor.getInt(_cursorIndexOfSpanX);
            final int _tmpSpanY;
            _tmpSpanY = _cursor.getInt(_cursorIndexOfSpanY);
            final int _tmpScreen;
            _tmpScreen = _cursor.getInt(_cursorIndexOfScreen);
            final int _tmpPosition;
            _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
            _item = new HomeItem(_tmpId,_tmpType,_tmpPackageName,_tmpComponentName,_tmpLabel,_tmpWidgetId,_tmpCellX,_tmpCellY,_tmpSpanX,_tmpSpanY,_tmpScreen,_tmpPosition);
            _result.add(_item);
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
  public Flow<List<HomeItem>> getItemsByScreen(final int screen) {
    final String _sql = "SELECT * FROM home_items WHERE screen = ? ORDER BY cellY, cellX";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, screen);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"home_items"}, new Callable<List<HomeItem>>() {
      @Override
      @NonNull
      public List<HomeItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfComponentName = CursorUtil.getColumnIndexOrThrow(_cursor, "componentName");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfWidgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "widgetId");
          final int _cursorIndexOfCellX = CursorUtil.getColumnIndexOrThrow(_cursor, "cellX");
          final int _cursorIndexOfCellY = CursorUtil.getColumnIndexOrThrow(_cursor, "cellY");
          final int _cursorIndexOfSpanX = CursorUtil.getColumnIndexOrThrow(_cursor, "spanX");
          final int _cursorIndexOfSpanY = CursorUtil.getColumnIndexOrThrow(_cursor, "spanY");
          final int _cursorIndexOfScreen = CursorUtil.getColumnIndexOrThrow(_cursor, "screen");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final List<HomeItem> _result = new ArrayList<HomeItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HomeItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final HomeItemModel.ItemType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __itemTypeConverter.toItemType(_tmp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final String _tmpComponentName;
            if (_cursor.isNull(_cursorIndexOfComponentName)) {
              _tmpComponentName = null;
            } else {
              _tmpComponentName = _cursor.getString(_cursorIndexOfComponentName);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final Integer _tmpWidgetId;
            if (_cursor.isNull(_cursorIndexOfWidgetId)) {
              _tmpWidgetId = null;
            } else {
              _tmpWidgetId = _cursor.getInt(_cursorIndexOfWidgetId);
            }
            final int _tmpCellX;
            _tmpCellX = _cursor.getInt(_cursorIndexOfCellX);
            final int _tmpCellY;
            _tmpCellY = _cursor.getInt(_cursorIndexOfCellY);
            final int _tmpSpanX;
            _tmpSpanX = _cursor.getInt(_cursorIndexOfSpanX);
            final int _tmpSpanY;
            _tmpSpanY = _cursor.getInt(_cursorIndexOfSpanY);
            final int _tmpScreen;
            _tmpScreen = _cursor.getInt(_cursorIndexOfScreen);
            final int _tmpPosition;
            _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
            _item = new HomeItem(_tmpId,_tmpType,_tmpPackageName,_tmpComponentName,_tmpLabel,_tmpWidgetId,_tmpCellX,_tmpCellY,_tmpSpanX,_tmpSpanY,_tmpScreen,_tmpPosition);
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
  public Object getItemById(final long id, final Continuation<? super HomeItem> $completion) {
    final String _sql = "SELECT * FROM home_items WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<HomeItem>() {
      @Override
      @Nullable
      public HomeItem call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfComponentName = CursorUtil.getColumnIndexOrThrow(_cursor, "componentName");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfWidgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "widgetId");
          final int _cursorIndexOfCellX = CursorUtil.getColumnIndexOrThrow(_cursor, "cellX");
          final int _cursorIndexOfCellY = CursorUtil.getColumnIndexOrThrow(_cursor, "cellY");
          final int _cursorIndexOfSpanX = CursorUtil.getColumnIndexOrThrow(_cursor, "spanX");
          final int _cursorIndexOfSpanY = CursorUtil.getColumnIndexOrThrow(_cursor, "spanY");
          final int _cursorIndexOfScreen = CursorUtil.getColumnIndexOrThrow(_cursor, "screen");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final HomeItem _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final HomeItemModel.ItemType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __itemTypeConverter.toItemType(_tmp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final String _tmpComponentName;
            if (_cursor.isNull(_cursorIndexOfComponentName)) {
              _tmpComponentName = null;
            } else {
              _tmpComponentName = _cursor.getString(_cursorIndexOfComponentName);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final Integer _tmpWidgetId;
            if (_cursor.isNull(_cursorIndexOfWidgetId)) {
              _tmpWidgetId = null;
            } else {
              _tmpWidgetId = _cursor.getInt(_cursorIndexOfWidgetId);
            }
            final int _tmpCellX;
            _tmpCellX = _cursor.getInt(_cursorIndexOfCellX);
            final int _tmpCellY;
            _tmpCellY = _cursor.getInt(_cursorIndexOfCellY);
            final int _tmpSpanX;
            _tmpSpanX = _cursor.getInt(_cursorIndexOfSpanX);
            final int _tmpSpanY;
            _tmpSpanY = _cursor.getInt(_cursorIndexOfSpanY);
            final int _tmpScreen;
            _tmpScreen = _cursor.getInt(_cursorIndexOfScreen);
            final int _tmpPosition;
            _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
            _result = new HomeItem(_tmpId,_tmpType,_tmpPackageName,_tmpComponentName,_tmpLabel,_tmpWidgetId,_tmpCellX,_tmpCellY,_tmpSpanX,_tmpSpanY,_tmpScreen,_tmpPosition);
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
  public Object getItemsByPackage(final String packageName,
      final Continuation<? super List<HomeItem>> $completion) {
    final String _sql = "SELECT * FROM home_items WHERE packageName = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (packageName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, packageName);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HomeItem>>() {
      @Override
      @NonNull
      public List<HomeItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfComponentName = CursorUtil.getColumnIndexOrThrow(_cursor, "componentName");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfWidgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "widgetId");
          final int _cursorIndexOfCellX = CursorUtil.getColumnIndexOrThrow(_cursor, "cellX");
          final int _cursorIndexOfCellY = CursorUtil.getColumnIndexOrThrow(_cursor, "cellY");
          final int _cursorIndexOfSpanX = CursorUtil.getColumnIndexOrThrow(_cursor, "spanX");
          final int _cursorIndexOfSpanY = CursorUtil.getColumnIndexOrThrow(_cursor, "spanY");
          final int _cursorIndexOfScreen = CursorUtil.getColumnIndexOrThrow(_cursor, "screen");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final List<HomeItem> _result = new ArrayList<HomeItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HomeItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final HomeItemModel.ItemType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __itemTypeConverter.toItemType(_tmp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final String _tmpComponentName;
            if (_cursor.isNull(_cursorIndexOfComponentName)) {
              _tmpComponentName = null;
            } else {
              _tmpComponentName = _cursor.getString(_cursorIndexOfComponentName);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final Integer _tmpWidgetId;
            if (_cursor.isNull(_cursorIndexOfWidgetId)) {
              _tmpWidgetId = null;
            } else {
              _tmpWidgetId = _cursor.getInt(_cursorIndexOfWidgetId);
            }
            final int _tmpCellX;
            _tmpCellX = _cursor.getInt(_cursorIndexOfCellX);
            final int _tmpCellY;
            _tmpCellY = _cursor.getInt(_cursorIndexOfCellY);
            final int _tmpSpanX;
            _tmpSpanX = _cursor.getInt(_cursorIndexOfSpanX);
            final int _tmpSpanY;
            _tmpSpanY = _cursor.getInt(_cursorIndexOfSpanY);
            final int _tmpScreen;
            _tmpScreen = _cursor.getInt(_cursorIndexOfScreen);
            final int _tmpPosition;
            _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
            _item = new HomeItem(_tmpId,_tmpType,_tmpPackageName,_tmpComponentName,_tmpLabel,_tmpWidgetId,_tmpCellX,_tmpCellY,_tmpSpanX,_tmpSpanY,_tmpScreen,_tmpPosition);
            _result.add(_item);
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
  public Object getItemsAtPosition(final int x, final int y, final int screen,
      final Continuation<? super List<HomeItem>> $completion) {
    final String _sql = "SELECT * FROM home_items WHERE cellX = ? AND cellY = ? AND screen = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, x);
    _argIndex = 2;
    _statement.bindLong(_argIndex, y);
    _argIndex = 3;
    _statement.bindLong(_argIndex, screen);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<HomeItem>>() {
      @Override
      @NonNull
      public List<HomeItem> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfComponentName = CursorUtil.getColumnIndexOrThrow(_cursor, "componentName");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfWidgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "widgetId");
          final int _cursorIndexOfCellX = CursorUtil.getColumnIndexOrThrow(_cursor, "cellX");
          final int _cursorIndexOfCellY = CursorUtil.getColumnIndexOrThrow(_cursor, "cellY");
          final int _cursorIndexOfSpanX = CursorUtil.getColumnIndexOrThrow(_cursor, "spanX");
          final int _cursorIndexOfSpanY = CursorUtil.getColumnIndexOrThrow(_cursor, "spanY");
          final int _cursorIndexOfScreen = CursorUtil.getColumnIndexOrThrow(_cursor, "screen");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final List<HomeItem> _result = new ArrayList<HomeItem>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HomeItem _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final HomeItemModel.ItemType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __itemTypeConverter.toItemType(_tmp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final String _tmpComponentName;
            if (_cursor.isNull(_cursorIndexOfComponentName)) {
              _tmpComponentName = null;
            } else {
              _tmpComponentName = _cursor.getString(_cursorIndexOfComponentName);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final Integer _tmpWidgetId;
            if (_cursor.isNull(_cursorIndexOfWidgetId)) {
              _tmpWidgetId = null;
            } else {
              _tmpWidgetId = _cursor.getInt(_cursorIndexOfWidgetId);
            }
            final int _tmpCellX;
            _tmpCellX = _cursor.getInt(_cursorIndexOfCellX);
            final int _tmpCellY;
            _tmpCellY = _cursor.getInt(_cursorIndexOfCellY);
            final int _tmpSpanX;
            _tmpSpanX = _cursor.getInt(_cursorIndexOfSpanX);
            final int _tmpSpanY;
            _tmpSpanY = _cursor.getInt(_cursorIndexOfSpanY);
            final int _tmpScreen;
            _tmpScreen = _cursor.getInt(_cursorIndexOfScreen);
            final int _tmpPosition;
            _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
            _item = new HomeItem(_tmpId,_tmpType,_tmpPackageName,_tmpComponentName,_tmpLabel,_tmpWidgetId,_tmpCellX,_tmpCellY,_tmpSpanX,_tmpSpanY,_tmpScreen,_tmpPosition);
            _result.add(_item);
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
  public Object getItemByPackageAndPosition(final String packageName, final int x, final int y,
      final Continuation<? super HomeItem> $completion) {
    final String _sql = "SELECT * FROM home_items WHERE packageName = ? AND cellX = ? AND cellY = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 3);
    int _argIndex = 1;
    if (packageName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, packageName);
    }
    _argIndex = 2;
    _statement.bindLong(_argIndex, x);
    _argIndex = 3;
    _statement.bindLong(_argIndex, y);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<HomeItem>() {
      @Override
      @Nullable
      public HomeItem call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfComponentName = CursorUtil.getColumnIndexOrThrow(_cursor, "componentName");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfWidgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "widgetId");
          final int _cursorIndexOfCellX = CursorUtil.getColumnIndexOrThrow(_cursor, "cellX");
          final int _cursorIndexOfCellY = CursorUtil.getColumnIndexOrThrow(_cursor, "cellY");
          final int _cursorIndexOfSpanX = CursorUtil.getColumnIndexOrThrow(_cursor, "spanX");
          final int _cursorIndexOfSpanY = CursorUtil.getColumnIndexOrThrow(_cursor, "spanY");
          final int _cursorIndexOfScreen = CursorUtil.getColumnIndexOrThrow(_cursor, "screen");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final HomeItem _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final HomeItemModel.ItemType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __itemTypeConverter.toItemType(_tmp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final String _tmpComponentName;
            if (_cursor.isNull(_cursorIndexOfComponentName)) {
              _tmpComponentName = null;
            } else {
              _tmpComponentName = _cursor.getString(_cursorIndexOfComponentName);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final Integer _tmpWidgetId;
            if (_cursor.isNull(_cursorIndexOfWidgetId)) {
              _tmpWidgetId = null;
            } else {
              _tmpWidgetId = _cursor.getInt(_cursorIndexOfWidgetId);
            }
            final int _tmpCellX;
            _tmpCellX = _cursor.getInt(_cursorIndexOfCellX);
            final int _tmpCellY;
            _tmpCellY = _cursor.getInt(_cursorIndexOfCellY);
            final int _tmpSpanX;
            _tmpSpanX = _cursor.getInt(_cursorIndexOfSpanX);
            final int _tmpSpanY;
            _tmpSpanY = _cursor.getInt(_cursorIndexOfSpanY);
            final int _tmpScreen;
            _tmpScreen = _cursor.getInt(_cursorIndexOfScreen);
            final int _tmpPosition;
            _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
            _result = new HomeItem(_tmpId,_tmpType,_tmpPackageName,_tmpComponentName,_tmpLabel,_tmpWidgetId,_tmpCellX,_tmpCellY,_tmpSpanX,_tmpSpanY,_tmpScreen,_tmpPosition);
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
  public Object getItemCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM home_items";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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
  public Object getItemCountByScreen(final int screen,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM home_items WHERE screen = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, screen);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
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
  public Object isPositionOccupied(final int x, final int y, final int screen,
      final Continuation<? super Boolean> $completion) {
    final String _sql = "\n"
            + "        SELECT EXISTS(\n"
            + "            SELECT 1 FROM home_items \n"
            + "            WHERE screen = ? \n"
            + "            AND ? >= cellX AND ? < cellX + spanX\n"
            + "            AND ? >= cellY AND ? < cellY + spanY\n"
            + "        )\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 5);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, screen);
    _argIndex = 2;
    _statement.bindLong(_argIndex, x);
    _argIndex = 3;
    _statement.bindLong(_argIndex, x);
    _argIndex = 4;
    _statement.bindLong(_argIndex, y);
    _argIndex = 5;
    _statement.bindLong(_argIndex, y);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Boolean>() {
      @Override
      @NonNull
      public Boolean call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Boolean _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp == null ? null : _tmp != 0;
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
  public Object findFirstEmptyPosition(final int screen, final int columns,
      final Continuation<? super HomeItem> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM home_items \n"
            + "        WHERE screen = ? \n"
            + "        ORDER BY cellY * ? + cellX \n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, screen);
    _argIndex = 2;
    _statement.bindLong(_argIndex, columns);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<HomeItem>() {
      @Override
      @Nullable
      public HomeItem call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfPackageName = CursorUtil.getColumnIndexOrThrow(_cursor, "packageName");
          final int _cursorIndexOfComponentName = CursorUtil.getColumnIndexOrThrow(_cursor, "componentName");
          final int _cursorIndexOfLabel = CursorUtil.getColumnIndexOrThrow(_cursor, "label");
          final int _cursorIndexOfWidgetId = CursorUtil.getColumnIndexOrThrow(_cursor, "widgetId");
          final int _cursorIndexOfCellX = CursorUtil.getColumnIndexOrThrow(_cursor, "cellX");
          final int _cursorIndexOfCellY = CursorUtil.getColumnIndexOrThrow(_cursor, "cellY");
          final int _cursorIndexOfSpanX = CursorUtil.getColumnIndexOrThrow(_cursor, "spanX");
          final int _cursorIndexOfSpanY = CursorUtil.getColumnIndexOrThrow(_cursor, "spanY");
          final int _cursorIndexOfScreen = CursorUtil.getColumnIndexOrThrow(_cursor, "screen");
          final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
          final HomeItem _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final HomeItemModel.ItemType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __itemTypeConverter.toItemType(_tmp);
            final String _tmpPackageName;
            if (_cursor.isNull(_cursorIndexOfPackageName)) {
              _tmpPackageName = null;
            } else {
              _tmpPackageName = _cursor.getString(_cursorIndexOfPackageName);
            }
            final String _tmpComponentName;
            if (_cursor.isNull(_cursorIndexOfComponentName)) {
              _tmpComponentName = null;
            } else {
              _tmpComponentName = _cursor.getString(_cursorIndexOfComponentName);
            }
            final String _tmpLabel;
            if (_cursor.isNull(_cursorIndexOfLabel)) {
              _tmpLabel = null;
            } else {
              _tmpLabel = _cursor.getString(_cursorIndexOfLabel);
            }
            final Integer _tmpWidgetId;
            if (_cursor.isNull(_cursorIndexOfWidgetId)) {
              _tmpWidgetId = null;
            } else {
              _tmpWidgetId = _cursor.getInt(_cursorIndexOfWidgetId);
            }
            final int _tmpCellX;
            _tmpCellX = _cursor.getInt(_cursorIndexOfCellX);
            final int _tmpCellY;
            _tmpCellY = _cursor.getInt(_cursorIndexOfCellY);
            final int _tmpSpanX;
            _tmpSpanX = _cursor.getInt(_cursorIndexOfSpanX);
            final int _tmpSpanY;
            _tmpSpanY = _cursor.getInt(_cursorIndexOfSpanY);
            final int _tmpScreen;
            _tmpScreen = _cursor.getInt(_cursorIndexOfScreen);
            final int _tmpPosition;
            _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
            _result = new HomeItem(_tmpId,_tmpType,_tmpPackageName,_tmpComponentName,_tmpLabel,_tmpWidgetId,_tmpCellX,_tmpCellY,_tmpSpanX,_tmpSpanY,_tmpScreen,_tmpPosition);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
