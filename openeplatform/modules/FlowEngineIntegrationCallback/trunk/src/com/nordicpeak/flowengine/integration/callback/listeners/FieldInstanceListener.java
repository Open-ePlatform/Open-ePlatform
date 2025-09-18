package com.nordicpeak.flowengine.integration.callback.listeners;

import java.lang.reflect.Field;
import java.util.concurrent.locks.Lock;

import se.unlogic.hierarchy.core.interfaces.instances.InstanceListener;
import se.unlogic.standardutils.reflection.ReflectionUtils;


public class FieldInstanceListener<T> implements InstanceListener<T> {

	private final Object target;
	private final Field field;

	public FieldInstanceListener(Object target, Field field, boolean required, Lock writeLock) {

		this.target = target;
		this.field = field;
		
		ReflectionUtils.fixFieldAccess(field);
	}
	
	@Override
	public <InstanceType extends T> void instanceAdded(Class<T> key, InstanceType instance) {

		try {
			field.set(target, instance);
			
		} catch (Exception e){
		
			if(e instanceof RuntimeException){
				
				throw (RuntimeException)e;
			}
			
			throw new RuntimeException(e);
		}	

	}

	@Override
	public <InstanceType extends T> void instanceRemoved(Class<T> key, InstanceType instance) {

		try {
			field.set(target, null);
			
		} catch (Exception e){
		
			if(e instanceof RuntimeException){
				
				throw (RuntimeException)e;
			}
			
			throw new RuntimeException(e);
		}	
	}
}
