package cherry.android.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ROOT on 2017/8/21.
 */

/*public*/ class SharedPreferenceCursor extends AbstractWindowedCursor {
    private static final String TAG = "SharedPreferenceCursor";
    private static final int NO_COUNT = -1;

    private SharedPreferences mPreference;
    private String mPreferenceName;
    private final String[] mColumns;
    private int mCount = NO_COUNT;
    private Map<String, Integer> mColumnNameMap;

    public SharedPreferenceCursor(Context context, String preferenceName, String[] keys) {
        super();
        mPreferenceName = preferenceName;
        mPreference = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        Set<String> set = mPreference.getAll().keySet();
        if (keys == null || keys.length == 0) {
            List<String> list = new ArrayList<>(set);
            keys = list.toArray(new String[set.size()]);
        }
        mColumns = keys;
    }

    @Override
    public boolean onMove(int oldPosition, int newPosition) {
        if (mWindow == null || newPosition < mWindow.getStartPosition()
                || newPosition >= (mWindow.getStartPosition() + mWindow.getNumRows())) {
            fillWindow(newPosition);
        }
        return true;
    }

    private void fillWindow(int requiredPos) {
        clearOrCreateWindow(mPreferenceName);
        try {
            if (mCount == NO_COUNT) {
                mCount = customFillWindow(requiredPos, mWindow);
            } else {
                customFillWindow(requiredPos, mWindow);
            }
        } catch (RuntimeException ex) {
            closeWindow();
            throw ex;
        }
    }

    @Override
    public int getCount() {
        if (mCount == NO_COUNT)
            fillWindow(0);
        return mCount;
    }

    @Override
    public String[] getColumnNames() {
        return mColumns;
    }

    @Override
    public int getColumnIndex(String columnName) {
        if (mColumnNameMap == null) {
            String[] columns = mColumns;
            int columnCount = columns.length;
            Map<String, Integer> map = new HashMap<>(columnCount, 1);
            for (int i = 0; i < columnCount; i++) {
                map.put(columns[i], i);
            }
            mColumnNameMap = map;
        }

        final int periodIndex = columnName.lastIndexOf('.');
        if (periodIndex != -1) {
            Exception e = new Exception();
            Log.e(TAG, "requesting column name with table name -- " + columnName, e);
            columnName = columnName.substring(periodIndex + 1);
        }

        Integer i = mColumnNameMap.get(columnName);
        if (i != null) {
            return i.intValue();
        } else {
            return -1;
        }
    }

    @Override
    public void setWindow(CursorWindow window) {
        super.setWindow(window);
        mCount = NO_COUNT;
    }

    private int getType(String key) {
        Map<String, ?> map = mPreference.getAll();
        Object value = map.get(key);
        if (value instanceof Integer)
            return ValueType.TYPE_INTEGER;
        else if (value instanceof String)
            return ValueType.TYPE_STRING;
        else if (value instanceof Float)
            return ValueType.TYPE_FLOAT;
        else if (value instanceof Long)
            return ValueType.TYPE_LONG;
        else if (value instanceof Boolean)
            return ValueType.TYPE_BOOLEAN;
        else
            return Cursor.FIELD_TYPE_NULL;
    }

    private int customFillWindow(int requiredPos, CursorWindow window) {
        final int numColumns = getColumnCount();
        window.clear();
        window.setStartPosition(requiredPos);
        window.setNumColumns(numColumns);
        if (!window.allocRow())
            return -1;
        for (int i = 0; i < numColumns; i++) {
            final String key = getColumnName(i);
            final int type = getType(key);
            final boolean success;
            switch (type) {
                case ValueType.TYPE_BOOLEAN:
                    final boolean b = mPreference.getBoolean(key, false);
                    success = window.putLong(b ? 1 : 0, requiredPos, i);
                    break;
                case ValueType.TYPE_STRING:
                    final String s = mPreference.getString(key, null);
                    success = s != null ? window.putString(s, requiredPos, i) : window.putNull(requiredPos, i);
                    break;
                case ValueType.TYPE_FLOAT:
                    final float f = mPreference.getFloat(key, 0);
                    success = window.putDouble(f, requiredPos, i);
                    break;
                case ValueType.TYPE_LONG:
                    final long l = mPreference.getLong(key, 0);
                    success = window.putLong(l, requiredPos, i);
                    break;
                case ValueType.TYPE_INTEGER:
                    final int j = mPreference.getInt(key, 0);
                    success = window.putLong(j, requiredPos, i);
                    break;
                case ValueType.TYPE_NULL:
                default:
                    success = window.putNull(requiredPos, i);
                    break;
            }
            if (!success) {
                window.freeLastRow();
            }
        }
        return 1;
    }

    protected void closeWindow() {
        if (mWindow != null) {
            mWindow.close();
            mWindow = null;
        }
    }

    protected void clearOrCreateWindow(String name) {
        if (mWindow == null) {
            mWindow = new CursorWindow(name);
        } else {
            mWindow.clear();
        }
    }

    static class ValueType {
        public static final int TYPE_NULL = 0;
        public static final int TYPE_STRING = 1;
        public static final int TYPE_BOOLEAN = 2;
        public static final int TYPE_INTEGER = 3;
        public static final int TYPE_FLOAT = 4;
        public static final int TYPE_LONG = 5;
    }
}
