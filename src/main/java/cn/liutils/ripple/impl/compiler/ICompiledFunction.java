package cn.liutils.ripple.impl.compiler;

import cn.liutils.ripple.Path;
import cn.liutils.ripple.impl.runtime.IFunction;

public interface ICompiledFunction extends IFunction {
    
    void bind(Path path);
    
}
