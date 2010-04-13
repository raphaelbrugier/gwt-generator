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
package com.objetdirect.gwt.gen.server.services;

import java.util.ArrayList;
import java.util.Map;

import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.entities.ManyToManyReferenceListDescriptor;
import com.objetdirect.entities.ManyToOneReferenceDescriptor;
import com.objetdirect.entities.OneToManyReferenceListDescriptor;
import com.objetdirect.entities.OneToOneReferenceDescriptor;
import com.objetdirect.gwt.gen.shared.GWTGeneratorException;
import com.objetdirect.gwt.umlapi.client.UMLComponentException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClassAttribute;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLType;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLVisibility;

/**
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class GeneratorHelper {

	public static EntityDescriptor convertUMLClassToEntityDescriptor(UMLClass umlClass, String packageName) throws UMLComponentException {
		EntityDescriptor entity = new EntityDescriptor(packageName, umlClass.getName());
		
		ArrayList<UMLClassAttribute> attributes = umlClass.getAttributes();
		
		for(UMLClassAttribute attribute : attributes) {
			addAttribute(entity, attribute);
		}
		
		return entity;
	}
	
	public static void createOneToOneRelation(Map<UMLClass, EntityDescriptor> entities, UMLRelation relation) throws UMLComponentException {
		EntityDescriptor leftEntity = entities.get(relation.getLeftTarget());
		EntityDescriptor rightEntity = entities.get(relation.getRightTarget());
		
		if (relation.isBidirectional()) {
//			System.out.println("leftEntity name :" + leftEntity.getName());
//			System.out.println("rightEntity name :" + rightEntity.getName());
//			System.out.println("\nLeftRole = " + relation.getLeftRole());
//			System.out.println("RightRole = " + relation.getRightRole());
//			System.out.println("\nIsRightOwner = " + relation.isRightOwner());
//			System.out.println("\nIsLeftOwner = " + relation.isLeftOwner());
			
			OneToOneReferenceDescriptor refLeftToRight = 
				new OneToOneReferenceDescriptor(leftEntity, rightEntity, relation.getRightRole(), false, false, false);
			OneToOneReferenceDescriptor refRightToLeft = 
				new OneToOneReferenceDescriptor(rightEntity, leftEntity, relation.getLeftRole(), false, false, false);
			
			refLeftToRight.setReverse(refRightToLeft, relation.isLeftOwner());
			refRightToLeft.setReverse(refLeftToRight, relation.isRightOwner());
		} else {
			if (relation.isLeftOwner()) {
				OneToOneReferenceDescriptor refLeftToRight = 
					new OneToOneReferenceDescriptor(leftEntity, rightEntity, relation.getRightRole(), false, relation.isAComposition(), false);
			} else
			{
				OneToOneReferenceDescriptor refRightToLeft = 
					new OneToOneReferenceDescriptor(rightEntity, leftEntity, relation.getLeftRole(), false, relation.isAComposition(), false);
			}
		}
	}
	
	
	public static void createOneToManyRelation(Map<UMLClass, EntityDescriptor> entities, UMLRelation relation) throws UMLComponentException {
		EntityDescriptor leftEntity = entities.get(relation.getLeftTarget());
		EntityDescriptor rightEntity = entities.get(relation.getRightTarget());
		
		if (relation.isBidirectional()) {
			throw new UMLComponentException("Bidirectional one-to-many relation is not supported yet.");
		} 
		// Unidirectional
		else {
			if (relation.isLeftOwner()) {
				OneToManyReferenceListDescriptor ref = 
					new OneToManyReferenceListDescriptor(leftEntity, rightEntity, relation.getRightRole(), false);
			} else {
				OneToManyReferenceListDescriptor ref = 
					new OneToManyReferenceListDescriptor(rightEntity, leftEntity, relation.getLeftRole(), false);
			}
		}
	}


	public static void createManyToOneRelation(Map<UMLClass, EntityDescriptor> entities, UMLRelation relation) throws UMLComponentException {
		EntityDescriptor leftEntity = entities.get(relation.getLeftTarget());
		EntityDescriptor rightEntity = entities.get(relation.getRightTarget());
		
		if (relation.isBidirectional()) {
			if (relation.isLeftOwner()) {
				ManyToOneReferenceDescriptor leftToRight = 
					new ManyToOneReferenceDescriptor(leftEntity, rightEntity, relation.getRightRole(), false, false);
				OneToManyReferenceListDescriptor rightToLeft = 
					new OneToManyReferenceListDescriptor(rightEntity, leftEntity, relation.getLeftRole(), false);
				
				leftToRight.setReverse(rightToLeft, true);
				rightToLeft.setReverse(leftToRight, false);
			} 
			else {
				OneToManyReferenceListDescriptor leftToRight = 
					new OneToManyReferenceListDescriptor(leftEntity, rightEntity, relation.getRightRole(), false);
				
				ManyToOneReferenceDescriptor rightToLeft = 
					new ManyToOneReferenceDescriptor(rightEntity, leftEntity, relation.getLeftRole(), false, false);
				
				leftToRight.setReverse(rightToLeft, false);
				rightToLeft.setReverse(leftToRight, true);
			}
		} 
		// Unidirectional
		else {
			if (relation.isLeftOwner()) {
				ManyToOneReferenceDescriptor ref = 
					new ManyToOneReferenceDescriptor(leftEntity, rightEntity, relation.getRightRole(), false, false);
			} else {
				ManyToOneReferenceDescriptor ref = 
					new ManyToOneReferenceDescriptor(rightEntity, leftEntity, relation.getLeftRole(), false, false);
			}
		}
	}


	public static void createManyToManyRelation(Map<UMLClass, EntityDescriptor> entities, UMLRelation relation) throws UMLComponentException {
		EntityDescriptor leftEntity = entities.get(relation.getLeftTarget());
		EntityDescriptor rightEntity = entities.get(relation.getRightTarget());
		
		if (relation.isBidirectional()) {
			ManyToManyReferenceListDescriptor leftToRight = 
				new ManyToManyReferenceListDescriptor(leftEntity, rightEntity, relation.getRightRole(), false);
			ManyToManyReferenceListDescriptor rightToLeft = 
				new ManyToManyReferenceListDescriptor(rightEntity, leftEntity, relation.getLeftRole(), false);
			leftToRight.setReverse(rightToLeft, relation.isLeftOwner());
			rightToLeft.setReverse(leftToRight, relation.isRightOwner());
		}
		// Unidirectional
		else {
			if (relation.isLeftOwner()) {
				ManyToManyReferenceListDescriptor ref = 
					new ManyToManyReferenceListDescriptor(leftEntity, rightEntity, relation.getRightRole(), false);
			} else {
				ManyToManyReferenceListDescriptor ref = 
					new ManyToManyReferenceListDescriptor(rightEntity, leftEntity, relation.getLeftRole(), false);
			}
		}
	}
	
	
	/**
	 * Add an attribute to the entity depending of the type of the attribute
	 * @param entity the target entity
	 * @param attribute	the attribute to add to the entity
	 * @throws UMLComponentException 
	 */
	private static void addAttribute(EntityDescriptor entity, UMLClassAttribute attribute) throws UMLComponentException {
		String name = attribute.getName();
		String type = attribute.getType();
		UMLVisibility visibility = attribute.getVisibility();
		
		switch (visibility) {
		case PRIVATE:
			break;
		default:
			throw new UMLComponentException("Pojo generator only supports private field");
		}
		
		switch (UMLType.getUMLTypeFromString(type)) {
		case STRING :
			entity.addStringField(name, null);
			break;
		case INT :
			entity.addIntField(name, null);
			break;
			
		case INTEGER :
			entity.addWrapperIntField(name, null);
			break;
			
		case LONG : 
			entity.addLongField(name, null);
			break;
			
		case WRAPPED_LONG : 
			entity.addWrapperLongField(name, null);
			break;
			
		case BYTE : 
			entity.addByteField(name, null);
			break;
			
		case WRAPPED_BYTE : 
			entity.addWrapperByteField(name, null);
			break;
			
		case SHORT  : 
			entity.addShortField(name, null);
			break;
			
		case WRAPPED_SHORT :
			entity.addWrapperShortField(name, null);
			break;
			
		case BOOLEAN :
			entity.addBooleanField(name, null);
			break;
			
		case WRAPPED_BOOLEAN :
			entity.addWrapperBooleanField(name, null);
			break;
			
		case CHAR :
			entity.addCharField(name, null);
			break;
			
		case CHARACTER :
			entity.addWrapperCharField(name, null);
			break;
			
		case FLOAT  :
			entity.addFloatField(name, null);
			break;
			
		case WRAPPED_FLOAT :
			entity.addWrapperFloatField(name, null);
			break;
			
		case DOUBLE :
			entity.addDoubleField(name, null);
			break;
			
		case WRAPPED_DOUBLE  :
			entity.addWrapperDoubleField(name, null);
			break;

		default:
			break;
		}
	}
}
