package ca.billy.line;

import ca.billy.Const;
import ca.billy.Log;
import lombok.Getter;

public class LineWrapper {

    private final static char INDENT = '\t';

    private static int lineCounter = 0;

    @Getter
    private String line;

    @Getter
    private int lineNumber;

    public LineWrapper(String line) {
        this.line = removeComment(line);
        lineNumber = ++lineCounter;

        if (Log.isEnabled()) {
            print();
        }
    }

    public boolean isEmpty() {
        return line.trim().length() == 0;
    }

    public int getIndent() {
        int index = 0;
        while (INDENT == line.charAt(index))
            ++index;

        return index;
    }

    private void print() {
        Log.log(lineNumber + ":" + removeComment(line));
    }

    private String removeComment(String line) {
        int index = line.indexOf(Const.COMMENT);
        return index == -1 ? line : index == 0 ? Const.EMPTY : line.substring(0, index - 1);
    }

    /** Reset the lineCounter. Only used in test. */
    public static void reset() {
        lineCounter = 0;
    }

}
