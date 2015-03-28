package cn.liutils.ripple.impl.compiler;

/**
 * Definition of unary operators.
 * @author acaly, WeAthFold
 *
 */
public enum BinaryOperator {
    
    UNKNOWN(null),
    
    ADD("binAdd"),
    SUBSTRACT("binSubstract"),
    MULTIPLY("binMultiply"),
    DIVIDE("binDivide"),
    EQUAL("binEqual"),
    NOT_EQUAL("binNotEqual"),
    GREATER("binGreater"),
    LESSER("binLesser"),
    GREATER_EQUAL("binGreaterEqual"),
    LESSER_EQUAL("binLesserEqual"), 
    AND("binAnd"),
    OR("binOr");

    public final String methodName;
    
    private BinaryOperator(String methodName) {
        this.methodName = methodName;
    }
    
    static BinaryOperator fromToken(Object /*Token*/ token) {
        return UNKNOWN;
    }
}
