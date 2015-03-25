package cn.liutils.ripple;

import cn.liutils.ripple.impl.runtime.Calculation;
import cn.liutils.ripple.impl.runtime.IFunction;
import cn.liutils.ripple.RippleException.ScriptRuntimeException;

/**
 * A wrapped function object compiled from script.
 * @author acaly
 *
 */
public final class ScriptFunction {
    
    private final ScriptProgram parent;
    private IFunction[] internalFunc;
    
    public ScriptFunction(ScriptProgram parent) {
        this.parent = parent;
        this.internalFunc = new IFunction[0];
    }
    
    public void merge(IFunction newFunc, int sizeArg) {
        synchronized (this) {
            if (internalFunc.length <= sizeArg) {
                IFunction[] newFuncArray = new IFunction[sizeArg + 1];
                for (int i = 0; i < internalFunc.length; ++i) {
                    newFuncArray[i] = internalFunc[i];
                }
                newFuncArray[sizeArg] = newFunc;
                internalFunc = newFuncArray;
            } else if (internalFunc[sizeArg] == null) {
                internalFunc[sizeArg] = newFunc;
            } else {
                throw new ScriptRuntimeException("Function overloading fails. Argument number " + sizeArg);
            }
        }
    }
    
    private IFunction getOverload(int sizeArg) {
        synchronized (this) {
            if (sizeArg >= internalFunc.length || internalFunc[sizeArg] == null) {
                throw new ScriptRuntimeException("Function overload not found. Argument number " + sizeArg);
            }
            return internalFunc[sizeArg];
        }
    }
    
    public Object callObject(Object... args) {
        return getOverload(args.length).call(args);
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

