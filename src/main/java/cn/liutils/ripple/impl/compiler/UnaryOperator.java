package cn.liutils.ripple.impl.compiler;

/**
 * Definition of unary operators.
 * @author acaly, WeAthFold
 *
 */
public enum UnaryOperator {
    
    UNKNOWN(null),
    
    MINUS("unMinus"),
    NOT("unNot"),

    U_EQUAL("unEqual"),
    U_NOT_EQUAL("unNotEqual"),
    U_GREATER("unGreater"),
    U_LESSER("unLesser"),
    U_GREATER_EQUAL("unGreaterEqual"),
    U_LESSER_EQUAL("unLesserEqual");
    
    public final String methodName;
    
    private UnaryOperator(String methodName) {
        this.methodName = methodName;
    }
    
    static UnaryOperator fromToken(Object /*Token*/ token) {
        return UNKNOWN;
    }
}
