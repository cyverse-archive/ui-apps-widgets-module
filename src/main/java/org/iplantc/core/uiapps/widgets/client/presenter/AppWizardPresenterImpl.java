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
     * <code>AppWizardView</code> contains an editor and needs to be initialized with a valid
     * <code>AppTemplate</code>. Therefore, this presenter is responsible for the instantiation of the
     * <code>AppWizardView</code>.
     * FIXME JDS This is no longer true. Update documentation.
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

        view.edit(appTemplate);
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
