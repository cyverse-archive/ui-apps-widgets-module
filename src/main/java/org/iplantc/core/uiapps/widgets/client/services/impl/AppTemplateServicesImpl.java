package org.iplantc.core.uiapps.widgets.client.services.impl;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSource;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSourceProperties;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoType;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoTypeProperties;
import org.iplantc.core.uiapps.widgets.client.models.metadata.JobExecution;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.services.AppTemplateServices;
import org.iplantc.core.uiapps.widgets.client.services.DeployedComponentServices;
import org.iplantc.core.uicommons.client.DEServiceFacade;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.de.shared.SharedServiceFacade;
import org.iplantc.de.shared.services.BaseServiceCallWrapper.Type;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

public class AppTemplateServicesImpl implements AppTemplateServices, AppMetadataServiceFacade {
    private final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
    private final DeployedComponentServices dcServices = GWT.create(DeployedComponentServices.class);
    private final FileInfoTypeProperties fileInfoTypeProperties = GWT.create(FileInfoTypeProperties.class);
    private final DataSourceProperties dataSourceProperties = GWT.create(DataSourceProperties.class);
    private final List<FileInfoType> fileInfoTypeList = Lists.newArrayList();
    private final List<DataSource> dataSourceList = Lists.newArrayList();

    @Override
    public void getAppTemplate(HasId appId, AsyncCallback<AppTemplate> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() 
                + "app/" + appId.getId(); //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        DEServiceFacade.getInstance().getServiceData(wrapper, new AppTemplateCallbackConverter(factory, dcServices, callback));
    }

    @Override
    public void getAppTemplateForEdit(HasId appId, AsyncCallback<AppTemplate> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl()
                + "edit-app/" + appId.getId(); //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        DEServiceFacade.getInstance().getServiceData(wrapper, new AppTemplateCallbackConverter(factory, dcServices, callback));
    }

    @Override
    public void saveAndPublishAppTemplate(AppTemplate at, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() 
                + "update-app"; //$NON-NLS-1$
        Splittable split = appTemplateToSplittable(at);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.PUT, 
                address, split.getPayload());
        callSecuredService(callback, wrapper);
    }
    
    @Override
    public void getAppTemplatePreview(AppTemplate at, AsyncCallback<AppTemplate> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl() 
                + "preview-template"; //$NON-NLS-1$
        Splittable split = appTemplateToSplittable(at);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.POST, 
                address, split.getPayload());
        DEServiceFacade.getInstance().getServiceData(wrapper, new AppTemplateCallbackConverter(factory, dcServices, callback));
    }

    @Override
    public void rerunAnalysis(HasId analysisId, AsyncCallback<AppTemplate> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl() 
                + "app-rerun-info/" + analysisId.getId(); //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(ServiceCallWrapper.Type.GET, address);

        DEServiceFacade.getInstance().getServiceData(wrapper, new AppTemplateCallbackConverter(factory, dcServices, callback));
    }

    @Override
    public void cmdLinePreview(AppTemplate at, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl()
               + "arg-preview"; //$NON-NLS-1$
        AppTemplate copy = AppTemplateUtils.copyAppTemplate(at);
        // JDS Transform any Argument's value which contains a full SelectionItem obj to the
        // SelectionItem's value
        for (ArgumentGroup ag : copy.getArgumentGroups()) {
            for (Argument arg : ag.getArguments()) {
                if (AppTemplateUtils.isSimpleSelectionArgumentType(arg.getType())) {

                    if ((arg.getValue() != null) && arg.getValue().isKeyed() && !arg.getValue().isUndefined("value")) {
                        arg.setValue(arg.getValue().get("value"));
                    } else {
                        arg.setValue(null);
                    }
                } else if (arg.getType().equals(ArgumentType.TreeSelection)) {
                    if ((arg.getSelectionItems() != null) && (arg.getSelectionItems().size() == 1)) {
                        SelectionItemGroup sig = AppTemplateUtils.selectionItemToSelectionItemGroup(arg.getSelectionItems().get(0));
                        List<SelectionItem> siList = AppTemplateUtils.getSelectedTreeItems(sig);
                        String retVal = "";
                        for (SelectionItem si : siList) {
                            if (si.getValue() != null) {
                                retVal += si.getValue() + " ";
                            }
                        }
                        arg.setValue(StringQuoter.create(retVal.trim()));
                    }
                } else if (arg.getType().equals(ArgumentType.EnvironmentVariable)) {
                    // Exclude environment variables from the command line
                    arg.setValue(null);
                    arg.setName("");
                }
            }
        }
        Splittable split = appTemplateToSplittable(copy);
        String payload = split.getPayload();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.POST, 
                address, payload);
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public void launchAnalysis(AppTemplate at, JobExecution je, AsyncCallback<String> callback) {
        String address = DEProperties.getInstance().getMuleServiceBaseUrl() 
                + "workspaces/" + je.getWorkspaceId() + "/newexperiment"; //$NON-NLS-1$
        Splittable split = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(je));
        Splittable configSplit = StringQuoter.createSplittable();
        for (ArgumentGroup ag : at.getArgumentGroups()) {
            for (Argument arg : ag.getArguments()) {
                Splittable value = arg.getValue();
                if ((value == null) && !arg.getType().equals(ArgumentType.TreeSelection)) {
                    continue;
                }
                if (AppTemplateUtils.isSimpleSelectionArgumentType(arg.getType())) {
                    value.assign(configSplit, arg.getId());
                } else if (AppTemplateUtils.isDiskResourceArgumentType(arg.getType()) && !arg.getType().equals(ArgumentType.MultiFileSelector)) {
                    value.get("id").assign(configSplit, arg.getId());
                } else if (arg.getType().equals(ArgumentType.MultiFileSelector) && value.isIndexed()) {
                    value.assign(configSplit, arg.getId());
                } else if (arg.getType().equals(ArgumentType.TreeSelection) && (arg.getSelectionItems() != null) && (arg.getSelectionItems().size() == 1)) {
                    SelectionItemGroup sig = AppTemplateUtils.selectionItemToSelectionItemGroup(arg.getSelectionItems().get(0));
                    Splittable sigSplit = AppTemplateUtils.getSelectedTreeItemsAsSplittable(sig);
                    sigSplit.assign(configSplit, arg.getId());
                } else {
                    value.assign(configSplit, arg.getId());
                }

            }
        }
        configSplit.assign(split, "config");
        GWT.log("LaunchAnalysis Json:\n" + JsonUtil.prettyPrint(split));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(Type.PUT, address, split.getPayload());
        DEServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public void getFileInfoTypes(AsyncCallback<List<FileInfoType>> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl() + "get-workflow-elements/info-types";
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);

        if (!fileInfoTypeList.isEmpty()) {
            callback.onSuccess(fileInfoTypeList);
            return;
        }

        DEServiceFacade.getInstance().getServiceData(wrapper, new FileInfoTypeCallbackConverter(fileInfoTypeList, factory, callback));

    }

    @Override
    public void getDataSources(AsyncCallback<List<DataSource>> callback) {
        String address = DEProperties.getInstance().getUnproctedMuleServiceBaseUrl() + "get-workflow-elements/data-sources"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        if (!dataSourceList.isEmpty()) {
            callback.onSuccess(dataSourceList);
            return;
        }

        DEServiceFacade.getInstance().getServiceData(wrapper, new DataSourceCallbackConverter(dataSourceList, factory, callback));

    }

    private Splittable appTemplateToSplittable(AppTemplate at){
        Splittable ret = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(at));
        if(at.getDeployedComponent() != null){
            StringQuoter.create(at.getDeployedComponent().getId()).assign(ret, "component_id");
        }
        // JDS Convert Argument.getValue() which contain any selected/checked *Selection types to only
        // contain their value.
        for (ArgumentGroup ag : at.getArgumentGroups()) {
            for (Argument arg : ag.getArguments()) {
                if (arg.getType().equals(ArgumentType.TreeSelection)) {
                    if ((arg.getSelectionItems() != null) && (arg.getSelectionItems().size() == 1)) {
                        SelectionItemGroup sig = AppTemplateUtils.selectionItemToSelectionItemGroup(arg.getSelectionItems().get(0));
                        Splittable split = AppTemplateUtils.getSelectedTreeItemsAsSplittable(sig);
                        arg.setValue(split);
                    }
                }
            }
        }
        return ret;
    }

    private void callSecuredService(AsyncCallback<String> callback, ServiceCallWrapper wrapper) {
        SharedServiceFacade.getInstance().getServiceData(wrapper, callback);
    }

    @Override
    public FileInfoTypeProperties getFileInfoTypeProperties() {
        return fileInfoTypeProperties;
    }

    @Override
    public DataSourceProperties getDataSourceProperties() {
        return dataSourceProperties;
    }

}
