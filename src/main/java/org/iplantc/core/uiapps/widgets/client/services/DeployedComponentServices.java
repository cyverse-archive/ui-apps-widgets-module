package org.iplantc.core.uiapps.widgets.client.services;

import java.util.List;

import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponent;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeployedComponentServices {

    void getDeployedComponents(AsyncCallback<List<DeployedComponent>> callback);

    void searchDeployedComponents(String searchTerm, AsyncCallback<List<DeployedComponent>> callback);
}
