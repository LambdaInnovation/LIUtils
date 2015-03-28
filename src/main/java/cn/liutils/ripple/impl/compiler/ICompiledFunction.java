package cn.liutils.ripple.impl.compiler;

import cn.liutils.ripple.IFunction;
import cn.liutils.ripple.Path;

public interface ICompiledFunction extends IFunction {
    
    void bind(Path path);
    
}
