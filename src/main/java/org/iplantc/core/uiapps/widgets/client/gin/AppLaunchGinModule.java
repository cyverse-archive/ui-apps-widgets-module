package org.iplantc.core.uiapps.widgets.client.gin;

import org.iplantc.core.uiapps.widgets.client.presenter.AppLaunchPresenterImpl;
import org.iplantc.core.uiapps.widgets.client.view.AppLaunchView;
import org.iplantc.core.uiapps.widgets.client.view.AppLaunchViewImpl;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm;
import org.iplantc.core.uiapps.widgets.client.view.LaunchAnalysisView;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateFormImpl;
import org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentEditorFactoryImpl;
import org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentGroupEditorImpl;
import org.iplantc.core.uiapps.widgets.client.view.editors.LaunchAnalysisViewImpl;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserSettings;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class AppLaunchGinModule extends AbstractGinModule {

    @Provides
    @Singleton
    public AppTemplateWizardAppearance createAppTemplateWizardAppearance() {
        return AppTemplateWizardAppearance.INSTANCE;
    }

    @Provides
    @Singleton
    public EventBus createEventBus() {
        return EventBus.getInstance();
    }

    @Provides
    @Singleton
    public UserSettings createUserSettings() {
        return UserSettings.getInstance();
    }

    @Override
    protected void configure() {

        bind(AppTemplateForm.class).to(AppTemplateFormImpl.class);
        bind(AppLaunchView.class).to(AppLaunchViewImpl.class);
        bind(AppLaunchView.Presenter.class).to(AppLaunchPresenterImpl.class);
        bind(LaunchAnalysisView.class).to(LaunchAnalysisViewImpl.class);

        bind(AppTemplateForm.ArgumentGroupEditor.class).to(ArgumentGroupEditorImpl.class);
        bind(AppTemplateForm.ArgumentEditorFactory.class).to(ArgumentEditorFactoryImpl.class);
    }
}
