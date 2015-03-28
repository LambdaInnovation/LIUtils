package cn.liutils.ripple;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import cn.liutils.ripple.impl.compiler.FunctionClassLoader;
import cn.liutils.ripple.impl.compiler.Parser;
import cn.liutils.ripple.impl.compiler.Parser.ScriptObject;
import cn.liutils.ripple.RippleException.ScriptRuntimeException;

/**
 * The global object of a ripple program.
 * It contains all functions and values, and handles script file parsing.
 * @author acaly, WeAthFold
 *
 */
public final class ScriptProgram {
    
    public final ScriptNamespace root = new ScriptNamespace(this, new Path(null));
    private final FunctionClassLoader classLoader = new FunctionClassLoader();
    private final HashMap<String, Object> objectMap = new HashMap();
    
    public ScriptProgram() {
        Library.openLibrary(this);
    }
    
    public void loadScript(InputStream input) {
        List<ScriptObject> objects = Parser.parse(this, input, classLoader);
        for (ScriptObject object : objects) {
            if (object.value == null) {
                this.mergeFunctionAt(new Path(object.path), object.func, object.funcArgNum);
            } else {
                this.setValueAt(new Path(object.path), object.value);
            }
        }
        //TODO make it thread safe
    }
    
    public ScriptNamespace at(Path path) {
        return new ScriptNamespace(this, path);
    }
    
    //may return an FunctionWrapper, Integer, Double, Boolean or null.
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
                ScriptFunction sf = new ScriptFunction(this, path);
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
