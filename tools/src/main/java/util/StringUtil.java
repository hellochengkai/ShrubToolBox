package util;

public class StringUtil {

    public static boolean isEmpty(String s)
    {
        return s == null
                || s.isEmpty()
                || s.length() == 0
                || "".equals(s)
                || s.toLowerCase().equals("null");
    }
}
