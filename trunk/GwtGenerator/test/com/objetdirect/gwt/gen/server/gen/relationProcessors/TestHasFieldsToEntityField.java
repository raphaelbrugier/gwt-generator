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

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.objetdirect.gwt.gen.server.gen.seamMM.EntityField;
import com.objetdirect.seam.fieldrenderers.HasFields;
import com.objetdirect.seam.print.PrintFormDescriptor;
import com.objetdirect.seam.print.PrintInternalListDescriptor;
import com.objetdirect.seam.print.PrintListDescriptor;

/**
 * @author Rapha�l Brugier <raphael dot brugier at gmail dot com>
 */
public class TestHasFieldsToEntityField extends TestRelationProcessor{
	private static final String FIELD_TITLE = "linkField";
	private static final String FIELD_NAME = "Link Field";
	private static final String LENGTH = "20";
	private static final int LENGHT_INT_VALUE = 20;
	private static final String LABELS_VALUE = "name";

	@Mock
	PrintFormDescriptor printForm;
	
	@Mock
	PrintInternalListDescriptor printInternalList;
	
	@Mock
	PrintListDescriptor printListDescriptor;
	
	EntityField entityField;
	
	HasFieldsToEntityField processor;
	
	@Before
	public void beforeTests() {
		entityField  = new EntityField(FIELD_NAME, FIELD_TITLE, LABELS_VALUE, LENGTH);
		processor = new HasFieldsToEntityField(seamGenerator);
	}
	
	
	@Test
	public void printFormDescriptor_to_EntityField() {
		setReturnedGenObject(printForm, entityField, null);

		processor.process(objectRelation);
		verifyAll(printForm);
	}
	
	@Test
	public void printInternaList_to_EntityField() {
		setReturnedGenObject(printInternalList, entityField, null);

		processor.process(objectRelation);
		verifyAll(printInternalList);
	}
	
	@Test
	public void printListDescriptor_to_EntityField() {
		setReturnedGenObject(printListDescriptor, entityField, null);

		processor.process(objectRelation);
		verifyAll(printListDescriptor);
	}
	
	private void verifyAll(HasFields objectToVerify) {
		verify(seamGenerator).getGenObjectCounterPartOf(umlObjectOwner);
		verify(seamGenerator).getGenObjectCounterPartOf(umlObjectTarget);
		verify(objectToVerify).addEntityField(FIELD_NAME, FIELD_TITLE, LABELS_VALUE, LENGHT_INT_VALUE);
	}
}