package android.text;

/**
 * Created by ROOT on 2017/8/3.
 */

public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.equals(""))
            return true;
        return false;
    }
}
