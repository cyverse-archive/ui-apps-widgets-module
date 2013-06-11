/**
 *
 */
package org.iplantc.core.uiapps.widgets.client.presenter;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.widgets.client.services.DeployedComponentServices;
import org.iplantc.core.uiapps.widgets.client.view.deployedComponents.DeployedComponentsListingView;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponent;
import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponentAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponentList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * @author sriram
 *
 */
public class DeployedComponentPresenterImpl implements DeployedComponentsListingView.Presenter {

    private final DeployedComponentsListingView view;
    private List<DeployedComponent> depCompList;
    private final
    DeployedComponentAutoBeanFactory factory = GWT.create(DeployedComponentAutoBeanFactory.class);
    private final DeployedComponentServices dcService;

    public DeployedComponentPresenterImpl(DeployedComponentsListingView view, final DeployedComponentServices dcService) {
        this.view = view;
        this.dcService = dcService;
        this.view.setPresenter(this);
        loadDeployedComponents();
    }


    /* (non-Javadoc)
     * @see org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView.Presenter#getSelectedDC()
     */
    @Override
    public DeployedComponent getSelectedDC() {
        return view.getSelectedDC();
    }

    /* (non-Javadoc)
     * @see org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView.Presenter#searchDC(java.lang.String)
     */
    @Override
    public void searchDC(String filter) {
        if (filter != null && !filter.isEmpty()) {
            if (filter.length() >= 3) {
                view.mask();
                dcService.searchDeployedComponents(filter, new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                        view.unmask();
                    }

                    @Override
                    public void onSuccess(String result) {
                        view.loadDC(parseResult(result));
                        view.unmask();
                    }
                });
            }
        } else {
            loadDeployedComponents();
        }

    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
    }

    @Override
    public void loadDeployedComponents() {
        view.mask();
        if (depCompList == null) {
            dcService.getDeployedComponents(new AsyncCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    // setCurrentCompSelection(currentSelection);
                    // cache result
                    depCompList = parseResult(result);
                    view.loadDC(depCompList);
                    view.unmask();

                }

                @Override
                public void onFailure(Throwable caught) {
                    view.unmask();
                    ErrorHandler.post(I18N.ERROR.dcLoadError(), caught);
                }
            });
        } else {
            view.loadDC(depCompList);
            view.unmask();
        }
    }

    private List<DeployedComponent> parseResult(String result) {
        AutoBean<DeployedComponentList> autoBean = AutoBeanCodex.decode(factory,
                DeployedComponentList.class, result);
        List<DeployedComponent> items = autoBean.as().getDCList();
        return items;

    }

}
