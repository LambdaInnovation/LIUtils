package cn.liutils.ripple;

import java.util.HashMap;

import cn.liutils.ripple.RippleException.ScriptRuntimeException;

/**
 * A wrapper for Java function injected into a Ripple program.
 * @author acaly
 *
 */
public abstract class NativeFunction implements IFunction {
    
    class NativeFunctionFrame {
        
        private Object[] args;
        
        NativeFunctionFrame(Object[] args) {
            this.args = args;
        }
        
        public Object getArgument(String name) {
            Integer id = parameterMap.get(name);
            if (id == null) {
                throw new ScriptRuntimeException("Parameter not found");
            }
            return args[id];
        }
        
        public Object getArgument(int id) {
            if (id < 0 || id >= args.length) {
                throw new ScriptRuntimeException("Invalid parameter index");
            }
            return args[id];
        }
        
        //get value or get function can also be added here if really needed
    }
    
    private ScriptProgram env;
    private Path functionPath;
    private final HashMap<String, Integer> parameterMap;

    protected NativeFunction(String[] parameters) {
        this.parameterMap = new HashMap();
        for (int i = 0; i < parameters.length; ++i) {
            parameterMap.put(parameters[i], i);
        }
    }
    
    @Override
    public Object call(Object[] args) {
        if (args.length != parameterMap.size()) {
            throw new ScriptRuntimeException("Invalid argument count for function " + functionPath.path + ". " + 
                    parameterMap.size() + " expected, " + args.length + " received.");
        }
        for (Object arg : args) {
            if (!Calculation.checkType(arg)) {
                throw new ScriptRuntimeException("Invalid argument type for function " + functionPath.path + ".");
            }
        }
        
        NativeFunctionFrame frame = new NativeFunctionFrame(args);
        Object ret = call(frame);
        
        if (!Calculation.checkType(ret)) {
            throw new ScriptRuntimeException("Invalid return value");
        }
        return ret;
    }
    
    void bind(ScriptProgram env, Path path) {
        if (this.env != null) {
            throw new ScriptRuntimeException("Try to rebind a native function");
        }
        this.env = env;
        this.functionPath = path;
    }
    
    int getParamterCount() {
        return parameterMap.size();
    }
    
    protected abstract Object call(NativeFunctionFrame frame);
}
