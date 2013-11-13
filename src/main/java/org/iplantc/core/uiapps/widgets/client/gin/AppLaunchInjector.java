package org.iplantc.core.uiapps.widgets.client.gin;

import org.iplantc.core.uiapps.widgets.client.view.AppLaunchView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(AppLaunchGinModule.class)
public interface AppLaunchInjector extends Ginjector {

    public static final AppLaunchInjector INSTANCE = GWT.create(AppLaunchInjector.class);

    AppLaunchView.Presenter getAppLaunchPresenter();
}
