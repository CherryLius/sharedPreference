package cherry.android.sharedpreference;


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import static cherry.android.sharedpreference.Utils.checkNotNull;

public abstract class SharedPreferenceProvider extends ContentProvider {

    private static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.sharedPreference";
    private static final int PREFERENCE = 0;
    private final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

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
        this.mPreferenceName = checkNotNull(getSharedPreferenceName());
        this.mAuthority = checkNotNull(getAuthority());
        mUriMatcher.addURI(this.mAuthority, "SharedPreferences", 0);
        this.mContentUri = Uri.parse("content://" + this.mAuthority + "/SharedPreferences");

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
        final int type = mUriMatcher.match(uri);
        if (type == PREFERENCE) {
            return CONTENT_TYPE;
        }
        throw new IllegalArgumentException("Unknown Uri: " + uri);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int size = values != null && values.size() > 0 ? values.size() : 0;
        if (size > 0) {
            SharedPreferences.Editor editor = mPreference.edit();
            for (String key : values.keySet()) {
                putValueToPreference(editor, key, values.get(key));
            }
            editor.apply();
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Map<String, String> map = Utils.parseSelection(selection, selectionArgs);
        if (map == null)
            throw new IllegalArgumentException("SharedPreference delete must contains KEY");
        if (map.size() == 0)
            return -1;
        SharedPreferences.Editor editor = mPreference.edit();
        for (String value : map.values()) {
            editor.remove(value);
        }
        editor.apply();
        return map.size();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        insert(uri, values);
        return values.size();
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Log.i("Test", "call + " + method + " + " + Process.myPid());
        return super.call(method, arg, extras);
    }

    private static void putValueToPreference(SharedPreferences.Editor edit, String key, Object value) {
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
}
