package cn.liutils.ripple.impl.runtime;

import cn.liutils.ripple.NativeFunction;
import cn.liutils.ripple.ScriptNamespace;
import cn.liutils.ripple.ScriptProgram;

/**
 * Implementations of standard library.
 * @author acaly, WeAthFold
 *
 */
public final class Library {
    
    private Library() {}

    public static void openLibrary(ScriptProgram program) {
        ScriptNamespace root = program.root();
        
        root.setNativeFunction("test", new NativeFunction(new String[] {}) {
            @Override
            protected Object call(NativeFunctionFrame frame) {
                System.out.println("Test function called.");
                return 0;
            }
        });
        
        root.setNativeFunction("print", new NativeFunction(new String[] {"arg0"}) {
            @Override
            protected Object call(NativeFunctionFrame frame) {
                Object arg0 = frame.getArgument(0);
                System.out.println("Ripple print: " + arg0.toString());
                return arg0;
            }
        });
    }
}
