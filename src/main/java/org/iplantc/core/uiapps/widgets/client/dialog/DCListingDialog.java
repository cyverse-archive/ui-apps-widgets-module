/**
 * 
 */
package org.iplantc.core.uiapps.widgets.client.dialog;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.DeployedComponent;
import org.iplantc.core.uiapps.widgets.client.presenter.DeployedComponentPresenterImpl;
import org.iplantc.core.uiapps.widgets.client.services.DeployedComponentServices;
import org.iplantc.core.uiapps.widgets.client.view.deployedComponents.DeployedComponentsListingView;
import org.iplantc.core.uiapps.widgets.client.view.deployedComponents.DeployedComponentsListingViewImpl;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * 
 * @author sriram
 * 
 */
public class DCListingDialog extends IPlantDialog {

    private DeployedComponent selectedComponent = null;
    
    public DCListingDialog() {
        setHideOnButtonClick(true);
        setPixelSize(600, 500);
        setResizable(false);
        setModal(true);
        setHeadingText("Installed Tools");
        getOkButton().setEnabled(false);

        ListStore<DeployedComponent> listStore = new ListStore<DeployedComponent>(new DCKeyProvider());
        DeployedComponentsListingView view = new DeployedComponentsListingViewImpl(listStore,
                new DCSelectionChangedHandler());
        DeployedComponentServices dcService = GWT.create(DeployedComponentServices.class);
        DeployedComponentsListingView.Presenter p = new DeployedComponentPresenterImpl(view, dcService);
        p.go(this);

    }

    public DeployedComponent getSelectedComponent() {
        return selectedComponent;
    }


    class DCKeyProvider implements ModelKeyProvider<DeployedComponent> {

        @Override
        public String getKey(DeployedComponent item) {
            return item.getId();
        }

    }

    class DCSelectionChangedHandler implements SelectionChangedHandler<DeployedComponent> {

        @Override
        public void onSelectionChanged(SelectionChangedEvent<DeployedComponent> event) {
            List<DeployedComponent> items = event.getSelection();
            if (items != null && items.size() > 0) {
                getButtonById(PredefinedButton.OK.toString()).setEnabled(true);
                selectedComponent = items.get(0);
            } else {
                getButtonById(PredefinedButton.OK.toString()).setEnabled(false);
                selectedComponent = null;
            }

        }

    }

}
