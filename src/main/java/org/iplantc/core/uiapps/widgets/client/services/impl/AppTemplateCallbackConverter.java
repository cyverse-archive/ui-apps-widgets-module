package org.iplantc.core.uiapps.widgets.client.services.impl;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class AppTemplateCallbackConverter extends AsyncCallbackConverter<String, AppTemplate> {

    private final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);

    public AppTemplateCallbackConverter(AsyncCallback<AppTemplate> callback) {
        super(callback);
    }

    @Override
    protected AppTemplate convertFrom(String object) {
        AutoBean<AppTemplate> atAb = AutoBeanCodex.decode(factory, AppTemplate.class, object);
        for(ArgumentGroup ag : atAb.as().getArgumentGroups()){
            for(Argument arg : ag.getArguments()){
                // JDS If we are dealing with Hierarchical Lists, we need to message the data a little.
                if (arg.getType().equals(ArgumentType.TreeSelection)) {
                    AppTemplateUtils.reserializeTreeSelectionArguments(arg);
                }
            }
        }
        return atAb.as();
    }

}
