package ca.billy.line;

import ca.billy.Log;
import ca.billy.util.LineUtil;
import lombok.Getter;

public class LineWrapper {

    @Getter
    private String line;

    @Getter
    private int lineNumber;

    @Getter
    private int indent;

    public LineWrapper(String line) {
        lineNumber = LineUtil.getLineNumber();
        if (Log.isEnabled()) {
            print(line);
        }
        line = LineUtil.removeComment(line);
        this.line = line.trim();
        
        if (!isEmpty()) {
            indent = LineUtil.getIndent(line);
        }
    }

    public LineWrapper(String line, int lineNumber) {
        this.line = line;
        this.lineNumber = lineNumber;
    }

    public boolean isEmpty() {
        return line.length() == 0;
    }

    private void print(String line) {
        Log.log(lineNumber + ":" + line);
    }
}
