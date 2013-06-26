package org.iplantc.core.uiapps.widgets.client.services.impl;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;
import org.iplantc.core.uiapps.widgets.client.services.DeployedComponentServices;
import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponent;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

public class AppTemplateCallbackConverter extends AsyncCallbackConverter<String, AppTemplate> {

    private final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
    private final DeployedComponentServices dcServices = GWT.create(DeployedComponentServices.class);

    public AppTemplateCallbackConverter(AsyncCallback<AppTemplate> callback) {
        super(callback);
    }

    @Override
    public void onSuccess(String result) {
        final Splittable split = StringQuoter.split(result);

        /*
         * JDS If the result contains no "deployedComponent" definition, and the result contains a
         * DeployedComponent id, then search for the DeployedComponent and attach it to the result.
         */
        boolean hasDeployedComponentId = split.getPropertyKeys().contains("component_id");
        boolean hasDeployedComponent = split.getPropertyKeys().contains("deployedComponent");
        if (!hasDeployedComponent && hasDeployedComponentId && !Strings.isNullOrEmpty(split.get("component_id").asString())) {
            final Splittable deployedComponentId = split.get("component_id");

            dcServices.getDeployedComponents(new AsyncCallback<List<DeployedComponent>>() {

                @Override
                public void onSuccess(List<DeployedComponent> result) {
                    String asString = deployedComponentId.asString();
                    for (DeployedComponent dc : result) {
                        if (dc.getId().equalsIgnoreCase(asString)) {
                            Splittable dcSplittable = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(dc));
                            dcSplittable.assign(split, "deployedComponent");
                        }
                    }
                    AppTemplateCallbackConverter.super.onSuccess(split.getPayload());
                }

                @Override
                public void onFailure(Throwable caught) {
                    AppTemplateCallbackConverter.this.onFailure(caught);
                }
            });
        } else {
            super.onSuccess(split.getPayload());
        }
    }

    @Override
    protected AppTemplate convertFrom(String object) {

        Splittable split = StringQuoter.split(object);
        AutoBean<AppTemplate> atAb = AutoBeanCodex.decode(factory, AppTemplate.class, split);

        /*
         * JDS Grab TreeSelection argument type's original selectionItems, decode them as
         * SelectionItemGroup, and place them back in the Argument's selection items.
         */
        Splittable atGroups = split.get("groups");
        for (int i = 0; i < atGroups.size(); i++) {
            Splittable grp = atGroups.get(i);
            Splittable properties = grp.get("properties");
            if (properties == null) {
                continue;
            }
            for (int j = 0; j < properties.size(); j++) {
                Splittable arg = properties.get(j);
                Splittable type = arg.get("type");
                if (type.asString().equals(ArgumentType.TreeSelection.name())) {
                    Splittable arguments = arg.get("arguments");
                    if ((arguments != null) && (arguments.isIndexed()) && (arguments.size() > 0)) {
                        SelectionItemGroup sig = AutoBeanCodex.decode(factory, SelectionItemGroup.class, arguments.get(0)).as();
                        atAb.as().getArgumentGroups().get(i).getArguments().get(j).setSelectionItems(Lists.<SelectionItem> newArrayList(sig));
                    }
                }
            }
        }

        /*
         * JDS If any argument has a "defaultValue", forward it to the "value" field
         */
        for (ArgumentGroup ag : atAb.as().getArgumentGroups()) {
            for (Argument arg : ag.getArguments()) {
                if (arg.getDefaultValue() != null) {
                    arg.setValue(arg.getDefaultValue());
                }
            }
        }

        return atAb.as();
    }

}
