/**
 * Copyright (C) Lambda-Innovation, 2013-2014
 * This code is open-source. Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 */
package cn.liutils.core.debug;

import java.lang.reflect.Field;

/**
 * @author WeAthFolD
 *
 */
public abstract class FieldModifier {
	
	public Field theField;
	public double scale = .02;
	
	public FieldModifier(Field field) {
		theField = field;
	}
	
	/**
	 * 获取需要几个维度的修改。最大支持3.
	 */
	public abstract int getRequiredDimensions();
	
	public abstract void applyModification(Object instance, boolean dirFlag, int pressTick, int dimension) throws Exception;
	
	public abstract void directSet(Object instance, String value);
	
	public abstract String getName();
	
	protected double getDeltaDoubleLinear(boolean flag, int tick) {
		return (flag ? 1 : -1) * (1D + tick / 20D) * scale;
	}
	
	public String toInfo(Object instance) {
		try {
			return String.valueOf(theField.get(instance));
		} catch (Exception e) {
			
		}
		return null;
	}
	
	public FieldModifier setScale(double f) {
		scale = f;
		return this;
	}
	
}
