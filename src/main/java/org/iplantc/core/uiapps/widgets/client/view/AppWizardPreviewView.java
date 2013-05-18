package org.iplantc.core.uiapps.widgets.client.view;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizard;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;

public class AppWizardPreviewView extends Window {

    @UiTemplate("AppWizardView.ui.xml")
    interface AppWizardPreviewUiBinder extends UiBinder<Widget, AppWizardPreviewView> {
    }

    private static AppWizardPreviewUiBinder BINDER = GWT.create(AppWizardPreviewUiBinder.class);

    @UiField(provided = true)
    AppTemplateWizard wizard;

    public AppWizardPreviewView(EventBus eventBus, AppTemplate appTemplate) {
        wizard = new AppTemplateWizard(eventBus, false);
        setWidget(BINDER.createAndBindUi(this));
        setHeadingText("Preview of " + appTemplate.getName());
        setSize("640", "375");
        setBorders(false);
        wizard.edit(appTemplate);
    }

}
