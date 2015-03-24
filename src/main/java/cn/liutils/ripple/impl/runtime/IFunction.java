package cn.liutils.ripple.impl.runtime;

import cn.liutils.ripple.ScriptNamespace;

/**
 * Super class for all compiled function, used by code generator.
 * @author acaly
 *
 */
public interface IFunction {
    
    Object call(Object[] args);
    void bind(ScriptNamespace env);
    
}
