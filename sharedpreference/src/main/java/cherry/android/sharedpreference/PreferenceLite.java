package cherry.android.sharedpreference;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * Created by ROOT on 2017/8/22.
 */

public class PreferenceLite implements Literate {

    private Context mContext;
    private final Uri mContentUri;

    public PreferenceLite(@NonNull Context context,
                          @NonNull String authority) {
        this.mContext = context;
        this.mContentUri = Uri.parse("content://" + authority + "/SharedPreferences");
    }

    @Override
    public <T> void put(@NonNull String key, @Nullable T value) {
        ContentValues values = new ContentValues();
        if (value == null) {
            values.putNull(key);
        } else {
            putContentValues(values, key, value);
        }
        this.mContext.getContentResolver().insert(mContentUri, values);
    }

    @Override
    public void delete(@NonNull String key) {
        this.mContext.getContentResolver().delete(mContentUri, "key=?", new String[]{key});
    }

    @Override
    public <T> T get(@NonNull String key, T defValue) {
        return convertCursor(key, defValue, new Converter<Cursor, T>() {
            @Override
            public T convert(Cursor cursor) {
                return (T) cursor.getString(0);
            }
        });
    }

    @Override
    public String get(@NonNull String key, String defValue) {
        return convertCursor(key, defValue, new Converter<Cursor, String>() {
            @Override
            public String convert(Cursor cursor) {
                return cursor.getString(0);
            }
        });
    }

    @Override
    public int get(@NonNull String key, @Nullable int defValue) {
        return convertCursor(key, defValue, new Converter<Cursor, Integer>() {
            @Override
            public Integer convert(Cursor cursor) {
                return cursor.getInt(0);
            }
        });
    }

    @Override
    public long get(@NonNull String key, @Nullable long defValue) {
        return convertCursor(key, defValue, new Converter<Cursor, Long>() {
            @Override
            public Long convert(Cursor cursor) {
                return cursor.getLong(0);
            }
        });
    }

    @Override
    public float get(@NonNull String key, @Nullable float defValue) {
        return convertCursor(key, defValue, new Converter<Cursor, Float>() {
            @Override
            public Float convert(Cursor cursor) {
                return cursor.getFloat(0);
            }
        });
    }

    @Override
    public boolean get(@NonNull String key, @Nullable boolean defValue) {
        return convertCursor(key, defValue, new Converter<Cursor, Boolean>() {
            @Override
            public Boolean convert(Cursor cursor) {
                return Boolean.valueOf(cursor.getString(0));
            }
        });
    }

    @Override
    public double get(@NonNull String key, @Nullable double defValue) {
        return convertCursor(key, defValue, new Converter<Cursor, Double>() {
            @Override
            public Double convert(Cursor cursor) {
                return cursor.getDouble(0);
            }
        });
    }

    @Override
    public short get(@NonNull String key, @Nullable short defValue) {
        return convertCursor(key, defValue, new Converter<Cursor, Short>() {
            @Override
            public Short convert(Cursor cursor) {
                return cursor.getShort(0);
            }
        });
    }

    public void call() {
        mContext.getContentResolver().call(mContentUri, "call", "tag", null);
    }

    private static Cursor resolveCursor(@NonNull Context context, Uri uri, @NonNull String key) {
        return context.getContentResolver().query(uri, new String[]{key}, null, null, null);
    }

    private <T> T convertCursor(String key, T defValue, @NonNull Converter<Cursor, T> converter) {
        T ret = null;
        Cursor cursor = resolveCursor(mContext, mContentUri, key);
        if (cursor == null)
            return defValue;
        if (cursor.moveToFirst()) {
            ret = converter.convert(cursor);
        }
        cursor.close();
        return ret != null ? ret : defValue;
    }

    private static void putContentValues(final ContentValues values, String key, Object value) {
        if (value instanceof Boolean) {
            values.put(key, (boolean) value);
        } else if (value instanceof Float) {
            values.put(key, (float) value);
        } else if (value instanceof Integer) {
            values.put(key, (int) value);
        } else if (value instanceof Long) {
            values.put(key, (long) value);
        } else if (value instanceof String) {
            values.put(key, (String) value);
        } else if (value instanceof Set) {

        } else {
            throw new UnsupportedOperationException("type not support! " + value.getClass());
        }
    }
}
