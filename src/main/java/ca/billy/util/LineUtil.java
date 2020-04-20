package ca.billy.util;

import ca.billy.BillyException;
import ca.billy.Const;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineUtil {

    private final static char INDENT = '\t';
    private final static char SPACE = ' ';

    private static Integer lineCounter = 0;
    private static Integer spaceNumber = 4;

    public static void setSpaceByTab(int spaceNumber) {
        LineUtil.spaceNumber = spaceNumber;
    }

    public static int getLineNumber() {
        return ++lineCounter;
    }
    
    /** Reset the lineCounter. Only used in test. */
    public static void resetLineNumber() {
        lineCounter = 0;
    }

    public static int getIndent(String line) {
        boolean stop = false;
        int ident = 0, space = 0;
        for (int index = 0; line.length() != index && !stop; ++index) {
            if (INDENT == line.charAt(index)) {
                ++ident;
            } else if (SPACE == line.charAt(index)) {
                if (++space == spaceNumber) {
                    space = 0;
                    ++ident;
                }
            } else {
                stop = true;
            }
        }

        if (space != 0) {
            throw new BillyException("Invalid number of space: " + line);
        }

        return ident;
    }

    public static String removeComment(String line) {
        int index = line.indexOf(Const.COMMENT);
        return index == -1 ? line : index == 0 ? Const.EMPTY : line.substring(0, index - 1);
    }

}
