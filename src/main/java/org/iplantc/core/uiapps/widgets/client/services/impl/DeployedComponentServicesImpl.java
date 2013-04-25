package org.iplantc.core.uiapps.widgets.client.services.impl;

import org.iplantc.core.uiapps.widgets.client.services.DeployedComponentServices;
import org.iplantc.core.uicommons.client.DEServiceFacade;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.de.shared.SharedAuthenticationValidatingServiceFacade;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeployedComponentServicesImpl implements DeployedComponentServices {

    @Override
    public void getDeployedComponents(AsyncCallback<String> callback) {
        ServiceCallWrapper wrapper = new ServiceCallWrapper("org.iplantc.services.zoidberg.components"); //$NON-NLS-1$
        callService(callback, wrapper);
    }

    @Override
    public void searchDeployedComponents(String searchTerm, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl() + "search-deployed-components/" + URL.encodeQueryString(searchTerm);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    private void callService(AsyncCallback<String> callback, ServiceCallWrapper wrapper) {
        SharedAuthenticationValidatingServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

}
