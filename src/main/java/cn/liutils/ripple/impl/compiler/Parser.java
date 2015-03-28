package cn.liutils.ripple.impl.compiler;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import cn.liutils.ripple.IFunction;
import cn.liutils.ripple.Path;
import cn.liutils.ripple.ScriptProgram;

public class Parser {
    public static class ScriptObject {
        public Object value;
        public String path;
        public IFunction func;
        public int funcArgNum;
    }
    
    public ScriptProgram program;
    
    public static List<ScriptObject> parse(ScriptProgram program, InputStream input, FunctionClassLoader loader) {
        Parser p = new Parser();
        p.program = program;
        
        ScriptObject o = new ScriptObject();
        CodeGenerator gen = new CodeGenerator(p, loader, new Path("main"));
        o.path = "main";
        o.func = gen.testCompile();
        o.funcArgNum = 1;
        return Arrays.asList(new ScriptObject[] { o });
    }
}
