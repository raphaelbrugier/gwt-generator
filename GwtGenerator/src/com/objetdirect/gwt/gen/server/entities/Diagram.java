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
package com.objetdirect.gwt.gen.server.entities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.users.User;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType;

/**
 * A diagram stored in the app egine.
 * A diagram contains the uml components and their positions on the canvas so they could be restore on the canvas. 
 * A diagram could be a class or an object or a sequence diagram.
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
@PersistenceCapable
public class Diagram {
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
    private String key;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.parent-pk", value="true")
	private String directoryKey;

	@Persistent
	private String name;
	
	@Persistent
	private DiagramType type;
	
	@Persistent
	private Blob serializedCanvas;
	
	@SuppressWarnings("unused")
	@Persistent
	private User user;
	
	/**
	 * In the case where the diagram is an object diagram, this is the key to the Class diagram that it instantiates. 
	 */
	@Persistent
	private String classDiagramKey;
	
	@Persistent
	private boolean isSeamDiagram;

	/**
	 * Default constructor ONLY for gwt-rpc serialization
	 */
	protected Diagram() {
	}
	
	public Diagram(String directoryKey, DiagramType type, String name, User user) {
		this.type = type;
		this.name = name;
		this.user = user;
		this.directoryKey = directoryKey;
		classDiagramKey = null;
		isSeamDiagram = false;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public DiagramType getType() {
		return type;
	}

	/**
	 * @return the classDiagramKey
	 */
	public String getClassDiagramKey() {
		return classDiagramKey;
	}

	/**
	 * @param classDiagramKey the classDiagramKey to set
	 */
	public void setClassDiagramKey(String classDiagramKey) {
		this.classDiagramKey = classDiagramKey;
	}

	public void setCanvas(UMLCanvas umlCanvas) {
		if (umlCanvas == null) {
			return;
		}
		
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(byteOutput);
			oos.writeObject(umlCanvas);
			this.serializedCanvas = new Blob(byteOutput.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public UMLCanvas getCanvas() {
		UMLCanvas canvas = null;
		
		if (serializedCanvas!=null) {
			ByteArrayInputStream byteInput = new ByteArrayInputStream(serializedCanvas.getBytes());
			ObjectInputStream ois;
			try {
				ois = new ObjectInputStream(byteInput);
				canvas = (UMLCanvas)ois.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return canvas;
	}
	
	/**
	 * @return the directoryKey
	 */
	public String getDirectoryKey() {
		return directoryKey;
	}

	/**
	 * @return the isSeamDiagram
	 */
	public boolean isSeamDiagram() {
		return isSeamDiagram;
	}

	/**
	 * @param isSeamDiagram the isSeamDiagram to set
	 */
	public void setSeamDiagram(boolean isSeamDiagram) {
		this.isSeamDiagram = isSeamDiagram;
	}
	
	/**
	 * Copy all the fields of the given DiagramInformations the object.
	 * @param diagramToCopy The source diagram
	 */
	public void copyFromDiagramDto(DiagramDto diagramToCopy) {
		this.type = diagramToCopy.getType();
		this.name = diagramToCopy.getName();
		this.classDiagramKey = diagramToCopy.classDiagramKey;
		setCanvas(diagramToCopy.getCanvas());
	}

	
	/** Copy the fields of the Diagram object into the given DiagramInformations object.
	 * @param diagramToCopy the diagram the targeted diagram
	 */
	public DiagramDto copyToDiagramDto() {
		DiagramDto diagramToCopy = new DiagramDto();
		diagramToCopy.setKey(this.key);
		diagramToCopy.setType(this.type);
		diagramToCopy.setName(this.name);
		diagramToCopy.setDirectoryKey(this.directoryKey);
		diagramToCopy.classDiagramKey = this.classDiagramKey;
		diagramToCopy.setIsSeamDiagram(isSeamDiagram);
		diagramToCopy.setEditable(true);
		diagramToCopy.setCanvas(getCanvas());
		
		return diagramToCopy;
	}

}
