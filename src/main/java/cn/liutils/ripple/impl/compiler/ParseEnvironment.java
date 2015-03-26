package cn.liutils.ripple.impl.compiler;

import cn.liutils.ripple.Path;

/**
 * The code generator for a single function. Used by parser.
 * @author acaly, WeAthFold
 *
 */
public final class ParseEnvironment {
    
    ParseEnvironment(FunctionClassLoader classLoader, Path functionPath) {
        
    }

    void addParameter(String name) {
        
    }

    //STACK +1
    void pushIntegerConst(int value) {
        
    }

    //STACK +1
    void pushDoubleConst(double value) {
        
    }

    //STACK +1
    void pushBooleanConst(boolean value) {
        
    }

    //STACK +1
    void pushParameter(String name) {
        
    }

    //STACK -2+1
    void calcBinary(BinaryOperator op) {
        
    }

    //STACK -1+1
    void calcUnary(UnaryOperator op) {
        
    }
    
    //STACK -nargs+1
    void callFunction(String path, int nargs) {
        
    }

    //STACK -1
    void pushSwitchBlock() {
        
    }
    
    //STACK +1
    void popSwitchBlock() {
        
    }
    
    //STACK -2
    void switchCase() {
        
    }
    
    //STACK -1
    void switchDefault() {
        
    }

    //STACK -1. CHECK 0.
    void returnValue() {
        
    }
}
