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

    @Getter
    private int indent;

    public LineWrapper(String line) {
        lineNumber = ++lineCounter;
        if (Log.isEnabled()) {
            print(line);
        }
        
        line = removeComment(line);
        this.line = line.substring(indent = getIndent(line)).trim();
    }
    
    public LineWrapper(String line, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
    }

    public boolean isEmpty() {
        return line.length() == 0;
    }
    
    private String removeComment(String line) {
        int index = line.indexOf(Const.COMMENT);
        return index == -1 ? line : index == 0 ? Const.EMPTY : line.substring(0, index - 1);
    }

    private int getIndent(String line) {        
        int index = 0;
        while (line.length() != index && INDENT == line.charAt(index))
            ++index;

        return index;
    }
   
    private void print(String line) {
        Log.log(lineNumber + ":" + line);
    }

    /** Reset the lineCounter. Only used in test. */
    public static void reset() {
        lineCounter = 0;
    }

}
