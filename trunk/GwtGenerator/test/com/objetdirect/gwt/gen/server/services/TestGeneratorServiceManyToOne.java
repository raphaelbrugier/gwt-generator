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

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import com.objetdirect.gwt.gen.TestUtil;
import com.objetdirect.gwt.gen.shared.dto.GeneratedCode;
import com.objetdirect.gwt.umlapi.client.UMLException;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLClass;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLRelation;
import com.objetdirect.gwt.umlapi.client.umlcomponents.UMLLink.LinkKind;
import com.objetdirect.gwt.umlapi.client.umlcomponents.umlrelation.LinkAdornment;

/**
 * Test the generation of the many to one relations
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class TestGeneratorServiceManyToOne extends TestCase {

	private GeneratorServiceImpl service = new GeneratorServiceImpl();
	
	public void testUnidirectional() {
		UMLClass flightEntity = new UMLClass("Flight");
		UMLClass companyEntity = new UMLClass("Company");
		
		UMLRelation relation = new UMLRelation(LinkKind.ASSOCIATION_RELATION);
		relation.setName("");
		
		relation.setLeftAdornment(LinkAdornment.NONE);
		relation.setLeftCardinality("*");
		relation.setLeftConstraint("");
		relation.setLeftRole("");
		relation.setLeftStereotype("");
		relation.setLeftTarget(flightEntity);
		
		relation.setRightAdornment(LinkAdornment.WIRE_ARROW);
		relation.setRightCardinality("1");
		relation.setRightConstraint("");
		relation.setRightRole("company");
		relation.setRightStereotype("");
		relation.setRightTarget(companyEntity);

		List<UMLClass> classes = new LinkedList<UMLClass>();
		List<UMLRelation> relations = new LinkedList<UMLRelation>();
		
		classes.add(flightEntity);
		classes.add(companyEntity);
		relations.add(relation);
		
		List<GeneratedCode> generatedClassesCode = null;
		try {
			generatedClassesCode = service.generateClassesCode(classes, relations, TestUtil.packageName);
		} catch (UMLException e) {
			fail();
		}
		
		TestUtil.assertExist(flightEntity.getName(), generatedClassesCode,
			"import javax.persistence.ManyToOne;");
		
		TestUtil.assertExist(flightEntity.getName(), generatedClassesCode,
			"@ManyToOne",
			"Company company;");
		
		TestUtil.assertExist(flightEntity.getName(), generatedClassesCode,
			"public Flight(boolean dummy) {",
			"	this.company = null;");
		
		TestUtil.assertExist(flightEntity.getName(), generatedClassesCode,
			"public static Flight createFlight() {",
			"	Flight flight = new Flight();",
			"	return flight;");
	}
	
	public void testBidirectional() {
		UMLClass soldierEntity = new UMLClass("Soldier");
		UMLClass troopEntity = new UMLClass("Troop");
		
		UMLRelation relation = new UMLRelation(LinkKind.ASSOCIATION_RELATION);
		relation.setName("");
		
		relation.setLeftAdornment(LinkAdornment.NONE);
		relation.setLeftCardinality("*");
		relation.setLeftConstraint("");
		relation.setLeftRole("soldiers");
		relation.setLeftStereotype("<<owner>>");
		relation.setLeftTarget(soldierEntity);
		
		relation.setRightAdornment(LinkAdornment.NONE);
		relation.setRightCardinality("1");
		relation.setRightConstraint("");
		relation.setRightRole("troop");
		relation.setRightStereotype("");
		relation.setRightTarget(troopEntity);

		List<UMLClass> classes = new LinkedList<UMLClass>();
		List<UMLRelation> relations = new LinkedList<UMLRelation>();
		
		classes.add(troopEntity);
		classes.add(soldierEntity);
		relations.add(relation);
		
		List<GeneratedCode> generatedClassesCode = null;
		try {
			generatedClassesCode = service.generateClassesCode(classes, relations, TestUtil.packageName);
		} catch (UMLException e) {
			fail();
		}
		
		TestUtil.assertExist(soldierEntity.getName(), generatedClassesCode,
			"import javax.persistence.ManyToOne;");

		TestUtil.assertExist(soldierEntity.getName(), generatedClassesCode,
			"@ManyToOne",
			"Troop troop;",
			"@Transient",
	    	"boolean inDeletion = false;");
		
		
		TestUtil.assertExist(troopEntity.getName(), generatedClassesCode,
			"import javax.persistence.OneToMany;");
		
		TestUtil.assertExist(troopEntity.getName(), generatedClassesCode,
			"@OneToMany(mappedBy=\"troop\")",
			"List<Soldier> soldiers;",
			"@Transient",
			"boolean inDeletion = false;");

		TestUtil.assertExist(troopEntity.getName(), generatedClassesCode,
			"public Troop(boolean dummy) {",
	        "	this.soldiers = new ArrayList<Soldier>();");
		
		TestUtil.assertExist(troopEntity.getName(), generatedClassesCode,
			"public static Troop createTroop() {",
	        "	Troop troop = new Troop();",
	        "	troop.soldiers = new ArrayList<Soldier>();",
	        "	return troop;");
		
		TestUtil.assertExist(troopEntity.getName(), generatedClassesCode,
			"public List<Soldier> getSoldiers() {",
        	"	return Collections.unmodifiableList(soldiers);");
	}
}
