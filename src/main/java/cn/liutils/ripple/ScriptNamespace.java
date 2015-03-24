package cn.liutils.ripple;

import cn.liutils.ripple.impl.runtime.Calculation;

/**
 * A helper class used to find objects (functions and values) from
 * a ScriptProgram.
 * @author acaly
 *
 */
public final class ScriptNamespace {
    
    private final ScriptProgram parent;
    private final Path path;
    
    ScriptNamespace(ScriptProgram parent, Path path) {
        this.parent = parent;
        this.path = path;
    }

    public ScriptFunction getFunction(Path path) {
        return null; //TODO
    }
    
    public Object getValue(Path path) {
        return null; //TODO
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
    
    public ScriptNamespace getNamespace(Path path) {
        return new ScriptNamespace(parent, new Path(this.path, path));
    }
}
