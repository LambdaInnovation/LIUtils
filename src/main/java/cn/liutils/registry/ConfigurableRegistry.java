package cn.liutils.registry;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.common.config.Configuration;
import cn.annoreg.core.AnnotationData;
import cn.annoreg.core.RegModInformation;
import cn.annoreg.core.RegistryType;
import cn.annoreg.core.RegistryTypeDecl;
import cn.liutils.api.LIGeneralRegistry;
import cn.liutils.core.LIUtils;

@RegistryTypeDecl
public class ConfigurableRegistry extends RegistryType {

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface RegConfigurable {
	}

	public ConfigurableRegistry() {
		super(RegConfigurable.class, LIUtils.REGISTER_TYPE_CONFIGURABLE);
	}

	@Override
	public boolean registerClass(AnnotationData data) {
		Class<?> clazz = data.getTheClass();
		LIGeneralRegistry.loadConfigurableClass(getConfig(data.mod), clazz);
		return true;
	}

	@Override
	public boolean registerField(AnnotationData data) {
		return false;
	}
	
	private Map<RegModInformation, Configuration> configs = new HashMap();

	private Configuration getConfig(RegModInformation mod) {
		//How to find the Configuration:
		//First iterate all fields in mod class.
		//If there is a field with the type Configuration, return the value of this field.
		//When the field is null, throw an exception.
		//When the field is not found, also throw an exception.
		if (configs.containsKey(mod)) {
			return configs.get(mod);
		}
		Class<?> clazz = mod.getModInstance().getClass();
		for (Field f : clazz.getFields()) {
			if (f.getType() == Configuration.class) {
				Configuration config = null;
				try {
					if (Modifier.isStatic(f.getModifiers())) {
						config = (Configuration) f.get(null);
					} else {
						config = (Configuration) f.get(mod.getModInstance());
					}
				} catch (Exception e) {}
				if (config == null) {
					break;
				} else {
					configs.put(mod, config);
					return config;
				}
			}
		}
		throw new RuntimeException("Can not get config instance for mod " + mod.getModID() + ".");
	}
}
