package cn.liutils.ripple;

import cn.liutils.ripple.impl.runtime.Calculation;
import cn.liutils.ripple.RippleException.ScriptRuntimeException;

/**
 * A helper class used to find objects (functions and values) from
 * a ScriptProgram.
 * @author acaly
 *
 */
public final class ScriptNamespace {
    
    private final ScriptProgram program;
    private final Path path;
    
    ScriptNamespace(ScriptProgram program, Path path) {
        this.program = program;
        this.path = path;
    }

    public ScriptFunction getFunction(Path path) {
        Object func = program.getObjectAt(new Path(this.path, path));
        if (func != null) {
            if (func instanceof ScriptFunction)
                return (ScriptFunction) func;
            else {
                throw new ScriptRuntimeException("Try to convert a value to function");
            }
        }
        if (this.path.hasParent()) {
            return program.at(this.path.getParent()).getFunction(path);
        }
        return null;
    }
    
    public Object getValue(Path path) {
        Object value = program.getObjectAt(new Path(this.path, path));
        if (value != null) {
            if (Calculation.checkType(value)) {
                return value;
            } else {
                throw new ScriptRuntimeException("Try to convert a function to value");
            }
        }
        if (this.path.hasParent()) {
            return program.at(this.path.getParent()).getValue(path);
        }
        return null;
    }
    
    public int getInteger(Path path) {
        return Calculation.castInt(getValue(path));
    }
    
    public double getDouble(Path path) {
        return Calculation.castDouble(getValue(path));
    }
    
    public boolean getBoolean(Path path) {
        return Calculation.castBoolean(getValue(path));
    }
    
    public void setConst(String key, Object value) {
        if (!Calculation.checkType(value)) {
            throw new ScriptRuntimeException("Invalid const value type");
        }
        program.setValueAt(new Path(path, key), value);
    }
    
    public void setNativeFunction(String key, NativeFunction func) {
        program.mergeFunctionAt(new Path(path, key), func, func.getParamterCount());
    }
    
    /**
     * Get the namespace under this namespace.
     * Note that this function never check the parent namespace, as a namespace always exists.
     * @param path
     * @return
     */
    public ScriptNamespace getSubNamespace(Path path) {
        return new ScriptNamespace(program, new Path(this.path, path));
    }
}
