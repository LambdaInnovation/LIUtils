package cn.liutils.ripple.impl.compiler;

/**
 * Definition of unary operators.
 * @author acaly, WeAthFold
 *
 */
public enum BinaryOperator {
    
    UNKNOWN,
    
    ADD, SUBSTRACT, MULTIPLY, DIVIDE,
    EQUAL, NOT_EQUAL, GREATER, LESSER, GREATER_EQUAL, LESSER_EQUAL, 
    AND, OR;

    static BinaryOperator fromToken(Object /*Token*/ token) {
        return UNKNOWN;
    }
}
