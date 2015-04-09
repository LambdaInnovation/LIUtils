/**
 * 
 */
package cn.liutils.cgui.loader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a IProperty's field with @Editable annotation, and those field marked will be visible in CGUI's loading screen.
 * Currently supported types: <br/>
 * <code>
 * integer (input box) <br/>
 * double (input box) <br/>
 * string (input box) <br/>
 * boolean (check box) <br/>
 * enumeration (selection list) <br/>
 * </code>
 * @author WeAthFolD
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Editable {
	/**
	 * Represents key name displayed in the property page. You can put multiple props with same ID to merge them
	 * into the same place.
	 */
	String value();
	
	String defStr() default "";
	
	int defInt() default 0;
	
	double defDouble() default 0.0;
	
	boolean defBoolean() default false;
	
}
