package cn.liutils.ripple;

import java.io.IOException;

import cn.liutils.ripple.impl.compiler.Parser;

/**
 * Super class for all exceptions that may be thrown in Ripple.
 * @author acaly
 *
 */
public class RippleException extends RuntimeException {
    
    public RippleException(String message) {
        super(message);
    }
    
    public RippleException(Throwable cause) {
        super(cause);
    }
    
    public RippleException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static class RippleCompilerException extends RippleException {
        
        /* Parser instance here */
        
        public RippleCompilerException(String message, Parser parser) {
            super(message);
        }
        
        public RippleCompilerException(String message, Parser parser, Throwable cause) {
            super(message, cause);
        }

        public RippleCompilerException(Throwable cause, Parser parser) {
            super(cause);
        }
    }
    
    public static class RippleRuntimeException extends RippleException {
        
        public final ScriptStacktrace stacktrace;
        
        public RippleRuntimeException(Throwable cause) {
            super(cause);
            this.stacktrace = ScriptStacktrace.getStacktrace();
        }
        
        public RippleRuntimeException(String message) {
            super(message);
            this.stacktrace = ScriptStacktrace.getStacktrace();
        }
        
        public RippleRuntimeException(String message, Throwable cause) {
            super(message, cause);
            this.stacktrace = ScriptStacktrace.getStacktrace();
        }
        
    }
}
