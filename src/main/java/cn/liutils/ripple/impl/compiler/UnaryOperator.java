package cn.liutils.ripple.impl.compiler;

/**
 * Definition of unary operators.
 * @author acaly, WeAthFold
 *
 */
public enum UnaryOperator {
    
    UNKNOWN,
    
    MINUS,
    NOT,

    EQUAL, NOT_EQUAL, GREATER, LESSER, GREATER_EQUAL, LESSER_EQUAL;
    
    static UnaryOperator fromToken(Object /*Token*/ token) {
        return UNKNOWN;
    }
}
