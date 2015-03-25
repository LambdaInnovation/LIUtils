package cn.liutils.ripple.impl.runtime;

/**
 * Implementation of basic calculations
 * @author acaly, WeAthFold
 *
 */
public final class Calculation {
    
    private Calculation() {}
    
    public static int castInt(Object value) {
        return (Integer) value;
    }
    
    public static double castDouble(Object value) {
        return (Double) value;
    }
    
    public static boolean castBoolean(Object value) {
        return (Boolean) value;
    }
    
    public static boolean checkType(Object value) {
        if (value == null) return false;
        return value instanceof Integer || 
                value instanceof Double || value instanceof Boolean;
    }
}
