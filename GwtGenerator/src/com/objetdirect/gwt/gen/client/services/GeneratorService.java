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
package com.objetdirect.gwt.gen.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.umlapi.client.UMLComponentException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLRelation;

/**
 * Services for generate the code from the UMLComponents.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@RemoteServiceRelativePath("generator")
public interface GeneratorService extends RemoteService {
	/**
	 * Generate the code from the given classes and relations.
	 * @param classes the classes as UMLComponent
	 * @param relations the relations between the classes
	 * @param packageName the name of the package
	 * @return A list of GeneratedCode object which contains the generated lines of code and the classes name.
	 * @throws UMLComponentException 
	 */
	public List<GeneratedCode> generateClassesCode(List<UMLClass> classes, List<UMLRelation> relations, String packageName) throws UMLComponentException;
}