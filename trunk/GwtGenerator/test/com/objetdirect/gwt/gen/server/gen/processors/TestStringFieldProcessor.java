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

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import com.objetdirect.gwt.gen.server.gen.seamMM.StringField;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLObject;

/**
 * @author Rapha�l Brugier <raphael dot brugier at gmail dot com>
 */
public class TestStringFieldProcessor extends TestProcessor {
	private static final String LENGTH_VALUE = "20";
	private static final String FIELD_TITLE_VALUE = "Name";
	private static final String FIELD_NAME_VALUE = "name";

	@Test
	public void process() {
		StringFieldProcessor pdp = new StringFieldProcessor(seamGenerator);
		
		UMLObject objectArgument = new UMLObject("", "StringField").
			addAttributeValuePair("fieldName", FIELD_NAME_VALUE).
			addAttributeValuePair("fieldTitle", FIELD_TITLE_VALUE).
			addAttributeValuePair("length", LENGTH_VALUE);
		
		StringField sfExepected = new StringField(FIELD_NAME_VALUE, FIELD_TITLE_VALUE, LENGTH_VALUE);
		
		pdp.process(objectArgument);
		
		verify(seamGenerator).addBridgeObject(eq(objectArgument), eq(sfExepected));
	}
}