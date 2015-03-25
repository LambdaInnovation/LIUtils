package cn.liutils.ripple.impl.runtime;

import cn.liutils.ripple.ScriptNamespace;
import cn.liutils.ripple.ScriptStacktrace;

/**
 * Super class for all compiled function, used by code generator.
 * You should not directly implement this interface. Use NativeFunction instead.
 * @author acaly
 *
 */
public interface IFunction {
    
    Object call(Object[] args);
    void bind(ScriptNamespace env);
    
}
