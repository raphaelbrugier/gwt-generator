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

import com.objetdirect.engine.ScriptDescriptor;
import com.objetdirect.engine.TestUtil;
import com.objetdirect.entities.EntityDescriptor;
import com.objetdirect.seam.lists.PersistentSelectOneListDescriptor;
import com.objetdirect.seam.lists.SelectOneDetailDescriptor;

import junit.framework.TestCase;

public class TestCreateEntityPage extends TestCase {

	public void testSimplePage() {
		Seam.clear();
		EntityDescriptor category = new EntityDescriptor("com.objetdirect.domain", "Category").
			addStringField("name", null);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "CreateSkill", "views", "create-skills");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(category,
			new ScriptDescriptor(
				"return new Category(\"Nom de la categorie\");"
			).replace("Category", category.getClassDescriptor())
		);
		feature.addForm(new FormDescriptor().
			editStringField("name", "Nom de la competence", 30)
		);
		page.setFeature(feature);
		page.build();	
		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Category;",
			"import com.objetdirect.jsffrmk.GuiUtil;",
			"import javax.faces.context.FacesContext;",
			"import javax.persistence.EntityManager;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.core.Conversation;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"createSkill\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class CreateSkillAnimator {",
			"",
			"	@In",
			"	EntityManager entityManager;",
			"	Category category = null;",
			"	String categoryName;",
			"",
			"	public CreateSkillAnimator() {",
			"		if (Conversation.instance().isLongRunning()) {",
			"			Conversation.instance().end(true);",
			"			Conversation.instance().leave();",
			"		}",
			"		Conversation.instance().begin();",
			"	}",
			"",
			"	public boolean isCategoryValid() {",
			"		return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"	}",
			"	",
			"	protected Category buildCategory() {",
			"		return new Category(\"Nom de la categorie\");",
			"	}",
			"	",
			"	public void cancelCategory() {",
			"		GuiUtil.cancelGui();",
			"		if (category!=null)",
			"			entityManager.refresh(category);",
			"		else",
			"			category = null;",
			"	}",
			"	",
			"	public void createCategory() {",
			"		if (isCategoryValid()) {",
			"			Category category = this.category;",
			"			if (category==null)",
			"				category = buildCategory();",
			"			category.setName(categoryName);",
			"			if (this.category==null)",
			"				entityManager.persist(category);",
			"			categoryName = \"\";",
			"			this.category = null;",
			"		}",
			"	}",
			"	",
			"	public String getCategoryName() {",
			"		return categoryName;",
			"	}",
			"	",
			"	public void setCategoryName(String categoryName) {",
			"		this.categoryName = categoryName;",
			"	}",
			"",
			"}"
		);
		TestUtil.assertText(page.getFaceletText(),
			"<!DOCTYPE composition PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"",
			"			\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
			"<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"",
			"	xmlns:s=\"http://jboss.com/products/seam/taglib\"",
			"	xmlns:ui=\"http://java.sun.com/jsf/facelets\"",
			"	xmlns:f=\"http://java.sun.com/jsf/core\"",
			"	xmlns:h=\"http://java.sun.com/jsf/html\"",
			"	xmlns:ice=\"http://www.icesoft.com/icefaces/component\"",
			"	template=\"/layout/template.xhtml\">",
			"",
			"<ui:define name=\"body\">",
			"",
			"	<h:messages styleClass=\"message\"/>",
			"",
			"	<ice:form id=\"pageForm\">",
			"		<ice:panelGroup	styleClass=\"formBorderHighlight\">",
			"",
			"			<div class=\"dialog\">",
			"				<h:panelGroup>",
			"					<s:validateAll>",
			"						<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"							<h:outputText value=\"Nom de la competence : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{createSkill.categoryName}\" size=\"30\"/>",
			"							</s:decorate>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"creer\" action=\"#{createSkill.createCategory}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createSkill.cancelCategory}\"/>",
			"					</div>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"
		);
	}

	public void testCompositePage() {
		Seam.clear();
		EntityDescriptor category = new EntityDescriptor("com.objetdirect.domain", "Category").
			addStringField("name", null);
		EntityDescriptor skill = new EntityDescriptor("com.objetdirect.domain", "Skill").
			addStringField("name", null);
		category.addOneToMany(skill, "skills", true);
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "CreateSkill", "views", "create-skills");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(category,
			new ScriptDescriptor(
				"return new Category(\"Nom de la categorie\");"
			).replace("Category", category.getClassDescriptor())
		);
		feature.addForm(new FormDescriptor().
			editStringField("name", "Nom de la competence", 30)
		);
		PersistentSelectOneListDescriptor persistentList = new PersistentSelectOneListDescriptor("skills")
			.showField("name", "Name", 20)
			.addDelete("Voulez-vous supprimer la competence : #{skillToDelete.name} ?");
		SelectOneDetailDescriptor skillDetail = new SelectOneDetailDescriptor().addCreate(
			new ScriptDescriptor(
				"return new Skill(\"Nom de la comp�tence\");"
			).replace("Skill", skill.getClassDescriptor())
		);
		skillDetail.addForm(new FormDescriptor().
			editStringField("name", "Nom de la competence", 30)
		);
		persistentList.setDetail(skillDetail);
		feature.addPersistentList(persistentList);
		page.setFeature(feature);
		page.build();
		TestUtil.assertText(page.getJavaText(),
			"package com.objetdirect.actions;",
			"",
			"import com.objetdirect.domain.Category;",
			"import com.objetdirect.domain.Skill;",
			"import com.objetdirect.jsffrmk.ExpressionUtil;",
			"import com.objetdirect.jsffrmk.GuiUtil;",
			"import java.util.List;",
			"import javax.faces.context.FacesContext;",
			"import javax.persistence.EntityManager;",
			"import org.jboss.seam.annotations.In;",
			"import org.jboss.seam.annotations.Name;",
			"import org.jboss.seam.annotations.Scope;",
			"import org.jboss.seam.core.Conversation;",
			"import org.jboss.seam.ScopeType;",
			"",
			"@Name(\"createSkill\")",
			"@Scope(ScopeType.CONVERSATION)",
			"public class CreateSkillAnimator {",
			"",
			"	@In",
			"	EntityManager entityManager;",
			"	Category category = null;",
			"	String categoryName;",
			"	Skill targetSkillForDeletion = null;",
			"	Skill currentSkill = null;",
			"	boolean isNewSkill = false;",
			"	String currentSkillName;",
			"	static final int OP_NONE = 0;",
			"	static final int OP_DELETE = 1;",
			"	int operation = OP_NONE;",
			"",
			"	public CreateSkillAnimator() {",
			"		if (Conversation.instance().isLongRunning()) {",
			"			Conversation.instance().end(true);",
			"			Conversation.instance().leave();",
			"		}",
			"		Conversation.instance().begin();",
			"	}",
			"",
			"	public boolean isCategoryValid() {",
			"		return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"	}",
			"	",
			"	protected Category buildCategory() {",
			"		return new Category(\"Nom de la categorie\");",
			"	}",
			"	",
			"	public void cancelCategory() {",
			"		GuiUtil.cancelGui();",
			"		if (category!=null)",
			"			entityManager.refresh(category);",
			"		else",
			"			category = null;",
			"		currentSkill = null;",
			"	}",
			"	",
			"	public void saveCategory() {",
			"		if (isCategoryValid()) {",
			"			Category category = this.category;",
			"			if (category==null)",
			"				category = buildCategory();",
			"			category.setName(categoryName);",
			"			if (this.category==null) {",
			"				entityManager.persist(category);",
			"				this.category = category;",
			"			}",
			"		}",
			"	}",
			"	",
			"	public void createCategory() {",
			"		if (isCategoryValid()) {",
			"			Category category = this.category;",
			"			if (category==null)",
			"				category = buildCategory();",
			"			category.setName(categoryName);",
			"			if (this.category==null)",
			"				entityManager.persist(category);",
			"			categoryName = \"\";",
			"			this.category = null;",
			"		}",
			"	}",
			"	",
			"	public String getCategoryName() {",
			"		return categoryName;",
			"	}",
			"	",
			"	public void setCategoryName(String categoryName) {",
			"		this.categoryName = categoryName;",
			"	}",
			"	",
			"	public boolean isCategorySkillsVisible() {",
			"		return category !=null;",
			"	}",
			"	",
			"	@SuppressWarnings(\"unchecked\")",
			"	public List<Skill> getCategorySkills() {",
			"		return category.getSkills();",
			"	}",
			"	",
			"	public void requestSkillDeletion(Skill skill) {",
			"		targetSkillForDeletion = skill;",
			"		operation = OP_DELETE;",
			"	}",
			"	",
			"	public Skill getTargetSkillForDeletion() {",
			"		return targetSkillForDeletion;",
			"	}",
			"	",
			"	void deleteSkill(Skill skill) {",
			"		category.removeSkill(skill);",
			"		entityManager.remove(skill);",
			"		if (skill==currentSkill)",
			"			currentSkill = null;",
			"	}",
			"	",
			"	public Skill getCurrentSkill() {",
			"		return currentSkill;",
			"	}",
			"	",
			"	void setCurrentSkill(Skill currentSkill) {",
			"		if (this.currentSkill != currentSkill) {",
			"			this.currentSkill = currentSkill;",
			"			currentSkillName = currentSkill.getName();",
			"		}",
			"	}",
			"	",
			"	public void selectSkill(Skill skill) {",
			"		if (currentSkill!=null || operation != OP_NONE)",
			"			return;",
			"		setCurrentSkill(skill);",
			"	}",
			"	",
			"	public boolean isCurrentSkillVisible() {",
			"		return isCategorySkillsVisible() && currentSkill != null;",
			"	}",
			"	",
			"	public void setCurrentSkillVisible(boolean visible) {",
			"	}",
			"	",
			"	void clearCurrentSkill() {",
			"		currentSkill = null;",
			"		isNewSkill = false;",
			"	}",
			"	",
			"	public boolean isSelectedSkill() {",
			"		Skill categorySkill =(Skill)ExpressionUtil.getValue(\"#{categorySkill}\");",
			"		return categorySkill==currentSkill;",
			"	}",
			"	",
			"	public void setSelectedSkill(Skill categorySkill) {",
			"	}",
			"	",
			"	boolean isCurrentSkillValid() {",
			"		return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"	}",
			"	",
			"	public void cancelCurrentSkill() {",
			"		GuiUtil.cancelGui();",
			"		clearCurrentSkill();",
			"	}",
			"	",
			"	public void validateCurrentSkill() {",
			"		if (isCurrentSkillValid()) {",
			"			currentSkill.setName(currentSkillName);",
			"			if (isNewSkill) {",
			"				entityManager.persist(currentSkill);",
			"				category.addSkill(currentSkill);",
			"				createSkill();",
			"			}",
			"			else",
			"				clearCurrentSkill();",
			"		}",
			"	}",
			"	",
			"	protected Skill buildSkill() {",
			"		return new Skill(\"Nom de la comp�tence\");",
			"	}",
			"	",
			"	public void createSkill() {",
			"		setCurrentSkill(buildSkill());",
			"		isNewSkill = true;",
			"	}",
			"	",
			"	public String getValidateSkillButtonLabel() {",
			"		if (isNewSkill)",
			"			return \"Valider et nouveau\";",
			"		else",
			"			return \"Valider\";",
			"	}",
			"	",
			"	public String getCurrentSkillName() {",
			"		return currentSkillName;",
			"	}",
			"	",
			"	public void setCurrentSkillName(String currentSkillName) {",
			"		this.currentSkillName = currentSkillName;",
			"	}",
			"	",
			"	public int getOperation() {",
			"		return operation;",
			"	}",
			"	",
			"	public void setOperation(int operation) {",
			"		this.operation = operation;",
			"	}",
			"	",
			"	public void cancelOperation() {",
			"		operation = OP_NONE;",
			"	}",
			"	",
			"	public void continueOperation() {",
			"		if (operation == OP_DELETE) {",
			"			if (targetSkillForDeletion != null) {",
			"				deleteSkill(targetSkillForDeletion);",
			"				targetSkillForDeletion = null;",
			"			}",
			"		}",
			"		operation = OP_NONE;",
			"	}",
			"	",
			"	public boolean isConfirmRequested() {",
			"		return operation != OP_NONE;",
			"	}",
			"	",
			"	public void setConfirmRequested(boolean confirmRequested) {",
			"		if (!confirmRequested) {",
			"			cancelOperation();",
			"		}",
			"	}",
			"",
			"}"
		);
		TestUtil.assertText(page.getFaceletText(),
			"<!DOCTYPE composition PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"",
			"			\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
			"<ui:composition xmlns=\"http://www.w3.org/1999/xhtml\"",
			"	xmlns:s=\"http://jboss.com/products/seam/taglib\"",
			"	xmlns:ui=\"http://java.sun.com/jsf/facelets\"",
			"	xmlns:f=\"http://java.sun.com/jsf/core\"",
			"	xmlns:h=\"http://java.sun.com/jsf/html\"",
			"	xmlns:ice=\"http://www.icesoft.com/icefaces/component\"",
			"	template=\"/layout/template.xhtml\">",
			"",
			"<ui:define name=\"body\">",
			"",
			"	<h:messages styleClass=\"message\"/>",
			"",
			"	<ice:form id=\"pageForm\">",
			"		<ice:panelGroup	styleClass=\"formBorderHighlight\">",
			"",
			"			<div class=\"dialog\">",
			"				<h:panelGroup>",
			"					<s:validateAll>",
			"						<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"							<h:outputText value=\"Nom de la competence : \" />",
			"							<s:decorate template=\"/layout/edit.xhtml\">",
			"								<h:inputText value=\"#{createSkill.categoryName}\" size=\"30\"/>",
			"							</s:decorate>",
			"						</h:panelGrid>",
			"					</s:validateAll>",
			"					<h:panelGroup rendered=\"#{createSkill.categorySkillsVisible}\">",
			"						<h:panelGroup>",
			"							<ice:dataTable id=\"categorySkills\" value=\"#{createSkill.categorySkills}\" var=\"categorySkill\"",
			"								rows=\"10\" resizable=\"true\" columnWidths=\"200px,100px\">",
			"								<ice:column>",
			"									<f:facet name=\"header\">Name</f:facet>",
			"									<ice:rowSelector selectionAction=\"#{createSkill.selectSkill(categorySkill)}\" value=\"#{createSkill.selectedSkill}\" multiple=\"false\" />",
			"									<h:outputText value=\"#{categorySkill.name}\" />",
			"								</ice:column>",
			"								<ice:column>",
			"									<h:commandButton value=\"del\" action=\"#{createSkill.requestSkillDeletion(categorySkill)}\" disabled=\"#{createSkill.currentSkillVisible}\"/>",
			"								</ice:column>",
			"							</ice:dataTable>",
			"							<ice:dataPaginator for=\"categorySkills\" fastStep=\"10\" paginator=\"true\" paginatorMaxPages=\"9\">",
			"								<f:facet name=\"first\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-first.gif\" style=\"border:none;\"/>",
			"								</f:facet>",
			"								<f:facet name=\"last\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-last.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"previous\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-previous.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"next\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-next.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"fastforward\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-ff.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"								<f:facet name=\"fastrewind\">",
			"									<ice:graphicImage url=\"/xmlhttp/css/xp/css-images/arrow-fr.gif\" style=\"border:none;\" />",
			"								</f:facet>",
			"							</ice:dataPaginator>",
			"							<h:panelGroup rendered=\"#{!createSkill.currentSkillVisible}\">",
			"								<h:commandButton value=\"Creer\" action=\"#{createSkill.createSkill}\"/>",
			"							</h:panelGroup>",
			"						</h:panelGroup>",
			"						<h:panelGroup rendered=\"#{createSkill.currentSkillVisible}\">",
			"							<div class=\"mask\"/>",
			"							<h:panelGroup style=\"position:relative\">",
			"								<s:validateAll>",
			"									<h:panelGrid columns=\"4\" rowClasses=\"prop\" columnClasses=\"name,value,name,value\">",
			"										<h:outputText value=\"Nom de la competence : \" />",
			"										<s:decorate template=\"/layout/edit.xhtml\">",
			"											<h:inputText value=\"#{createSkill.currentSkillName}\" size=\"30\"/>",
			"										</s:decorate>",
			"									</h:panelGrid>",
			"								</s:validateAll>",
			"								<div class=\"actionButtons\">",
			"									<h:commandButton value=\"#{createSkill.validateSkillButtonLabel}\" action=\"#{createSkill.validateCurrentSkill}\"/>",
			"									<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createSkill.cancelCurrentSkill}\"/>",
			"								</div>",
			"							</h:panelGroup>",
			"						</h:panelGroup>",
			"					</h:panelGroup>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"creer\" action=\"#{createSkill.createCategory}\"/>",
			"						<h:commandButton value=\"sauver\" action=\"#{createSkill.saveCategory}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createSkill.cancelCategory}\"/>",
			"					</div>",
			"				</h:panelGroup>",
			"			</div>",
			"		</ice:panelGroup>",
			"		<ice:panelPopup modal=\"true\" draggable=\"true\" styleClass=\"popup\" rendered=\"#{createSkill.confirmRequested}\">",
			"			<f:facet name=\"header\">",
			"				<ice:panelGrid styleClass=\"title\" cellpadding=\"0\" cellspacing=\"0\" columns=\"2\">",
			"					<ice:outputText value=\"Confirmation\"/>",
			"				</ice:panelGrid>",
			"			</f:facet>",
			"			<f:facet name=\"body\">",
			"				<ice:panelGrid width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" column=\"1\" styleClass=\"body\">",
			"					<ice:outputText value=\"Voulez-vous supprimer la competence : #{createSkill.targetSkillForDeletion.name} ?\" rendered=\"#{createSkill.operation==1}\"/>",
			"					<div class=\"actionButtons\">",
			"						<h:commandButton value=\"valider\" action=\"#{createSkill.continueOperation}\"/>",
			"						<h:commandButton value=\"annuler\" immediate=\"true\" action=\"#{createSkill.cancelOperation}\"/>",
			"					</div>",
			"				</ice:panelGrid>",
			"			</f:facet>",
			"		</ice:panelPopup>",
			"	</ice:form>",
			"</ui:define>",
			"</ui:composition>"		
		);
	}
	
	public void testValidationInCreationPage() {
		Seam.clear();
		EntityDescriptor category = new EntityDescriptor("com.objetdirect.domain", "Category").
			addStringField("name", null);
		category.addUniciyConstraint("name already used", "name");
		PageDescriptor page = 
			new PageDescriptor("com.objetdirect.actions", "CreateSkill", "views", "create-skills");
		CreateEntityDescriptor feature = new CreateEntityDescriptor(category,
			new ScriptDescriptor(
				"return new Category(\"Nom de la categorie\");"
			).replace("Category", category.getClassDescriptor())
		);
		feature.addForm(new FormDescriptor().
			editStringField("name", "Nom de la competence", 30)
		);
		page.setFeature(feature);
		page.build();	
		TestUtil.assertExists(page.getJavaText(),
			"public boolean isCategoryValid() {",
			"	String message;",
			"	message = category.verifyNameUnicity(entityManager, this.categoryName);",
			"	if (message!=null)",
			"		FacesContext.getCurrentInstance().addMessage(\"\", new FacesMessage(message));",
			"	return !FacesContext.getCurrentInstance().getMessages().hasNext();",
			"}"
		);
	}
	
	/*
	 */
}
