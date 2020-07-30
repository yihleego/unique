package io.leego.unique.common.util;

/**
 * @author Yihleego
 */
public final class StringUtils {
    public static final String EMPTY = "";
    public static final String NULL = null;
    public static final String LINE_BREAK = "[\n\r]";

    private StringUtils() {
    }

    public static boolean isNull(CharSequence text) {
        return text == null;
    }

    public static boolean isNotNull(CharSequence text) {
        return text != null;
    }

    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence text) {
        return !isEmpty(text);
    }

    public static boolean isBlank(CharSequence text) {
        if (isEmpty(text)) {
            return true;
        }
        for (int i = 0, len = text.length(); i < len; ++i) {
            if (!Character.isWhitespace(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence text) {
        return isNotEmpty(text) && !isBlank(text);
    }

    public static int getLength(CharSequence text) {
        if (text == null) {
            return 0;
        }
        return text.length();
    }

    public static String trim(String text) {
        return text == null ? null : text.trim();
    }

    public static String trimToEmpty(String text) {
        return text == null ? EMPTY : text.trim();
    }

    public static String trimToNull(String text) {
        String trimmed = trim(text);
        return isEmpty(trimmed) ? null : trimmed;
    }

    public static String trimLeft(String value) {
        if (isEmpty(value)) {
            return value;
        }
        int len = value.length();
        int st = 0;
        while ((st < len) && (value.charAt(st) <= ' ')) {
            st++;
        }
        if (st == len) {
            return EMPTY;
        }
        return st > 0 ? value.substring(st, len) : value;
    }

    public static String trimRight(String value) {
        if (isEmpty(value)) {
            return value;
        }
        int len = value.length();
        while ((0 < len) && (value.charAt(len - 1) <= ' ')) {
            len--;
        }
        if (len == 0) {
            return EMPTY;
        }
        return len < value.length() ? value.substring(0, len) : value;
    }

}
