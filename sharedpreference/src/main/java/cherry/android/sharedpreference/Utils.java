package cherry.android.sharedpreference;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import java.util.Map;

/**
 * Created by ROOT on 2017/8/22.
 */

/*public*/ final class Utils {
    private Utils() {
        throw new AssertionError("No instance.");
    }

    static <T> T checkNotNull(T t) {
        if (t == null)
            throw new NullPointerException("Null Unsupported.");
        return t;
    }

    // 没有区分and or
    static Map<String, String> parseSelection(String selection, String[] args) {
        if (TextUtils.isEmpty(selection) ||
                args == null || args.length == 0)
            return null;
        ArrayMap<String, String> map = new ArrayMap<>();
        int argsCount = count(selection, "\\?");
        if (argsCount != args.length)
            throw new IllegalArgumentException("Arguments Count Not Match.");
        selection = selection.replaceAll("\\?", "%s");
        Object[] objectArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            objectArgs[i] = args[i];
        }
        String format = String.format(selection, objectArgs);
        String[] splits = format.split("and|or");
        for (String split : splits) {
            String[] keyValue = split.trim().split("=");
            if (TextUtils.isEmpty(keyValue[1]))
                continue;
            map.put(keyValue[0], keyValue[1]);
        }
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "--" + entry.getValue());
        }
        return map;
    }

    private static int count(String src, String regex) {
        String tmp = src.replaceAll(regex, "");
        return src.length() - tmp.length();
    }

}
