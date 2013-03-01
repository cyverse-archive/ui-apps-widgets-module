package org.iplantc.core.uiapps.widgets.client.view;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentGroupListEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;

/**
 * 
 * Will eventually have to support drops to this form and the things it may contain.
 * 
 * Drops on the form itself
 * Drops on "groups"
 * 
 * 
 * TODO JDS View needs to have a TextBox across the bottom which displays the resulting command line
 * argument.
 * 
 * @author jstroot
 * 
 */
public class AppWizardViewImpl extends Composite implements AppWizardView {

    @UiTemplate("AppWizardView.ui.xml")
    interface AppWizardViewUIUiBinder extends UiBinder<Widget, AppWizardViewImpl> {}
    
    public interface Driver extends SimpleBeanEditorDriver<AppTemplate, AppWizardViewImpl> {}

    private static AppWizardViewUIUiBinder BINDER = GWT.create(AppWizardViewUIUiBinder.class);
    private final Driver driver = GWT.create(Driver.class);

    @UiField
    @Ignore
    FormPanel formPanel;

    @UiField
    ArgumentGroupListEditor argumentGroups;

    @UiField
    @Ignore
    TextButton launchButton;

    private AppWizardView.Presenter presenter;

    public AppWizardViewImpl() {
        Widget createAndBindUi = BINDER.createAndBindUi(this);
        initWidget(createAndBindUi);
        driver.initialize(this);
    }

    @Override
    public SimpleBeanEditorDriver<AppTemplate, ? extends AppWizardView> getEditorDriver() {
        return driver;
    }

    @Override
    public void setPresenter(AppWizardView.Presenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("launchButton")
    void onLaunchButtonClicked(SelectEvent event) {
        // Flush the editor driver to perform validations before calling back to presenter.
        FormPanelHelper.isValid(argumentGroups);

        AppTemplate at = driver.flush();
        Splittable split = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(at));
        if (driver.hasErrors()) {
            //
            GWT.log("Editor has errors");
            for (EditorError error : driver.getErrors()) {
                GWT.log("\t-- " + error.getPath() + ": " + error.getMessage());
            }
        } else {
            // If there are no errors, pass flushed template to presenter.
            presenter.doLaunchAnalysis(at);
        }
    }

}
