package org.iplantc.core.uiapps.widgets.client.services.impl;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
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
import com.google.web.bindery.autobean.shared.impl.HasSplittable;
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
        final Splittable deployedComponentId = split.get("component_id");
        Splittable deployedComponent = split.get("deployedComponent");
        /*
         * JDS If the result contains no "deployedComponent" definition, and the result contains a
         * DeployedComponent id, then search for the DeployedComponent and attach it to the result.
         */
        if (((deployedComponent == null) || deployedComponent.getPayload().isEmpty()) 
                && (deployedComponentId != null) && !Strings.isNullOrEmpty(deployedComponentId.asString())) {
            dcServices.getDeployedComponents(new AsyncCallback<List<DeployedComponent>>() {

                @Override
                public void onSuccess(List<DeployedComponent> result) {
                    String asString = deployedComponentId.asString();
                    for (DeployedComponent dc : result) {
                        if (dc.getId().equalsIgnoreCase(asString)) {
                            Splittable dcSplittable = ((HasSplittable)AutoBeanUtils.getAutoBean(dc)).getSplittable();
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
            for (int j = 0; j < grp.get("properties").size(); j++) {
                Splittable arg = grp.get("properties").get(j);
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

        return atAb.as();
    }

}
