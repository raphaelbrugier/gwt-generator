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
package com.objetdirect.gwt.gen.server.dao;

import static com.objetdirect.gwt.gen.server.ServerHelper.getCurrentUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.objetdirect.gwt.gen.server.ServerHelper;
import com.objetdirect.gwt.gen.server.entities.Diagram;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.exceptions.GWTGeneratorException;
import com.objetdirect.gwt.umlapi.client.umlCanvas.ClassDiagram;
import com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType;

/**
 * DAO for the diagram entities.
 * @author Rapha�l Brugier <raphael dot brugier at gmail dot com >
 */
public class DiagramDao {
	
	public interface Action {
		void run(PersistenceManager pm);
	}
	
	public void execute(Action a) {
		PersistenceManager pm = ServerHelper.getPM();
		try {
			a.run(pm);
		} finally  {
			pm.close();
		}
	}
	
	/**
	 * @param newDiagram
	 * @return
	 */
	public String createDiagram(Diagram newDiagram) {
		final Diagram persistedDiagram[] ={newDiagram};
		
		execute(new Action() {
			public void run(PersistenceManager pm) {
				persistedDiagram[0] = pm.makePersistent(persistedDiagram[0]);
			}
		});
		return persistedDiagram[0].getKey();
	}
	
	/**
	 * Get a diagram from its key. 
	 * 
	 * @param key
	 * @return the diagram found or null.
	 */
	public Diagram getDiagram(String key) {
		PersistenceManager pm = ServerHelper.getPM();
		Diagram diagram;
		try {
			diagram = pm.getObjectById(Diagram.class, key);
			if(diagram!= null) {
				diagram = pm.detachCopy(diagram);
			}
		} finally {
			pm.close();
		}
		
		return diagram;
	}
	
	/**
	 * Get a diagram from its name, its type and its directoryKey for the logged user.
	 * 
	 * @param type the type of the diagram
	 * @param name the name of the diagram
	 * @param directoryKey the directoryKey of the diagram
	 * @return the diagram found or null if not diagram was found
	 */
	@SuppressWarnings("unchecked")
	public DiagramDto getDiagram(DiagramType type, String name, String directoryKey) {
		PersistenceManager pm = ServerHelper.getPM();
		DiagramDto diagramFound = null;
		List<Diagram> queryResult;
		try {
			Query q = pm.newQuery(Diagram.class, "user == u && type == t && name == n && directoryKey == d");
			q.declareParameters(
					"com.google.appengine.api.users.User u, " +
					"String t, "+
					"String n, " +
					"String d");
			queryResult = (List<Diagram>) q.executeWithArray(getCurrentUser(), type, name, directoryKey);

			if (queryResult.size() == 1) {
				diagramFound = queryResult.get(0).copyToDiagramDto();
			}
		} finally {
			pm.close();
		}

		return diagramFound;
	}
	
	/**
	 * Return the diagrams of the logged user in the given directory.
	 * Please note that returned dto will NOT contained the serialized umlCanvas.
	 * Use getDiagram(Long key) to get a dto with the serialized umlCanvas field.
	 * @param directoryKey The key of the directory
	 * @return a collection of all the user's diagrams.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DiagramDto> getDiagrams(String directoryKey) {
		PersistenceManager pm = ServerHelper.getPM();
		List<Diagram> queryResult;
		ArrayList<DiagramDto> results = new ArrayList<DiagramDto>();
		try {
			Query q = pm.newQuery(Diagram.class, "directoryKey == d");
		    q.declareParameters("String d");
		    queryResult = (List<Diagram>) q.execute(directoryKey);
		      
		    for (Diagram diagram : queryResult) {
		    	DiagramDto diagramInformation = new DiagramDto(diagram.getKey(), diagram.getDirectoryKey(), diagram.getName(), diagram.getType());
		    	results.add(diagramInformation);
		    }
		} finally {
			pm.close();
		}

		return results;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Diagram> getSeamDiagrams() {
		PersistenceManager pm = ServerHelper.getPM();
		Collection<Diagram> queryResult = new ArrayList<Diagram>();
		
		try {
			Query q = pm.newQuery(Diagram.class, "isSeamDiagram == b");
		    q.declareParameters("boolean b");
		    queryResult = (List<Diagram>) q.execute(true);
		    queryResult = pm.detachCopyAll(queryResult);
		} finally {
			pm.close();
		}
		
		return queryResult;
	}
	
	/**
	 * Get all the class diagrams defining the seam diagram.
	 * 
	 * @return a list of all the diagrams.
	 */
	public List<ClassDiagram> getSeamClassDiagrams() {
		List<ClassDiagram> classDiagrams = new ArrayList<ClassDiagram>();
		Collection<Diagram> seamDiagrams = getSeamDiagrams();
		for (Diagram diagram : seamDiagrams) {
			ClassDiagram classDiagram = (ClassDiagram) diagram.getCanvas();
			classDiagrams.add(classDiagram);
		}
		return classDiagrams;
	}
	
	/**
	 * Delete a diagram from its key
	 * @param key the id of the diagram.
	 */
	public void deleteDiagram(String key) {
		PersistenceManager pm = ServerHelper.getPM();
		try {
			Diagram diagram = pm.getObjectById(Diagram.class, key);
			if (diagram == null) 
				throw new GWTGeneratorException("The diagram to delete was not found.");
			
			pm.deletePersistent(diagram);
		} finally {
			pm.close();
		}
	}
	
	
	/**
	 * Save a diagram from a dto.
	 * @param diagramToSave dto to copy in base.
	 */
	public void saveDiagram(DiagramDto diagramToSave) {
		PersistenceManager pm = ServerHelper.getPM();
		try {
			Diagram diagram = pm.getObjectById(Diagram.class, diagramToSave.getKey());
			if (diagram == null) 
				throw new GWTGeneratorException("The diagram to save was not found.");
			
			diagram.copyFromDiagramDto(diagramToSave);
			
		} finally {
			pm.close();
		}
	}
}
