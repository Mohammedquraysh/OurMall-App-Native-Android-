package eu.ourmall.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import eu.ourmall.app.data.local.entity.OrderEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class OrderDao_Impl implements OrderDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<OrderEntity> __insertionAdapterOfOrderEntity;

  public OrderDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfOrderEntity = new EntityInsertionAdapter<OrderEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `orders` (`id`,`createdAtEpoch`,`statusJson`,`vendorOrdersJson`,`cartLevelDiscountAmount`,`promoCode`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final OrderEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindLong(2, entity.getCreatedAtEpoch());
        statement.bindString(3, entity.getStatusJson());
        statement.bindString(4, entity.getVendorOrdersJson());
        statement.bindDouble(5, entity.getCartLevelDiscountAmount());
        if (entity.getPromoCode() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getPromoCode());
        }
      }
    };
  }

  @Override
  public Object upsert(final OrderEntity order, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfOrderEntity.insert(order);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<OrderEntity>> observeAll() {
    final String _sql = "SELECT * FROM orders ORDER BY createdAtEpoch DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"orders"}, new Callable<List<OrderEntity>>() {
      @Override
      @NonNull
      public List<OrderEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreatedAtEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpoch");
          final int _cursorIndexOfStatusJson = CursorUtil.getColumnIndexOrThrow(_cursor, "statusJson");
          final int _cursorIndexOfVendorOrdersJson = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorOrdersJson");
          final int _cursorIndexOfCartLevelDiscountAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "cartLevelDiscountAmount");
          final int _cursorIndexOfPromoCode = CursorUtil.getColumnIndexOrThrow(_cursor, "promoCode");
          final List<OrderEntity> _result = new ArrayList<OrderEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final OrderEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final long _tmpCreatedAtEpoch;
            _tmpCreatedAtEpoch = _cursor.getLong(_cursorIndexOfCreatedAtEpoch);
            final String _tmpStatusJson;
            _tmpStatusJson = _cursor.getString(_cursorIndexOfStatusJson);
            final String _tmpVendorOrdersJson;
            _tmpVendorOrdersJson = _cursor.getString(_cursorIndexOfVendorOrdersJson);
            final double _tmpCartLevelDiscountAmount;
            _tmpCartLevelDiscountAmount = _cursor.getDouble(_cursorIndexOfCartLevelDiscountAmount);
            final String _tmpPromoCode;
            if (_cursor.isNull(_cursorIndexOfPromoCode)) {
              _tmpPromoCode = null;
            } else {
              _tmpPromoCode = _cursor.getString(_cursorIndexOfPromoCode);
            }
            _item = new OrderEntity(_tmpId,_tmpCreatedAtEpoch,_tmpStatusJson,_tmpVendorOrdersJson,_tmpCartLevelDiscountAmount,_tmpPromoCode);
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
  public Flow<OrderEntity> observeById(final String id) {
    final String _sql = "SELECT * FROM orders WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"orders"}, new Callable<OrderEntity>() {
      @Override
      @Nullable
      public OrderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreatedAtEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpoch");
          final int _cursorIndexOfStatusJson = CursorUtil.getColumnIndexOrThrow(_cursor, "statusJson");
          final int _cursorIndexOfVendorOrdersJson = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorOrdersJson");
          final int _cursorIndexOfCartLevelDiscountAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "cartLevelDiscountAmount");
          final int _cursorIndexOfPromoCode = CursorUtil.getColumnIndexOrThrow(_cursor, "promoCode");
          final OrderEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final long _tmpCreatedAtEpoch;
            _tmpCreatedAtEpoch = _cursor.getLong(_cursorIndexOfCreatedAtEpoch);
            final String _tmpStatusJson;
            _tmpStatusJson = _cursor.getString(_cursorIndexOfStatusJson);
            final String _tmpVendorOrdersJson;
            _tmpVendorOrdersJson = _cursor.getString(_cursorIndexOfVendorOrdersJson);
            final double _tmpCartLevelDiscountAmount;
            _tmpCartLevelDiscountAmount = _cursor.getDouble(_cursorIndexOfCartLevelDiscountAmount);
            final String _tmpPromoCode;
            if (_cursor.isNull(_cursorIndexOfPromoCode)) {
              _tmpPromoCode = null;
            } else {
              _tmpPromoCode = _cursor.getString(_cursorIndexOfPromoCode);
            }
            _result = new OrderEntity(_tmpId,_tmpCreatedAtEpoch,_tmpStatusJson,_tmpVendorOrdersJson,_tmpCartLevelDiscountAmount,_tmpPromoCode);
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
  public Object getById(final String id, final Continuation<? super OrderEntity> $completion) {
    final String _sql = "SELECT * FROM orders WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<OrderEntity>() {
      @Override
      @Nullable
      public OrderEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreatedAtEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpoch");
          final int _cursorIndexOfStatusJson = CursorUtil.getColumnIndexOrThrow(_cursor, "statusJson");
          final int _cursorIndexOfVendorOrdersJson = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorOrdersJson");
          final int _cursorIndexOfCartLevelDiscountAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "cartLevelDiscountAmount");
          final int _cursorIndexOfPromoCode = CursorUtil.getColumnIndexOrThrow(_cursor, "promoCode");
          final OrderEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final long _tmpCreatedAtEpoch;
            _tmpCreatedAtEpoch = _cursor.getLong(_cursorIndexOfCreatedAtEpoch);
            final String _tmpStatusJson;
            _tmpStatusJson = _cursor.getString(_cursorIndexOfStatusJson);
            final String _tmpVendorOrdersJson;
            _tmpVendorOrdersJson = _cursor.getString(_cursorIndexOfVendorOrdersJson);
            final double _tmpCartLevelDiscountAmount;
            _tmpCartLevelDiscountAmount = _cursor.getDouble(_cursorIndexOfCartLevelDiscountAmount);
            final String _tmpPromoCode;
            if (_cursor.isNull(_cursorIndexOfPromoCode)) {
              _tmpPromoCode = null;
            } else {
              _tmpPromoCode = _cursor.getString(_cursorIndexOfPromoCode);
            }
            _result = new OrderEntity(_tmpId,_tmpCreatedAtEpoch,_tmpStatusJson,_tmpVendorOrdersJson,_tmpCartLevelDiscountAmount,_tmpPromoCode);
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
