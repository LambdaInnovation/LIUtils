package cn.liutils.ripple;

import java.util.ArrayList;
import java.util.Stack;

import com.google.common.collect.ImmutableList;

/**
 * Stack trace of the current thread. 
 * @author acaly
 *
 */
public final class ScriptStacktrace {
    private static final ThreadLocal<Stack<Path>> threadStacktrace = new ThreadLocal<Stack<Path>>() {
        @Override protected Stack<Path> initialValue() {
            return new Stack<Path>();
        }
    };
    
    public final ImmutableList<Path> stacktrace;
    
    private ScriptStacktrace() {
        this.stacktrace = ImmutableList.copyOf(threadStacktrace.get());
    }
    
    public static ScriptStacktrace getStacktrace() {
        return new ScriptStacktrace();
    }
    
    static int pushFrame(Path path) {
        Stack<Path> s = threadStacktrace.get();
        int ret = s.size();
        s.push(path);
        return ret;
    }
    
    static void popFrame() {
        threadStacktrace.get().pop();
    }
    
    static void adjustFrame(int count) {
        Stack<Path> s = threadStacktrace.get();
        s.setSize(count);
    }
}
