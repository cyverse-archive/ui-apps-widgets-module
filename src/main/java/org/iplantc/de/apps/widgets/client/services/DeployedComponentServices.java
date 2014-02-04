package org.iplantc.de.apps.widgets.client.services;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponent;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DeployedComponentServices {

    void getAppTemplateDeployedComponent(HasId appTemplateId, AsyncCallback<DeployedComponent> callback);

    void getDeployedComponents(AsyncCallback<List<DeployedComponent>> callback);

    void searchDeployedComponents(String searchTerm, AsyncCallback<List<DeployedComponent>> callback);
}
