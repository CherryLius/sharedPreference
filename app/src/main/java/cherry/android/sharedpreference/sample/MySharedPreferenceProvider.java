package cherry.android.sharedpreference.sample;

import android.support.annotation.NonNull;

import cherry.android.sharedpreference.SharedPreferenceProvider;

/**
 * Created by ROOT on 2017/8/21.
 */

public class MySharedPreferenceProvider extends SharedPreferenceProvider {
    @NonNull
    @Override
    public String getSharedPreferenceName() {
        return "test_name";
    }

    @NonNull
    @Override
    public String getAuthority() {
        return "cherry.android";
    }
}
