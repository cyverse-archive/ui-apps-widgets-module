/**
 *
 */
package org.iplantc.core.uiapps.widgets.client.presenter;

import org.iplantc.core.uiapps.widgets.client.services.DeployedComponentServices;
import org.iplantc.core.uiapps.widgets.client.view.deployedComponents.DeployedComponentsListingView;
import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponent;

import com.google.gwt.user.client.ui.HasOneWidget;

/**
 * @author sriram
 *
 */
public class DeployedComponentPresenterImpl implements DeployedComponentsListingView.Presenter {

    private final DeployedComponentsListingView view;
    public DeployedComponentPresenterImpl(DeployedComponentsListingView view, final DeployedComponentServices dcService) {
        this.view = view;
        this.view.setPresenter(this);
    }


    /* (non-Javadoc)
     * @see org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView.Presenter#getSelectedDC()
     */
    @Override
    public DeployedComponent getSelectedDC() {
        return view.getSelectedDC();
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
    }
}
