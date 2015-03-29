package cn.liutils.ripple;

import cn.liutils.ripple.RippleException.ScriptRuntimeException;

/**
 * Implementation of basic calculations
 * @author acaly, WeAthFold
 *
 */
public final class Calculation {
    
    //IMPORTANT function names in this class is used as hard-coded string in CodeGenerator.
    
    private Calculation() {}
    
    public static Object castObject(int value) {
        return value;
    }
    
    public static Object castObject(double value) {
        return value;
    }
    
    public static Object castObject(boolean value) {
        return value;
    }
    
    //TODO casts check first
    public static int castInt(Object value) {
        if (value instanceof Integer)
            return (Integer) value;
        throw new ScriptRuntimeException("Cannot cast to integer");
    }
    
    public static double castDouble(Object value) {
        if (value instanceof Double)
            return (Double) value;
        throw new ScriptRuntimeException("Cannot cast to double");
    }
    
    public static boolean castBoolean(Object value) {
        if (value instanceof Boolean)
            return (Boolean) value;
        throw new ScriptRuntimeException("Cannot cast to boolean");
    }
    
    public static boolean checkType(Object value) {
        if (value == null) return false;
        return value instanceof Integer || 
                value instanceof Double || value instanceof Boolean;
    }
    
    public static IFunction getFunctionOverload(ScriptProgram program, String path, int nargs) {
        return program.root.getFunction(new Path(path)).cacheOverload(nargs);
    }
    
    public static void throwRuntimeException(String message) {
        
    }
    
    public static Object binAdd(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return (Integer) a + (Integer) b;
            } else if (b instanceof Integer) {
                return ((Integer) a).doubleValue() + ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a + (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a + ((Integer) b).doubleValue();
            }
        }
        throw new ScriptRuntimeException("Unsupported operand type in binary add");
    }
    
    public static Object binMultiply(Object a, Object b) {
        if (a instanceof Integer) {
            if (b instanceof Integer) {
                return (Integer) a * (Integer) b;
            } else if (b instanceof Integer) {
                return ((Integer) a).doubleValue() * ((Double) b);
            }
        } else if (a instanceof Double) {
            if (b instanceof Double) {
                return (Double) a * (Double) b;
            } else if (b instanceof Integer) {
                return (Double) a * ((Integer) b).doubleValue();
            }
        }
        throw new ScriptRuntimeException("Unsupported operand type in binary multiply");
    }
}
