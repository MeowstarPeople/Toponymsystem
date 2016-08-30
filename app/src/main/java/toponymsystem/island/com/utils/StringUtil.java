package toponymsystem.island.com.utils;

/**
 * Created by user on 2015/7/1.
 */
public class StringUtil {

    /**
     * 计算一个字符串的byte长度（一个汉字多少字符）
     *
     * @param str
     * @return
     */
    public static int count(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int count = 0;
        char[] chs = str.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            count += (chs[i] > 0xff) ? 2 : 1;
        }
        return count;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public static final double str2Double(String s) {
        if (isEmpty(s)) {
            return 0.0;
        }
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public static float str2Float(String s) {
        try {
            return Float.parseFloat(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    public static long strLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}



