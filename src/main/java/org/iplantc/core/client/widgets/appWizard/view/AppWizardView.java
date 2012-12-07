package org.iplantc.core.client.widgets.appWizard.view;

import org.iplantc.core.client.widgets.appWizard.models.AppTemplate;
import org.iplantc.core.client.widgets.appWizard.view.editors.TemplateGroupListEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * Will eventually have to support drops to this form and the things it may contain.
 * 
 * Drops on the form itself
 * Drops on "groups"
 * @author jstroot
 *
 */
public class AppWizardView extends Composite implements Editor<AppTemplate> {

    @UiTemplate("AppWizardView.ui.xml")
    interface AppWizardViewUIUiBinder extends UiBinder<Widget, AppWizardView> {
    }
    
    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter{
    
        void setAppTemplateFromJsonString(String json);
    
        AppTemplate getAppTemplate();
    }

    private static AppWizardViewUIUiBinder BINDER = GWT.create(AppWizardViewUIUiBinder.class);

    @UiField
    TemplateGroupListEditor groupsEditor;
    
    public AppWizardView() {
        initWidget(BINDER.createAndBindUi(this));
    }
}
