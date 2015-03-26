package cn.liutils.ripple;

import java.io.InputStream;
import java.util.HashMap;

import cn.liutils.ripple.impl.compiler.FunctionClassLoader;
import cn.liutils.ripple.impl.runtime.Calculation;
import cn.liutils.ripple.impl.runtime.IFunction;
import cn.liutils.ripple.impl.runtime.Library;
import cn.liutils.ripple.RippleException.ScriptRuntimeException;

/**
 * The global object of a ripple program.
 * It contains all functions and values, and handles script file parsing.
 * @author acaly, WeAthFold
 *
 */
public final class ScriptProgram {
    
    private final FunctionClassLoader classLoader = new FunctionClassLoader();
    private final ScriptNamespace rootNamespace = new ScriptNamespace(this, new Path(null));
    private final HashMap<String, Object> objectMap = new HashMap();
    
    public ScriptProgram() {
        Library.openLibrary(this);
    }
    
    public void loadScript(InputStream input) {
        //TODO
        //Call parse. Use classLoader to initialize a new environment.
    }
    
    //Never returns null
    public ScriptNamespace root() {
        return rootNamespace;
    }
    
    public ScriptNamespace at(Path path) {
        return new ScriptNamespace(this, path);
    }
    
    //may return an ScriptFunction, Integer, Double, Boolean or null.
    Object getObjectAt(Path path) {
        synchronized (this) {
            return objectMap.get(path.path);
        }
    }
    
    void setValueAt(Path path, Object value) {
        if (!Calculation.checkType(value)) {
            throw new ScriptRuntimeException("Invalid value type");
        }
        synchronized (this) {
            if (objectMap.containsKey(path.path)) {
                throw new ScriptRuntimeException("Try to modify an existing object");
            }
            objectMap.put(path.path, value);
        }
    }
    
    void mergeFunctionAt(Path path, IFunction value, int nargs) {
        synchronized (this) {
            Object objInMap = objectMap.get(path.path);
            if (objInMap == null) {
                ScriptFunction sf = new ScriptFunction(new ScriptNamespace(this, path));
                sf.merge(value, nargs);
                objectMap.put(path.path, sf);
            } else if (objInMap instanceof ScriptFunction) {
                ((ScriptFunction) objInMap).merge(value, nargs);
            } else {
                throw new ScriptRuntimeException("Try to override a value with a function");
            }
        }
    }
}
