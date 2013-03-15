package org.iplantc.core.uiapps.widgets.client.presenter;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.view.AppWizardView;
import org.iplantc.core.uiapps.widgets.client.view.AppWizardViewImpl;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.presenter.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * 
 * Validation Strategy
 * 
 * The current method is "Functionally pro-active". The "Launch Analysis" button is only enabled when the entire form is valid.
 * The obvious intent is to simply not allow a user to launch an analysis unless the form is valid.
 * The downside to this method lies in the lack of communication to the user. If there is a required field in any property group
 * other than the first, the user has to hunt for it.
 * This method is also difficult to manage programmatically. Validation of each field has to be monitored with handlers in 
 * order to appropriately set state of the "Launch" button.
 * 
 * So what?
 * Let the user click the launch button whenever they want. There are only two outcomes, the form is valid, or it is not.
 * If the form is valid, proceed.
 * If the form is invalid, tell the user what is wrong.
 * Since our view literally hides fields (because of the accordian design), we HAVE to tell the user what fields are incorrect.
 * 
 * If the form is invalid, there are a few different reasons:
 *   The user simply didn't enter information into a required field.
 *      The validation messages should be displayed to the user, with some indication of where to find them.
 *   The user entered invalid information into a field.
 *      The fields should auto-validate. So if a user enters invalid information, the field should "inform" the user.
 * @author jstroot
 *
 */
public class AppWizardPresenterImpl implements AppWizardView.Presenter {

    private AppTemplate appTemplate;
    private AppWizardView view;
    private final EventBus eventBus;
    
    /**
     * Class constructor.
     * 
     * This {@link Presenter} implementation differs from the normal pattern since it does not take a
     * <code>View</code> at construction time. This is due to the fact that the
     * <code>AppWizardView</code> is an editor and needs to be initialized with a valid
     * <code>AppTemplate</code>. Therefore, this presenter is responsible for the instantiation of the
     * <code>AppWizardView</code>.
     */
    public AppWizardPresenterImpl() {
        this.eventBus = EventBus.getInstance();
    }
    
    @Override
    public void goLegacy(final HasOneWidget container, final Splittable legacyJson) {
        setAppTemplateFromLegacyJson(legacyJson);
        go(container);
    }

    @Override
    public void go(HasOneWidget container, AppTemplate appTemplate) {
        this.appTemplate = appTemplate;
        go(container);
    }

    @Override
    public void go(HasOneWidget container) {
        view = new AppWizardViewImpl(eventBus);
        view.setPresenter(this);

        view.getEditorDriver().edit(appTemplate);
        container.setWidget(view);
    }

    @Override
    public void setAppTemplateFromLegacyJson(Splittable legacyJson) {
        // LegacyAppTemplateAutoBeanFactory legacyFactory =
        // GWT.create(LegacyAppTemplateAutoBeanFactory.class);
        // Create Legacy App Template Autobean
        // AutoBean<LegacyAppTemplate> legacyAppTemplate = AutoBeanCodex.decode(legacyFactory,
        // LegacyAppTemplate.class, json);
        // Convert Legacy into AppTemplate
        // AutoBean<AppTemplate> at = new
        // LegacyAppTemplateConverter().convertModelValue(legacyAppTemplate);

        // ==== CURRENT ====
        // Create AppTemplateSplittable and assign top level values.
        Splittable appTemplateSplit = AppWizardPresenterJsonAdapter.adaptAppTemplateJsonString(legacyJson);
        
        AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
        AutoBean<AppTemplate> appTemplateAb = AutoBeanCodex.decode(factory, AppTemplate.class, appTemplateSplit);
        
        this.appTemplate = appTemplateAb.as();
        
    }

    @Override
    public AppTemplate getAppTemplate() {
        return appTemplate;
    }

    @Override
    public void doLaunchAnalysis(AppTemplate at) {
        // Temp Test to ensure that identity of objects
        if (at != appTemplate) {
            com.google.gwt.core.client.GWT.log("App Templates are not equal");
        }

    }

}
