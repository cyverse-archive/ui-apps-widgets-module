package org.iplantc.core.client.widgets.appWizard.presenter;

import org.iplantc.core.client.widgets.appWizard.models.AppTemplate;
import org.iplantc.core.client.widgets.appWizard.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.client.widgets.appWizard.view.AppWizardView;
import org.iplantc.core.client.widgets.appWizard.view.AppWizardViewImpl;
import org.iplantc.core.uicommons.client.presenter.Presenter;

import com.google.gwt.core.shared.GWT;
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
    
    /**
     * Class constructor.
     * 
     * This {@link Presenter} implementation differs from the normal pattern since it does not take a
     * <code>View</code> at construction time. This is due to the fact that the
     * <code>AppWizardView</code> is an editor and needs to be initialized with a valid
     * <code>AppTemplate</code>. Therefore, this presenter is responsible for the instantiation of the
     * <code>AppWizardView</code>.
     */
    public AppWizardPresenterImpl(){
    }
    

    @Override
    public void go(HasOneWidget container) {
        view = new AppWizardViewImpl();
        view.getEditorDriver().edit(appTemplate);

        container.setWidget(view);
    }

    @Override
    public void setAppTemplateFromJsonString(String json) {
        // Create AppTemplateSplittable and assign top level values.
        Splittable appTemplateSplit = AppWizardPresenterJsonAdapter.adaptAppTemplateJsonString(json);
        
        AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
        AutoBean<AppTemplate> appTemplateAb = AutoBeanCodex.decode(factory, AppTemplate.class, appTemplateSplit);
        
        this.appTemplate = appTemplateAb.as();
        
    }

    

    @Override
    public AppTemplate getAppTemplate() {
        return appTemplate;
    }

}
