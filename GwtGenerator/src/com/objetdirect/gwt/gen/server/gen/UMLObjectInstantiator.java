/*
 * This file is part of the Gwt-Generator project and was written by Rapha�l Brugier <raphael dot brugier at gmail dot com > for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright � 2010 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.gen.server.gen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.objetdirect.gwt.gen.shared.exceptions.DiagramGenerationException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObjectAttribute;

/**
 * This class is responsible to instantiate generator objects from UmlObject using reflection.
 * 
 * @author Rapha�l Brugier <raphael dot brugier at gmail dot com>
 */
public class UMLObjectInstantiator {

	private static final String PACKAGE_NAME_PREFIX = "com.objetdirect.";
	
	
	private static final String[] PACKAGES = {"entities.", "seam.fields.", "seam.print.", "seam."}; 

	public Object instantiate(UMLObject umlObject) {
		Class<?> classToInstantiate = getJavaClassFromUmlObject(umlObject);

		String exceptionMessage = "Unable to instantiate the class " + umlObject.getClassName();
		try {
			
			if (umlObject.getObjectAttributes().size() == 0) {
				exceptionMessage = exceptionMessage + " with the default constructor";
				return classToInstantiate.newInstance();
			} else {
				exceptionMessage = exceptionMessage + " with a constructor with the parameters " + umlObject.getObjectAttributes();
				return instantiateWithParameters(umlObject.getObjectAttributes(), classToInstantiate);
			}
			
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new DiagramGenerationException(exceptionMessage);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new DiagramGenerationException(exceptionMessage + " for security reason.");
		}
	}

	/**
	 * @param umlAttributes
	 * @param classToInstantiate
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@SuppressWarnings("unchecked")
	private Object instantiateWithParameters(List<UMLObjectAttribute> umlAttributes, Class<?> classToInstantiate) throws InstantiationException, IllegalAccessException {
		int numberOfAttributes = umlAttributes.size();
		Class<String>[] paramTypes = new Class[numberOfAttributes];
		Object[] attributesValues = new Object[numberOfAttributes];
		for (int i =0; i<numberOfAttributes; i++) {
			paramTypes[i] = String.class;
			attributesValues[i] = umlAttributes.get(i).getValue();
		}
		
		String className = classToInstantiate.getSimpleName();
		Constructor<?> constructor;
		try {
			constructor = classToInstantiate.getConstructor(paramTypes);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new DiagramGenerationException("Unable to instantiate the class " + className + " due to security restrictions");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new DiagramGenerationException(
				"Unable to instantiate the class " + className + " because there is no constructor for the given parameters");
		}
		
		Object object = null;
		try {
			object = constructor.newInstance(attributesValues);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new DiagramGenerationException(
					"Unable to instantiate the class " + className + " because wrong parameters has been passed to the constructor " + constructor);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new DiagramGenerationException(
					"Unable to instantiate the class " + className + " because wrong parameters has been passed to the constructor " + constructor);
		}
		return object;
	}
	
	/**
	 * @param className
	 * @return
	 */
	public static Class<?> getJavaClassFromUmlObject(UMLObject umlObject) {
		String className = umlObject.getClassName();
		Class<?> classToInstantiate = null;
		for (String packageName : PACKAGES) {
			try {
				String fullClassName = PACKAGE_NAME_PREFIX + packageName + className;
				classToInstantiate = Class.forName(fullClassName);
				break;
			} catch (ClassNotFoundException e) {
			}
		}
		if (classToInstantiate == null) {
			throw new DiagramGenerationException("Unable to find the class " + className + ".");
		}
		return classToInstantiate;
	}
}
