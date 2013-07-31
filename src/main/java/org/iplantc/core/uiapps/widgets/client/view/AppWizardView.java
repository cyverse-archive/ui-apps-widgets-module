package org.iplantc.core.uiapps.widgets.client.view;

import org.iplantc.core.uiapps.widgets.client.events.AnalysisLaunchEvent.AnalysisLaunchEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.metadata.JobExecution;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * The interface definition for the App Wizard view.
 * 
 * The App wizard is an editor which is data bound to a <code>AppTemplate</code>.
 * 
 * @author jstroot
 * 
 */
public interface AppWizardView extends IsWidget {

    /**
     * FIXME JDS Add explanation for why the presenter is broken into two interface definitions.
     * 
     * @author jstroot
     * 
     */
    public interface BasePresenter extends org.iplantc.core.uicommons.client.presenter.Presenter{
    
        AppTemplate getAppTemplate();

        void go(final HasOneWidget container, final AppTemplate appTemplate);

    }

    public interface Presenter extends BasePresenter {
        void doLaunchAnalysis(AppTemplate at, JobExecution je);

        void addAnalysisLaunchHandler(AnalysisLaunchEventHandler handler);
    }

    void setPresenter(AppWizardView.Presenter presenter);

    void edit(AppTemplate appTemplate, JobExecution je);

}
