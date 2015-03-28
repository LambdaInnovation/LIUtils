package cn.liutils.ripple;

import cn.liutils.ripple.RippleException.ScriptRuntimeException;

/**
 * A helper class used to find objects (functions and values) from
 * a ScriptProgram.
 * @author acaly
 *
 */
public final class ScriptNamespace {
    
    public final ScriptProgram program;
    public final Path path;
    
    ScriptNamespace(ScriptProgram program, Path path) {
        this.program = program;
        this.path = path;
    }

    public ScriptFunction getFunction(String key) {
        Object func = program.getObjectAt(new Path(this.path, key));
        if (func != null) {
            if (func instanceof ScriptFunction)
                return (ScriptFunction) func;
            else {
                throw new ScriptRuntimeException("Try to convert a value to function");
            }
        }
        throw new ScriptRuntimeException("Function not found");
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
    
    public Object getValue(String key) {
        Object value = program.getObjectAt(new Path(this.path, key));
        if (value != null) {
            if (Calculation.checkType(value)) {
                return value;
            } else {
                throw new ScriptRuntimeException("Try to convert a function to value");
            }
        }
        throw new ScriptRuntimeException("Value not found");
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
        throw new ScriptRuntimeException("Value not found");
    }
    
    public int getInteger(Path path) {
        return Calculation.castInt(getValue(path));
    }
    
    public int getInteger(String key) {
        return Calculation.castInt(getValue(key));
    }
    
    public double getDouble(Path path) {
        return Calculation.castDouble(getValue(path));
    }
    
    public double getDouble(String key) {
        return Calculation.castDouble(getValue(key));
    }
    
    public boolean getBoolean(Path path) {
        return Calculation.castBoolean(getValue(path));
    }
    
    public boolean getBoolean(String key) {
        return Calculation.castBoolean(getValue(key));
    }
    
    public void setConst(String key, Object value) {
        if (!Calculation.checkType(value)) {
            throw new ScriptRuntimeException("Invalid const value type");
        }
        program.setValueAt(new Path(path, key), value);
    }
    
    public void setNativeFunction(String key, NativeFunction func) {
        program.mergeFunctionAt(new Path(path, key), func, func.getParamterCount());
        func.bind(program, new Path(path, key));
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
    
    public ScriptNamespace getSubNamespace(String path) {
        return getSubNamespace(new Path(path));
    }
}
