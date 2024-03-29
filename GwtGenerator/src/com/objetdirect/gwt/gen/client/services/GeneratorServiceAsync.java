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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.gen.shared.dto.ObjectDiagramDto;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.UMLRelation;

/**
 * Asynchronous counterpart of the GeneratorService
 * 
 * @see com.objetdirect.gwt.gen.client.services.GeneratorService
 * 
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public interface GeneratorServiceAsync {

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.GeneratorService#generateClassesCode(java.util.List, java.util.List, java.lang.String)
	 */
	public void generateHibernateCode(List<UMLClass> classes, List<UMLRelation> relations,
			String packageName, AsyncCallback<List<GeneratedCode>> callback);

	
	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.GeneratorService#generateSeamCode(com.objetdirect.gwt.gen.shared.dto.ObjectDiagramDto)
	 */
	public void generateSeamCode(ObjectDiagramDto objectDiagram, AsyncCallback<List<GeneratedCode>> callback);
}
