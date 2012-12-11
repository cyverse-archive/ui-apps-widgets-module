package org.iplantc.core.client.widgets.appWizard.view;

import org.iplantc.core.client.widgets.appWizard.models.AppTemplate;
import org.iplantc.core.client.widgets.appWizard.view.editors.TemplateGroupListEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.FormPanel;

/**
 * 
 * Will eventually have to support drops to this form and the things it may contain.
 * 
 * Drops on the form itself
 * Drops on "groups"
 * @author jstroot
 *
 */
public class AppWizardViewImpl extends Composite implements AppWizardView {

    @UiTemplate("AppWizardView.ui.xml")
    interface AppWizardViewUIUiBinder extends UiBinder<Widget, AppWizardViewImpl> {}
    
    public interface Driver extends SimpleBeanEditorDriver<AppTemplate, AppWizardViewImpl> {}

    private static AppWizardViewUIUiBinder BINDER = GWT.create(AppWizardViewUIUiBinder.class);
    private final Driver driver = GWT.create(Driver.class);

    @Ignore
    @UiField
    FormPanel formPanel;

    @UiField
    TemplateGroupListEditor groupsEditor;

    private AppWizardView.Presenter presenter;
    
    public AppWizardViewImpl() {
        initWidget(BINDER.createAndBindUi(this));
    }


    @Override
    public SimpleBeanEditorDriver<AppTemplate, ? extends AppWizardView> getEditorDriver() {
        driver.initialize(this);
        return driver;
    }

    @Override
    public void setPresenter(AppWizardView.Presenter presenter) {
        this.presenter = presenter;
    }

}
