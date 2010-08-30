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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.objetdirect.gwt.gen.server.gen.processors.AllProcessorsTests;
import com.objetdirect.gwt.gen.server.gen.relationProcessors.AllRelationProcessorsTests;

/**
 * @author Rapha�l Brugier <raphael dot brugier at gmail dot com>
 */

@RunWith(Suite.class)
@Suite.SuiteClasses( {
	AllProcessorsTests.class,
	AllRelationProcessorsTests.class,
	TestEntityGenerator.class,
	TestObjectRelationMaker.class,
	TestPageDescriptor.class,
	TestPrintEntity.class,
	TestPrintList.class,
	TestUMLObjectInstantiator.class
})
public class AllGenTests {


}
