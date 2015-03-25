package cn.liutils.ripple;

import cn.liutils.ripple.NativeFunction.NativeFunctionFrame;

/**
 * Stack trace of the current thread. 
 * @author acaly
 *
 */
public final class ScriptStacktrace {
    
    private ScriptStacktrace() {
        
    }
    
    public static ScriptStacktrace getStacktrace() {
        return null; //TODO
    }
    
    static void pushNativeFrame(NativeFunctionFrame frame) {
        
    }
    
    static void popNativeFrame() {
        
    }
    
}
