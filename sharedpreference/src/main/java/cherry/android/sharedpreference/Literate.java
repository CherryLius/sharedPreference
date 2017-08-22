package cherry.android.sharedpreference;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by ROOT on 2017/8/22.
 */

public interface Literate {
    <T> void put(@NonNull String key, @Nullable T value);

    void delete(@NonNull String key);

    <T> T get(@NonNull String key, @Nullable T defValue);

    String get(@NonNull String key, @Nullable String defValue);

    int get(@NonNull String key, @Nullable int defValue);

    long get(@NonNull String key, @Nullable long defValue);

    float get(@NonNull String key, @Nullable float defValue);

    boolean get(@NonNull String key, @Nullable boolean defValue);

    double get(@NonNull String key, @Nullable double defValue);

    short get(@NonNull String key, @Nullable short defValue);
}