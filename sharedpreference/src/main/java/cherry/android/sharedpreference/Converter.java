package cherry.android.sharedpreference;

/**
 * Created by ROOT on 2017/8/22.
 */

interface Converter<T, R> {
    R convert(T t);
}