/**
 *
 */
package org.iplantc.de.apps.widgets.client.presenter;

import com.google.gwt.user.client.ui.HasOneWidget;

import org.iplantc.de.apps.widgets.client.view.deployedComponents.DeployedComponentsListingView;
import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponent;

/**
 * @author sriram
 *
 */
public class DeployedComponentPresenterImpl implements DeployedComponentsListingView.Presenter {

    private final DeployedComponentsListingView view;

    public DeployedComponentPresenterImpl(DeployedComponentsListingView view) {
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
