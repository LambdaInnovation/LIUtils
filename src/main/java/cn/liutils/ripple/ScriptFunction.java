package cn.liutils.ripple;

import cn.liutils.ripple.impl.runtime.Calculation;
import cn.liutils.ripple.impl.runtime.IFunction;

/**
 * A wrapped function object compiled from script.
 * @author acaly
 *
 */
public final class ScriptFunction {
    
    private final ScriptProgram parent;
    private final IFunction internalFunc;
    
    public ScriptFunction(ScriptProgram parent, IFunction internalFunc) {
        this.parent = parent;
        this.internalFunc = internalFunc;
    }
    
    public Object callObject(Object... args) {
        return internalFunc.call(args);
    }
    
    public int callInteger(Object... args) {
        return Calculation.castInt(callObject(args));
    }
    
    public double callDouble(Object... args) {
        return Calculation.castDouble(callObject(args));
    }
    
    public boolean callBoolean(Object... args) {
        return Calculation.castBoolean(callObject(args));
    }
    
}

