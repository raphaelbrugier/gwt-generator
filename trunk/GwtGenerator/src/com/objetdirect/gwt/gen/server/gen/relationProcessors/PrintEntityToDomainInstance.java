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
package com.objetdirect.gwt.gen.server.gen.relationProcessors;

import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.gwt.gen.server.gen.SeamGenerator;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.ObjectRelation;
import com.objetdirect.seam.print.PrintEntityDescriptor;

/**
 * @author Rapha�l Brugier <raphael dot brugier at gmail dot com>
 */
public class PrintEntityToDomainInstance extends RelationProcessor<PrintEntityDescriptor,EntityDescriptor> {

	/**
	 * @param seamGenerator
	 */
	public PrintEntityToDomainInstance(SeamGenerator seamGenerator) {
		super(seamGenerator);
	}

	@Override
	public void process(ObjectRelation objectRelation) {
		if (isEntityRelation(objectRelation)) {
			setEntity(objectRelation);
		}
	}

	private boolean isEntityRelation(ObjectRelation objectRelation) {
		return objectRelation.getRightRole().equals("entity");
	}

	private void setEntity(ObjectRelation objectRelation) {
		PrintEntityDescriptor printEntityDescriptor  = getOwner(objectRelation);
		EntityDescriptor entity = getTarget(objectRelation);

		printEntityDescriptor.setEntity(entity);
	}
}