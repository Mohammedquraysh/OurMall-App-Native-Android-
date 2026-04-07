package eu.ourmall.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import eu.ourmall.app.data.local.entity.CartItemEntity;
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
public final class CartDao_Impl implements CartDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CartItemEntity> __insertionAdapterOfCartItemEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateQuantity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfClearAll;

  private final SharedSQLiteStatement __preparedStmtOfUpdatePriceAndStock;

  public CartDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCartItemEntity = new EntityInsertionAdapter<CartItemEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `cart_items` (`productId`,`productName`,`imageUrl`,`originalPrice`,`discountPercent`,`offerExpiresAtEpoch`,`vendorId`,`vendorName`,`category`,`stockQuantity`,`quantity`,`snapshotPrice`,`appliedProductDiscount`,`addedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CartItemEntity entity) {
        statement.bindString(1, entity.getProductId());
        statement.bindString(2, entity.getProductName());
        statement.bindString(3, entity.getImageUrl());
        statement.bindDouble(4, entity.getOriginalPrice());
        statement.bindDouble(5, entity.getDiscountPercent());
        if (entity.getOfferExpiresAtEpoch() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getOfferExpiresAtEpoch());
        }
        statement.bindString(7, entity.getVendorId());
        statement.bindString(8, entity.getVendorName());
        statement.bindString(9, entity.getCategory());
        statement.bindLong(10, entity.getStockQuantity());
        statement.bindLong(11, entity.getQuantity());
        statement.bindDouble(12, entity.getSnapshotPrice());
        statement.bindDouble(13, entity.getAppliedProductDiscount());
        statement.bindLong(14, entity.getAddedAt());
      }
    };
    this.__preparedStmtOfUpdateQuantity = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE cart_items SET quantity = ? WHERE productId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cart_items WHERE productId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfClearAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM cart_items";
        return _query;
      }
    };
    this.__preparedStmtOfUpdatePriceAndStock = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE cart_items SET snapshotPrice = ?, appliedProductDiscount = ?, stockQuantity = ?, offerExpiresAtEpoch = ? WHERE productId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final CartItemEntity item, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCartItemEntity.insert(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateQuantity(final String id, final int qty,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateQuantity.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, qty);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfUpdateQuantity.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
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
  public Object clearAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearAll.acquire();
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
          __preparedStmtOfClearAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updatePriceAndStock(final String id, final double price, final double discount,
      final int stock, final Long expiresAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdatePriceAndStock.acquire();
        int _argIndex = 1;
        _stmt.bindDouble(_argIndex, price);
        _argIndex = 2;
        _stmt.bindDouble(_argIndex, discount);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, stock);
        _argIndex = 4;
        if (expiresAt == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, expiresAt);
        }
        _argIndex = 5;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfUpdatePriceAndStock.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CartItemEntity>> observeAll() {
    final String _sql = "SELECT * FROM cart_items ORDER BY addedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"cart_items"}, new Callable<List<CartItemEntity>>() {
      @Override
      @NonNull
      public List<CartItemEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfOriginalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "originalPrice");
          final int _cursorIndexOfDiscountPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "discountPercent");
          final int _cursorIndexOfOfferExpiresAtEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "offerExpiresAtEpoch");
          final int _cursorIndexOfVendorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorId");
          final int _cursorIndexOfVendorName = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorName");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfSnapshotPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "snapshotPrice");
          final int _cursorIndexOfAppliedProductDiscount = CursorUtil.getColumnIndexOrThrow(_cursor, "appliedProductDiscount");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final List<CartItemEntity> _result = new ArrayList<CartItemEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CartItemEntity _item;
            final String _tmpProductId;
            _tmpProductId = _cursor.getString(_cursorIndexOfProductId);
            final String _tmpProductName;
            _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final double _tmpOriginalPrice;
            _tmpOriginalPrice = _cursor.getDouble(_cursorIndexOfOriginalPrice);
            final double _tmpDiscountPercent;
            _tmpDiscountPercent = _cursor.getDouble(_cursorIndexOfDiscountPercent);
            final Long _tmpOfferExpiresAtEpoch;
            if (_cursor.isNull(_cursorIndexOfOfferExpiresAtEpoch)) {
              _tmpOfferExpiresAtEpoch = null;
            } else {
              _tmpOfferExpiresAtEpoch = _cursor.getLong(_cursorIndexOfOfferExpiresAtEpoch);
            }
            final String _tmpVendorId;
            _tmpVendorId = _cursor.getString(_cursorIndexOfVendorId);
            final String _tmpVendorName;
            _tmpVendorName = _cursor.getString(_cursorIndexOfVendorName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final int _tmpStockQuantity;
            _tmpStockQuantity = _cursor.getInt(_cursorIndexOfStockQuantity);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpSnapshotPrice;
            _tmpSnapshotPrice = _cursor.getDouble(_cursorIndexOfSnapshotPrice);
            final double _tmpAppliedProductDiscount;
            _tmpAppliedProductDiscount = _cursor.getDouble(_cursorIndexOfAppliedProductDiscount);
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            _item = new CartItemEntity(_tmpProductId,_tmpProductName,_tmpImageUrl,_tmpOriginalPrice,_tmpDiscountPercent,_tmpOfferExpiresAtEpoch,_tmpVendorId,_tmpVendorName,_tmpCategory,_tmpStockQuantity,_tmpQuantity,_tmpSnapshotPrice,_tmpAppliedProductDiscount,_tmpAddedAt);
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
  public Object getById(final String id, final Continuation<? super CartItemEntity> $completion) {
    final String _sql = "SELECT * FROM cart_items WHERE productId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CartItemEntity>() {
      @Override
      @Nullable
      public CartItemEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfProductId = CursorUtil.getColumnIndexOrThrow(_cursor, "productId");
          final int _cursorIndexOfProductName = CursorUtil.getColumnIndexOrThrow(_cursor, "productName");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfOriginalPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "originalPrice");
          final int _cursorIndexOfDiscountPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "discountPercent");
          final int _cursorIndexOfOfferExpiresAtEpoch = CursorUtil.getColumnIndexOrThrow(_cursor, "offerExpiresAtEpoch");
          final int _cursorIndexOfVendorId = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorId");
          final int _cursorIndexOfVendorName = CursorUtil.getColumnIndexOrThrow(_cursor, "vendorName");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfStockQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "stockQuantity");
          final int _cursorIndexOfQuantity = CursorUtil.getColumnIndexOrThrow(_cursor, "quantity");
          final int _cursorIndexOfSnapshotPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "snapshotPrice");
          final int _cursorIndexOfAppliedProductDiscount = CursorUtil.getColumnIndexOrThrow(_cursor, "appliedProductDiscount");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final CartItemEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpProductId;
            _tmpProductId = _cursor.getString(_cursorIndexOfProductId);
            final String _tmpProductName;
            _tmpProductName = _cursor.getString(_cursorIndexOfProductName);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            final double _tmpOriginalPrice;
            _tmpOriginalPrice = _cursor.getDouble(_cursorIndexOfOriginalPrice);
            final double _tmpDiscountPercent;
            _tmpDiscountPercent = _cursor.getDouble(_cursorIndexOfDiscountPercent);
            final Long _tmpOfferExpiresAtEpoch;
            if (_cursor.isNull(_cursorIndexOfOfferExpiresAtEpoch)) {
              _tmpOfferExpiresAtEpoch = null;
            } else {
              _tmpOfferExpiresAtEpoch = _cursor.getLong(_cursorIndexOfOfferExpiresAtEpoch);
            }
            final String _tmpVendorId;
            _tmpVendorId = _cursor.getString(_cursorIndexOfVendorId);
            final String _tmpVendorName;
            _tmpVendorName = _cursor.getString(_cursorIndexOfVendorName);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final int _tmpStockQuantity;
            _tmpStockQuantity = _cursor.getInt(_cursorIndexOfStockQuantity);
            final int _tmpQuantity;
            _tmpQuantity = _cursor.getInt(_cursorIndexOfQuantity);
            final double _tmpSnapshotPrice;
            _tmpSnapshotPrice = _cursor.getDouble(_cursorIndexOfSnapshotPrice);
            final double _tmpAppliedProductDiscount;
            _tmpAppliedProductDiscount = _cursor.getDouble(_cursorIndexOfAppliedProductDiscount);
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            _result = new CartItemEntity(_tmpProductId,_tmpProductName,_tmpImageUrl,_tmpOriginalPrice,_tmpDiscountPercent,_tmpOfferExpiresAtEpoch,_tmpVendorId,_tmpVendorName,_tmpCategory,_tmpStockQuantity,_tmpQuantity,_tmpSnapshotPrice,_tmpAppliedProductDiscount,_tmpAddedAt);
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
