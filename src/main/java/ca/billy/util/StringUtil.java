package ca.billy.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

    public static String[] splitOnFirst(String string, String split) {
        int index = string.indexOf(split);
        return new String[] { string.substring(0, index).trim(), string.substring(index + split.length()).trim() };
    }
}
