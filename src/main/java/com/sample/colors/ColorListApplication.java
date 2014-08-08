package com.sample.colors;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class ColorListApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(ColorList.class);
		return classes;
	}

}
