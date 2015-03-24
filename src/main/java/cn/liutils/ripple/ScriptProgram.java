package cn.liutils.ripple;

import java.io.InputStream;

import cn.liutils.ripple.impl.compiler.FunctionClassLoader;
import cn.liutils.ripple.impl.runtime.Calculation;

/**
 * The global object of a ripple program.
 * It contains all functions and values, and handles script file parsing.
 * @author acaly, WeAthFold
 *
 */
public final class ScriptProgram {
    
    private FunctionClassLoader classLoader = new FunctionClassLoader();
    private ScriptNamespace rootNamespace = new ScriptNamespace(this, new Path(null));
    
    public ScriptProgram() {
    }
    
    public void loadScript(InputStream input) {
        //TODO
        //Call parse. Use classLoader to initialize a new environment.
    }
    
    public ScriptNamespace root() {
        return rootNamespace;
    }
    
}
