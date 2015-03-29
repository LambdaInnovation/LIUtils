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
    
    public static class ScriptCompilerException extends RippleException {
        
        /* Parser instance here */
        
        public ScriptCompilerException(String message, Parser parser) {
            super(message);
        }
        
        public ScriptCompilerException(String message, Parser parser, Throwable cause) {
            super(message, cause);
        }

        public ScriptCompilerException(Throwable cause, Parser parser) {
            super(cause);
        }
    }
    
    public static class ScriptRuntimeException extends RippleException {
        
        public final ScriptStacktrace stacktrace;
        
        public ScriptRuntimeException(Throwable cause) {
            super(cause);
            this.stacktrace = ScriptStacktrace.getStacktrace();
        }
        
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
