package org.iplantc.core.uiapps.widgets.client.view;

import java.util.List;

import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsDisplayMessages;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.JobExecution;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizard;
import org.iplantc.core.uiapps.widgets.client.view.editors.LaunchAnalysisWidget;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserSettings;

import com.google.common.collect.Lists;
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
    AppTemplateWizard wizard;

    @UiField
    TextButton launchButton;

    LaunchAnalysisWidget law;

    private AppWizardView.Presenter presenter;
    private final EventBus eventBus;

    public AppWizardViewImpl(final EventBus eventBus, final UserSettings userSettings, final AppsWidgetsDisplayMessages displayMessages) {
        this.eventBus = eventBus;
        law = new LaunchAnalysisWidget(userSettings, displayMessages);
        initWidget(BINDER.createAndBindUi(this));
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
        JobExecution je = law.flushJobExecution();
        if (wizard.hasErrors() || law.hasErrors()) {
            //
            GWT.log("Editor has errors");
            List<EditorError> errors = Lists.newArrayList();
            errors.addAll(wizard.getErrors());
            errors.addAll(law.getErrors());
            for (EditorError error : errors) {
                GWT.log("\t-- " + ": " + error.getMessage());
            }
        } else {
            // If there are no errors, pass flushed template to presenter.
            presenter.doLaunchAnalysis(at, je);
        }
    }

    @Override
    public void edit(final AppTemplate appTemplate, final JobExecution je) {
        law.edit(je);
        wizard.edit(appTemplate);
        wizard.insertFirstInAccordion(law);
    }
}
