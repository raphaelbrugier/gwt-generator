/*
 * This file is part of the Gwt-Generator project and was written by Henri Darmet for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct
 * 
 * Gwt-Generator is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * Gwt-Generator is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Gwt-Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package com.objetdirect.seam;

import com.objetdirect.engine.TypeDescriptor;

public class Hibernate {

	public static final TypeDescriptor Hibernate = TypeDescriptor.type("org.hibernate", "Hibernate");
	public static final TypeDescriptor Criteria = TypeDescriptor.type("org.hibernate", "Criteria");
	public static final TypeDescriptor Session = TypeDescriptor.type("org.hibernate", "Session");
	public static final TypeDescriptor Restrictions = TypeDescriptor.type("org.hibernate.criterion", "Restrictions");
	public static final TypeDescriptor NotNull = TypeDescriptor.type("org.hibernate.validator", "NotNull");
	public static final TypeDescriptor Length = TypeDescriptor.type("org.hibernate.validator", "Length");
}

