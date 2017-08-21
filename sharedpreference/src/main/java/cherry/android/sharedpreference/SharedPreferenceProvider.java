package cherry.android.sharedpreference;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

public abstract class SharedPreferenceProvider extends ContentProvider {

    private ContentResolver mResolver;
    private SharedPreferences mPreference;

    private final String mPreferenceName;
    private final String mAuthority;
    private final Uri mContentUri;

    @NonNull
    public abstract String getSharedPreferenceName();

    @NonNull
    public abstract String getAuthority();

    public SharedPreferenceProvider() {
        this.mPreferenceName = getSharedPreferenceName();
        checkNotNull(this.mPreferenceName);
        this.mAuthority = getAuthority();
        checkNotNull(this.mAuthority);
        this.mContentUri = Uri.parse("content://" + this.mAuthority + "/SharedPreferenceProvider");
    }

    @Override
    public boolean onCreate() {
        mResolver = getContext().getContentResolver();
        mPreference = getContext().getSharedPreferences(this.mPreferenceName, Context.MODE_PRIVATE);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SharedPreferenceCursor cursor = new SharedPreferenceCursor(getContext(), this.mPreferenceName, projection);
        cursor.setNotificationUri(mResolver, mContentUri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int size = values != null && values.size() > 0 ? values.size() : 0;
        if (size > 0) {
            SharedPreferences.Editor edit = mPreference.edit();
            for (String key : values.keySet()) {
                putValueToPreference(edit, key, values.get(key));
            }
            edit.apply();
        }
        return uri;
    }

    private void putValueToPreference(SharedPreferences.Editor edit, String key, Object value) {
        if (value == null) {
            edit.putString(key, null);
            return;
        }
        if (value instanceof Boolean) {
            edit.putBoolean(key, (boolean) value);
        } else if (value instanceof Float) {
            edit.putFloat(key, (float) value);
        } else if (value instanceof Integer) {
            edit.putInt(key, (int) value);
        } else if (value instanceof Long) {
            edit.putLong(key, (long) value);
        } else if (value instanceof String) {
            edit.putString(key, (String) value);
        } else if (value instanceof Set) {
            Type type = value.getClass();
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypes = parameterizedType.getActualTypeArguments();
            if (!actualTypes[0].equals(String.class))
                throw new IllegalArgumentException("value must be Set<String>, current is " + type);
            edit.putStringSet(key, (Set<String>) value);
        } else {
            throw new UnsupportedOperationException("type not support! " + value.getClass());
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private static <T> void checkNotNull(T t) {
        if (t == null)
            throw new NullPointerException("Null Unsupported.");
    }
}
