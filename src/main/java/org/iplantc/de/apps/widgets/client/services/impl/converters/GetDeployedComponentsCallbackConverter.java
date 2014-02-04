package org.iplantc.de.apps.widgets.client.services.impl.converters;

import java.util.List;

import org.iplantc.de.commons.client.models.deployedcomps.DeployedComponent;
import org.iplantc.de.commons.client.models.deployedcomps.DeployedComponentAutoBeanFactory;
import org.iplantc.de.commons.client.models.deployedcomps.DeployedComponentList;
import org.iplantc.de.commons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class GetDeployedComponentsCallbackConverter extends AsyncCallbackConverter<String, List<DeployedComponent>> {

    private final DeployedComponentAutoBeanFactory factory;

    public GetDeployedComponentsCallbackConverter(AsyncCallback<List<DeployedComponent>> callback, DeployedComponentAutoBeanFactory factory) {
        super(callback);
        this.factory = factory;
    }

    @Override
    protected List<DeployedComponent> convertFrom(String object) {
        /*Storage localStorege = Storage.getLocalStorageIfSupported();

        if (localStorege != null) {
            String dcStored = localStorege.getItem("deployedComponents");
            if (dcStored == null) {
                localStorege.setItem("deployedComponents", object);
            }
        }*/
        AutoBean<DeployedComponentList> autoBean = AutoBeanCodex.decode(factory, DeployedComponentList.class, object);
        List<DeployedComponent> items = autoBean.as().getDCList();
        return items;
    }

}
