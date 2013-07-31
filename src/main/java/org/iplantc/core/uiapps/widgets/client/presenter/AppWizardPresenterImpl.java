package org.iplantc.core.uiapps.widgets.client.presenter;

import java.util.List;

import org.iplantc.core.resources.client.constants.IplantValidationConstants;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsDisplayMessages;
import org.iplantc.core.uiapps.widgets.client.events.AnalysisLaunchEvent;
import org.iplantc.core.uiapps.widgets.client.events.AnalysisLaunchEvent.AnalysisLaunchEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.metadata.JobExecution;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.services.AppTemplateServices;
import org.iplantc.core.uiapps.widgets.client.view.AppWizardView;
import org.iplantc.core.uiapps.widgets.client.view.AppWizardViewImpl;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.models.UserSettings;
import org.iplantc.core.uicommons.client.presenter.Presenter;
import org.iplantc.core.uicommons.client.util.RegExp;
import org.iplantc.de.client.UUIDServiceAsync;

import com.extjs.gxt.ui.client.util.Format;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;

/**
 * 
 * @author jstroot
 *
 */
public class AppWizardPresenterImpl implements AppWizardView.Presenter {

    private AppTemplate appTemplate;
    private AppWizardView view;
    private final AppTemplateServices atServices;
    private final UserSettings userSettings;
    private final UserInfo userInfo;
    private final List<AnalysisLaunchEventHandler> analysisLaunchHandlers = Lists.newArrayList();
    private final IplantValidationConstants valConstants;
    private final AppMetadataServiceFacade appMetadataService;
    private final UUIDServiceAsync uuidService;
    private final AppsWidgetsDisplayMessages appsWidgetsDisplayMessages;
    
    /**
     * Class constructor.
     * 
     * This {@link Presenter} implementation differs from the normal pattern since it does not take a
     * <code>View</code> at construction time. This is due to the fact that the
     * <code>AppWizardView</code> contains an editor and needs to be initialized with a valid
     * <code>AppTemplate</code>. Therefore, this presenter is responsible for the instantiation of the
     * <code>AppWizardView</code>.
     */
    public AppWizardPresenterImpl(final UUIDServiceAsync uuidService, final AppMetadataServiceFacade appMetadataService) {
        this.uuidService = uuidService;
        this.appMetadataService = appMetadataService;

        this.atServices = GWT.create(AppTemplateServices.class);
        this.userSettings = UserSettings.getInstance();
        this.userInfo = UserInfo.getInstance();
        this.appsWidgetsDisplayMessages = GWT.create(AppsWidgetsDisplayMessages.class);
        this.valConstants = GWT.create(IplantValidationConstants.class);
    }
    
    @Override
    public void go(HasOneWidget container, AppTemplate appTemplate) {
        this.appTemplate = appTemplate;
        go(container);
    }

    @Override
    public void go(final HasOneWidget container) {
        view = new AppWizardViewImpl(userSettings, appsWidgetsDisplayMessages, uuidService, appMetadataService, GWT.<AppTemplateWizardAppearance> create(AppTemplateWizardAppearance.class));
        view.setPresenter(this);

        final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
        final JobExecution je = factory.jobExecution().as();
        je.setAppTemplateId(appTemplate.getId());
        je.setEmailNotificationEnabled(userSettings.isEnableEmailNotification());
        je.setWorkspaceId(userInfo.getWorkspaceId());
        // JDS Replace all Cmd Line restricted chars with underscores
        String regex = Format.substitute("[{0}]", RegExp.escapeCharacterClassSet(valConstants.restrictedCmdLineChars()));
        String newName = appTemplate.getName().replaceAll(regex, "_");
        je.setName(newName + "_" + appsWidgetsDisplayMessages.defaultAnalysisName()); //$NON-NLS-1$
        je.setOutputDirectory(userSettings.getDefaultOutputFolder());

        view.edit(appTemplate, je);
        container.setWidget(view);
    }

    @Override
    public AppTemplate getAppTemplate() {
        return appTemplate;
    }

    private void launchAnalysis(final AppTemplate at, final JobExecution je) {
        atServices.launchAnalysis(at, je, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                for (AnalysisLaunchEventHandler handler : analysisLaunchHandlers) {
                    handler.onAnalysisLaunch(new AnalysisLaunchEvent(at));
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
    }

    @Override
    public void doLaunchAnalysis(final AppTemplate at, final JobExecution je) {
        // TODO JDS Add Launch Analysis validations.
        launchAnalysis(at, je);
    }

    @Override
    public void addAnalysisLaunchHandler(AnalysisLaunchEventHandler handler) {
        analysisLaunchHandlers.add(handler);
    }
}
