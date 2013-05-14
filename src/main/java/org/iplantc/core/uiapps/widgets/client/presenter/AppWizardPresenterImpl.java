package org.iplantc.core.uiapps.widgets.client.presenter;

import java.util.List;

import org.iplantc.core.resources.client.messages.IplantDisplayStrings;
import org.iplantc.core.uiapps.widgets.client.events.AnalysisLaunchEvent;
import org.iplantc.core.uiapps.widgets.client.events.AnalysisLaunchEvent.AnalysisLaunchEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.JobExecution;
import org.iplantc.core.uiapps.widgets.client.services.AppTemplateServices;
import org.iplantc.core.uiapps.widgets.client.view.AppWizardView;
import org.iplantc.core.uiapps.widgets.client.view.AppWizardViewImpl;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uicommons.client.presenter.Presenter;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
    private final AppTemplateServices atServices;
    private final UserSettings userSettings;
    private final UserInfo userInfo;
    private final IplantDisplayStrings displayMessages;
    private final List<AnalysisLaunchEventHandler> analysisLaunchHandlers = Lists.newArrayList();
    
    /**
     * Class constructor.
     * 
     * This {@link Presenter} implementation differs from the normal pattern since it does not take a
     * <code>View</code> at construction time. This is due to the fact that the
     * <code>AppWizardView</code> contains an editor and needs to be initialized with a valid
     * <code>AppTemplate</code>. Therefore, this presenter is responsible for the instantiation of the
     * <code>AppWizardView</code>.
     */
    public AppWizardPresenterImpl() {
        this.atServices = GWT.create(AppTemplateServices.class);
        this.eventBus = EventBus.getInstance();
        this.userSettings = UserSettings.getInstance();
        this.userInfo = UserInfo.getInstance();
        this.displayMessages = GWT.create(IplantDisplayStrings.class);
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
        view = new AppWizardViewImpl(eventBus, userSettings, userInfo, displayMessages);
        view.setPresenter(this);

        view.edit(appTemplate);
        container.setWidget(view);
    }

    @Override
    public void setAppTemplateFromLegacyJson(Splittable legacyJson) {
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
    public void doLaunchAnalysis(final AppTemplate at, JobExecution je) {
        atServices.launchAnalysis(at, je, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                GWT.log("SUCCESS: Launch ANalysis reponse: " + result);
                for (AnalysisLaunchEventHandler handler : analysisLaunchHandlers) {
                    handler.onAnalysisLaunch(new AnalysisLaunchEvent(at));
                }
            }

            @Override
            public void onFailure(Throwable caught) {
            }
        });

    }

    @Override
    public void addAnalysisLaunchHandler(AnalysisLaunchEventHandler handler) {
        analysisLaunchHandlers.add(handler);
    }
}
