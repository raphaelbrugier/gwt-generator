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
package com.objetdirect.gwt.gen.client.ui.diagramsList;

import static com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType.CLASS;
import static com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType.OBJECT;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;
import com.objetdirect.gwt.gen.client.event.EditDiagramEvent;
import com.objetdirect.gwt.gen.client.event.NewProjectEvent;
import com.objetdirect.gwt.gen.client.event.NewProjectEvent.NewProjectEventHandler;
import com.objetdirect.gwt.gen.client.services.DiagramServiceAsync;
import com.objetdirect.gwt.gen.client.services.ProjectServiceAsync;
import com.objetdirect.gwt.gen.client.ui.popup.ErrorToaster;
import com.objetdirect.gwt.gen.client.ui.popup.MessageToaster;
import com.objetdirect.gwt.gen.client.ui.resources.TreeProjectsResources;
import com.objetdirect.gwt.gen.shared.dto.DiagramDto;
import com.objetdirect.gwt.gen.shared.entities.Directory;
import com.objetdirect.gwt.gen.shared.entities.Project;
import com.objetdirect.gwt.gen.shared.entities.Directory.DirectoryType;
import com.objetdirect.gwt.umlapi.client.umlCanvas.UMLCanvas;
import com.objetdirect.gwt.umlapi.client.umlcomponents.DiagramType;

/**
 * Presenter for the explorer list of projects and directories on the explorer page.
 * Presenter and its associated view are inspired by the mvp pattern.
 * @see http://code.google.com/intl/fr/webtoolkit/articles/mvp-architecture.html
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class DiagramsListPresenter {

	public interface DisplayPopupDiagram {
		public void show();
		public void hide();
		public HasClickHandlers getCreateButton();
		public String getDiagramName();
	}
	
	public interface Display {
		/**
		 * @return The main container
		 */
		HasWidgets getContainer();

		/**
		 * @return A widget to display when loading project and directories.
		 */
		Widget getLoadingProjectsWidget();

		/**
		 * @return A widget to display a message when the use has no project
		 */
		Widget getNoProjectWidget();

		/**
		 * Create a new diagramTreeItem with the given diagram name
		 * @param diagramName the name displayed in the tree item
		 * @return the created TreeItem;
		 */
		DiagramTreeItem createDiagramTreeItem(String diagramName);

		/**
		 * @return The view as a widget.
		 */
		Widget asWidget();
	}

	private final HandlerManager eventBus;

	private final Display display;

	private final DiagramServiceAsync diagramService;

	private final ProjectServiceAsync projectService;

	private final Tree tree;

	public DiagramsListPresenter(HandlerManager eventBus, Display display, DiagramServiceAsync diagramService, ProjectServiceAsync projectService) {
		this.eventBus = eventBus;
		this.display = display;
		this.diagramService = diagramService;
		this.projectService = projectService;

		// TODO tree and treeItems creation should be delegate to the view.
		tree = new Tree(TreeProjectsResources.INSTANCE, true);
		display.getContainer().add(tree);

		addHandlers();
		doFetchProjects();
	}

	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}


	void addHandlers() {

		eventBus.addHandler(NewProjectEvent.TYPE, new NewProjectEventHandler() {
			@Override
			public void onNewProjectEvent(NewProjectEvent event) {
				final CreateProjectPopup createProjectPopUp = new CreateProjectPopup();
				createProjectPopUp.getCreateButton().addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent e) {
						doCreateProject(createProjectPopUp, createProjectPopUp.getProjectName());
					}
				});
				createProjectPopUp.show();
			}
		});
	}


	/**
	 * Create the items on the tree for the project.
	 * @param project
	 * @return The item representing the project with its directory children.
	 */
	private TreeItem createProjectItem(final Project project) {
		ProjectTreeItem projectItem = new ProjectTreeItem(project);
		bindProjectTreeItem(projectItem);

		for (Directory directory : project.getDirectories()) {
			DirectoryTreeItem directoryTreeItem = new DirectoryTreeItem(directory);
			bindDirectoryItem(project, directoryTreeItem);
			projectItem.addItem(directoryTreeItem);
			
			for (DiagramDto diagram : directory.getDiagrams()) {
				DiagramTreeItem diagramTreeItem = display.createDiagramTreeItem(diagram.getName());
				bindDiagramTreeItem(diagram, diagramTreeItem);
				directoryTreeItem.addItem(diagramTreeItem);
			}
		}

		return projectItem;
	}

	/**
	 * Bind a projectTreeItem buttons with the action : - delete the project
	 * 
	 * @param projectItem
	 *            The project tree item where the actions are attached.
	 */
	private void bindProjectTreeItem(final ProjectTreeItem projectItem) {
		projectItem.getDeleteProjectButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				//TODO Display a popup to ask to delete or not the project ?
				doDeleteProject(projectItem.getProject());
			}
		});
	}

	/**
	 * Bind the directoryTreeItem buttons with the action : - create a new diagram
	 * @param project Project owning the directory.
	 * @param directoryTreeItem
	 *            The directory tree item where the actions are attached.
	 */
	private void bindDirectoryItem(final Project project, final DirectoryTreeItem directoryTreeItem) {
		directoryTreeItem.getAddDiagramButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// Depending of the type of the directory we use a different popup.
				if (directoryTreeItem.getDirectory().getDirType() == DirectoryType.SEAM) {
					bindCreateObjectDiagram(project, directoryTreeItem);
				} else {
					bindCreateDiagram(directoryTreeItem, project.isSeamProject());
				}
			}
		});
	}
	
	void bindCreateObjectDiagram(final Project project, final DirectoryTreeItem directoryTreeItem) {
		Directory classDirectory  = getClassDiagramDirectoryFromProject(project);
		
		final CreateObjectDiagramPopup popup =  new CreateObjectDiagramPopup(classDirectory.getDiagrams());
		
		popup.getCreateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Directory directory = directoryTreeItem.getDirectory();
				String classDiagramKey = popup.getClassDiagramKeySelected();
				doCreateDiagram(popup, directory, popup.getDiagramName(), classDiagramKey, false);
			}
		});

		popup.show();
	}
	
	private Directory getClassDiagramDirectoryFromProject(Project project) {
		for(Directory dir : project.getDirectories()) {
			if (dir.getDirType() == DirectoryType.DOMAIN)
				return dir;
		}
		return null;
	}

	void bindCreateDiagram(final DirectoryTreeItem directoryTreeItem, final boolean isSeamProject) {
		final DisplayPopupDiagram popup =  new CreateDiagramPopup();
		
		popup.getCreateButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Directory directory = directoryTreeItem.getDirectory();
				doCreateDiagram(popup, directory, popup.getDiagramName(), null, isSeamProject);
			}
		});
		
		popup.show();
	}
	

	/**
	 * Bind the diagramTreeItem (view) with the actions :
	 *  - Edit the diagram.
	 *  - Delete the diagram.
	 * @param diagram The diagram on which the actions occurs.
	 * @param diagramTreeItem The view where the actions are attached.
	 */
	private void bindDiagramTreeItem(final DiagramDto diagram, DiagramTreeItem diagramTreeItem) {
		diagramTreeItem.getEditDiagramButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doEditDiagram(diagram);
			}
		});

		diagramTreeItem.getDeleteDiagramButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				doDeleteDiagram(diagram);
			}
		});
	}

	/**
	 * Fill the tree with the user's projects and directories. If the user has no project, display a message.
	 */
	private void doFetchProjects() {
		display.getContainer().clear();
		display.getContainer().add(display.getLoadingProjectsWidget());

		tree.removeItems();
		projectService.getProjects(new AsyncCallback<List<Project>>() {

			@Override
			public void onSuccess(List<Project> projectsFound) {
				display.getContainer().clear();
				if (projectsFound.size() == 0) {
					display.getContainer().add(display.getNoProjectWidget());
				} else {
					for (Project project : projectsFound) {
						tree.addItem(createProjectItem(project));
					}
					display.getContainer().add(tree);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error while fetching the projects : " + caught.getMessage());
				ErrorToaster.show("Error while fetching the projects, please retry in few moments or contact the administrator.");
			}
		});
	}

	/**
	 * Create a project.
	 * Automatically add the seam diagram to the project.
	 * When the action is finish (with success or not), close the popup and display a message.
	 * @param createProjectPopup The popup to hide when the action of creation is finished.
	 * @param projectName The name of the project to create.
	 */
	private void doCreateProject(final CreateProjectPopup createProjectPopup, final String projectName) {
		projectService.createProject(projectName, new AsyncCallback<Long>() {
			@Override
			public void onSuccess(Long result) {
				doFetchProjects();
				createProjectPopup.hide();
				MessageToaster.show("Project " + projectName + " created with success.");
			}

			@Override
			public void onFailure(Throwable caught) {
				createProjectPopup.hide();
				Log.error("Failed to create the project " + projectName);
				ErrorToaster.show("Failed to create the project " + projectName +", please retry in few moments or contact the administrator.");
			}
		});
	}

	/**
	 * Delete a project.
	 * When the project is deleted with success, display a message and reload the tree of projects.
	 * @param projectToDelete The project to delete
	 */
	private void doDeleteProject(final Project projectToDelete) {
		projectService.deleteProject(projectToDelete, new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				doFetchProjects();
				MessageToaster.show("Project " + projectToDelete.getName() + " deleted with success");
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed to delete the project " + projectToDelete);
				ErrorToaster.show("Failed to delete the project " + projectToDelete.getName() +", please retry in few moments or contact the administrator.");
			}
		});
	}

	/**
	 * Create a diagram in the given directory
	 * When the diagram is created, load the designer with it.
	 * 
	 * @param createDiagramPopup The Popup to hide when the action is finished.
	 * @param directory The directory where the diagram is created.
	 * @param diagramName The diagram's name.
	 * @param classDiagramKey If the diagram to create is an object diagram, this is the key to the class diagram instantiated.
	 * @param isSeamDiagram true if the diagram is created in the special seam admin project.
	 */
	private void doCreateDiagram(DisplayPopupDiagram createDiagramPopup, Directory directory, String diagramName, String classDiagramKey, boolean isSeamDiagram) {
		createDiagramPopup.hide();
		String directoryKey = directory.getKey();
		DiagramType diagramType = getDiagramTypeFromDirectoryType(directory.getDirType());
		
		final DiagramDto diagramDto = new DiagramDto(directoryKey, diagramName, diagramType);
		diagramDto.classDiagramKey = classDiagramKey;
		diagramDto.setIsSeamDiagram(isSeamDiagram);

		// Create a default canvas to save with the new diagram.
		UMLCanvas defaultCanvas = UMLCanvas.createUmlCanvas(diagramType);
		diagramDto.setCanvas(defaultCanvas);

		diagramService.createDiagram(diagramDto, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String key) {
				diagramDto.setKey(key);
				doEditDiagram(diagramDto);
				doFetchProjects();
				MessageToaster.show("Diagram " + diagramDto.getName() + " created with success.");
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Error while creating the diagram : " + caught.getMessage());
				ErrorToaster.show("Error while creation the diagram, please retry in few moments or contact the administrator.");
			}
		});
	}

	/**
	 * @param directoryType
	 * @return a type of diagram from a type of directory.
	 */
	private DiagramType getDiagramTypeFromDirectoryType(DirectoryType directoryType) {
		switch (directoryType) {
			case DOMAIN : return CLASS;
			case SEAM : return OBJECT;
		}
		return CLASS;
	}

	/**
	 * Request the edition of the given diagram
	 * @param diagram the diagram to edit in the modeler.
	 */
	private void doEditDiagram(final DiagramDto diagram) {
		eventBus.fireEvent(new EditDiagramEvent(diagram));
	}

	/**
	 * Request the deletion of the given diagram.
	 * @param diagram The diagram to delete.
	 */
	private void doDeleteDiagram(final DiagramDto diagram) {
		diagramService.deleteDiagram(diagram.getKey(), new AsyncCallback<Void>() {

			@Override
			public void onSuccess(Void result) {
				doFetchProjects();
				MessageToaster.show("Diagram " + diagram.getName() + " deleted with success.");
			}

			@Override
			public void onFailure(Throwable caught) {
				Log.error("Failed to delete the diagram " + diagram);
				ErrorToaster.show("Failed to delete the diagram " + diagram.getName() +", please retry in few moments or contact the administrator.");
			}
		});
	}
}
