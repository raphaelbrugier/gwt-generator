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
package com.objetdirect.gwt.gen.server.gen.processors;

import static junit.framework.Assert.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;
import com.objetdirect.seam.print.PrintInternalListDescriptor;

/**
 * @author Rapha�l Brugier <raphael dot brugier at gmail dot com>
 */
public class TestPrintInternalListDescriptor extends TestProcessor {
	
	Processor processor;
	
	@Before
	public void setUpObjectUnderTest() {
		processor = new PrintInternalListDescriptorProcessor(seamGenerator);
	}
	
	
	@Test
	public void process_success() {
		
		UMLObject object = new UMLObject().
			addAttributeValuePair("relationshipName", "employees");
		
		processor.process(object);
		
		verify(seamGenerator).addBridgeObject(eq(object), isA(PrintInternalListDescriptor.class));
	}
	
	@Test
	public void process_withNullParametersInObject_returnException() {
		UMLObject object = new UMLObject();
		
		try {
			processor.process(object);
			fail("The process method was expected to throw a GwtGeneratorException");
		} catch(GWTGeneratorException e) {
			
		}
	}
}