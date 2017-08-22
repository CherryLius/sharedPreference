package cherry.android.sharedpreference;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by ROOT on 2017/8/22.
 */
public class UtilsTest {
    @Test
    public void parseSelection() throws Exception {
        Utils.parseSelection("id=? and name=1 or age=?", new String[]{"2", "1"});
    }

}