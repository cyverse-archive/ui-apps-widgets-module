package org.iplantc.core.uiapps.widgets.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeployedComponentServices {

    void getDeployedComponents(AsyncCallback<String> callback);

    void searchDeployedComponents(String searchTerm, AsyncCallback<String> callback);
}
