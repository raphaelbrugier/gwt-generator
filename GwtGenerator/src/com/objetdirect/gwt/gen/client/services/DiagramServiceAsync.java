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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;


/**
 * Async counter part of DiagramService
 * @see com.objetdirect.gwt.gen.client.services.DiagramService
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public interface DiagramServiceAsync {

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#createDiagram(com.objetdirect.gwt.gen.shared.dto.DiagramDto)
	 */
	void createDiagram(DiagramDto diagramDto, AsyncCallback<String> callback);

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#deleteDiagram(java.lang.String)
	 */
	void deleteDiagram(String key, AsyncCallback<Void> callback);

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#getDiagram(java.lang.Long)
	 */
	void getDiagram(String key, AsyncCallback<DiagramDto> callback);

	/* (non-Javadoc)
	 * @see com.objetdirect.gwt.gen.client.services.DiagramService#saveDiagram(com.objetdirect.gwt.gen.shared.dto.DiagramInformations)
	 */
	void saveDiagram(DiagramDto diagram, AsyncCallback<Void> callback);
}
