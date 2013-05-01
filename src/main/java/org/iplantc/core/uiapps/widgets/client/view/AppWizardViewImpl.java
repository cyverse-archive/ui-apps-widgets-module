package org.iplantc.core.uiapps.widgets.client.view;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizard;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FormPanel;

/**
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
    
    private static AppWizardViewUIUiBinder BINDER = GWT.create(AppWizardViewUIUiBinder.class);

    @UiField
    FormPanel formPanel;

    @UiField
    AppTemplateWizard wizard;

    @UiField
    TextButton launchButton;

    private AppWizardView.Presenter presenter;
    private final EventBus eventBus;

    public AppWizardViewImpl(final EventBus eventBus) {
        this.eventBus = eventBus;
        Widget createAndBindUi = BINDER.createAndBindUi(this);
        initWidget(createAndBindUi);
    }

    @Override
    public void setPresenter(AppWizardView.Presenter presenter) {
        this.presenter = presenter;
    }

    @UiFactory
    AppTemplateWizard createAppTemplateWizard() {
        return new AppTemplateWizard(eventBus, false);
    }

    @UiHandler("launchButton")
    void onLaunchButtonClicked(SelectEvent event) {

        // Flush the editor driver to perform validations before calling back to presenter.
        AppTemplate at = wizard.flushAppTemplate();
        if (wizard.hasErrors()) {
            //
            GWT.log("Editor has errors");
            for (EditorError error : wizard.getErrors()) {
                GWT.log("\t-- " + error.getPath() + ": " + error.getMessage());
            }
        } else {
            // If there are no errors, pass flushed template to presenter.
            presenter.doLaunchAnalysis(at);
        }
    }

    @Override
    public void edit(AppTemplate appTemplate) {
        wizard.edit(appTemplate);
    }

}
