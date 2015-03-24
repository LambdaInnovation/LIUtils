package cn.liutils.ripple;

/**
 * Super class for all exceptions that may be thrown in Ripple.
 * @author acaly
 *
 */
public class RippleException extends RuntimeException {
    
    public RippleException(String message) {
        super(message);
    }
    
    public RippleException(String message, Throwable cause) {
        super(message, cause);
    }
    
    //TODO add constructors receiving script object
}
