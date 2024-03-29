/*
 * This file is part of the GWTUML project and was written by Mounier Florian <mounier-dot-florian.at.gmail'dot'com> for Objet Direct
 * <http://wwww.objetdirect.com>
 * 
 * Copyright © 2009 Objet Direct Contact: gwtuml@googlegroups.com
 * 
 * GWTUML is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * GWTUML is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with GWTUML. If not, see <http://www.gnu.org/licenses/>.
 */
package com.objetdirect.gwt.gen.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.objetdirect.gwt.gen.client.services.LoginService;
import com.objetdirect.gwt.gen.client.services.LoginServiceAsync;
import com.objetdirect.gwt.gen.client.ui.MainController;
import com.objetdirect.gwt.gen.shared.dto.LoginInfo;

/**
 * Main class for gwtuml application. This class does some initialization and calls the start panel.
 * 
 * @author Henri Darmet
 * @author Florian Mounier (mounier-dot-florian.at.gmail'dot'com)
 * @author Raphael Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public class GwtGenerator implements EntryPoint {

	/** Informations about the logged user or how to log in him. */
	public static LoginInfo loginInfo;
	
	private final LoginServiceAsync loginService = GWT.create(LoginService.class); 
	
	@SuppressWarnings("unused")
	private static MainController mainController;
	
	/*
	 * Real gwt app entry point, this code allow GWT Log to catch exception and display it (non-Javadoc)
	 * 
	 * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
	 */
	public void onModuleLoad() {
		Log.setUncaughtExceptionHandler();
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			
			@Override
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
//				RootLayoutPanel.get().add(new Main());
				mainController = new MainController();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Log.error(caught.getMessage());
				Window.alert(caught.getMessage());
			}
		});
	}
}
