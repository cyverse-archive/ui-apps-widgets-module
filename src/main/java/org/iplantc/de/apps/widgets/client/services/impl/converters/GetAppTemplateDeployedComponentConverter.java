package org.iplantc.de.apps.widgets.client.services.impl.converters;

import org.iplantc.de.commons.client.models.deployedcomps.DeployedComponent;
import org.iplantc.de.commons.client.models.deployedcomps.DeployedComponentAutoBeanFactory;
import org.iplantc.de.commons.client.models.deployedcomps.DeployedComponentList;
import org.iplantc.de.commons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class GetAppTemplateDeployedComponentConverter extends AsyncCallbackConverter<String, DeployedComponent> {

    private final DeployedComponentAutoBeanFactory factory;

    public GetAppTemplateDeployedComponentConverter(AsyncCallback<DeployedComponent> callback, DeployedComponentAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected DeployedComponent convertFrom(String object) {

        AutoBean<DeployedComponentList> autoBean = AutoBeanCodex.decode(factory, DeployedComponentList.class, object);
        if ((autoBean.as().getDCList() != null) && !autoBean.as().getDCList().isEmpty()) {
            // JDS This converter is for an AppTemplate, which should only have 1 deployed component.
            return autoBean.as().getDCList().get(0);
        }
        return null;
    }

}
