package ca.billy;

public class BillyException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 2268749609573928796L;

    public BillyException(String msg) {
        super(msg);
    }

    public BillyException(String msg, Exception e) {
        super(msg, e);
    }

}

