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
    
    public static class ScriptCompilerException extends RippleException {
        
        /* Parser instance here */
        
        public ScriptCompilerException(String message /* Parser instance here */) {
            super(message);
        }
        
    }
    
    public static class ScriptRuntimeException extends RippleException {
        
        public final ScriptStacktrace stacktrace;
        
        public ScriptRuntimeException(String message) {
            super(message);
            this.stacktrace = ScriptStacktrace.getStacktrace();
        }
        
        public ScriptRuntimeException(String message, Throwable cause) {
            super(message, cause);
            this.stacktrace = ScriptStacktrace.getStacktrace();
        }
        
    }
}
