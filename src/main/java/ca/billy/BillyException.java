package ca.billy;

import ca.billy.line.LineWrapper;

public class BillyException extends RuntimeException {

    private static final long serialVersionUID = 2268749609573928796L;
    
    private static final String LINE_FORMAT = "%s - lineNumber=%d";

    public BillyException(String msg) {
        super(msg);
    }

    public BillyException(String msg, Exception e) {
        super(msg, e);
    }

    public BillyException(String msg, LineWrapper lineWrapper) {
        super(String.format(LINE_FORMAT, msg, lineWrapper.getLineNumber()));
    }

}
