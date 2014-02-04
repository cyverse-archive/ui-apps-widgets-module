/**
 * 
 */
package org.iplantc.de.apps.widgets.client.view.deployedComponents;

import java.util.List;

import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponent;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author sriram
 *
 */
public interface DeployedComponentsListingView extends IsWidget {
    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {

        DeployedComponent getSelectedDC();
    }

    public DeployedComponent getSelectedDC();

    public void loadDC(List<DeployedComponent> list);

    public void mask();

    public void setPresenter(final Presenter presenter);

    public void showInfo(DeployedComponent dc);

    public void unmask();

}
