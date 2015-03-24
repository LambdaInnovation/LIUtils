package cn.liutils.ripple;

/**
 * Path for script objects (functions and values).
 * @author acaly
 *
 */
public final class Path {

    public static final String pathRegex = "\\w+(.\\w+)*";
    
    /**
     * The string value of this path.
     * Allows null. When path is null, it represents the root namespace. 
     */
    public final String path;
    
    public Path(String path) {
        this.path = path;
        if (!isValid(path))
            throw new RippleException("Invalid path");
    }
    
    public Path(Path parent, String sub) {
        if (parent.path == null) {
            this.path = sub;
        } else {
            this.path = parent.path + '.' + sub;
        }
        if (!isValid(path))
            throw new RippleException("Invalid path");
    }
    
    public Path(Path parent, Path sub) {
        if (parent.path == null) {
            this.path = sub.path;
        } else {
            this.path = parent.path + '.' + sub.path;
        }
        if (!isValid(path))
            throw new RippleException("Invalid path");
    }
    
    public static boolean isValid(String path) {
        return path == null || path.matches(pathRegex);
    }
}
