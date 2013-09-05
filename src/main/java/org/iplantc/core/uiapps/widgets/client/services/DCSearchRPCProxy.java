package org.iplantc.core.uiapps.widgets.client.services;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.widgets.client.services.impl.DeployedComponentServicesImpl;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.deployedcomps.DeployedComponent;

import com.google.common.base.Strings;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class DCSearchRPCProxy extends
        RpcProxy<FilterPagingLoadConfig, PagingLoadResult<DeployedComponent>> {

    private String lastQueryText = ""; //$NON-NLS-1$

    DeployedComponentServices dcService = new DeployedComponentServicesImpl();

    public String getLastQuery() {
        return lastQueryText;
    }

    @Override
    public void load(FilterPagingLoadConfig loadConfig,
            final AsyncCallback<PagingLoadResult<DeployedComponent>> callback) {
        // Get the proxy's search params.
        List<FilterConfig> filterConfigs = loadConfig.getFilters();
        if (filterConfigs != null && !filterConfigs.isEmpty()) {
            lastQueryText = filterConfigs.get(0).getValue();
        }

        if (!Strings.isNullOrEmpty(lastQueryText)) {
            dcService.searchDeployedComponents(lastQueryText,
                    new AsyncCallback<List<DeployedComponent>>() {

                        @Override
                        public void onSuccess(List<DeployedComponent> result) {
                            callback.onSuccess(new PagingLoadResultBean<DeployedComponent>(result,
                                    result.size(), 0));
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            ErrorHandler.post(caught);
                        }
                    });
        } else {

            dcService.getDeployedComponents(new AsyncCallback<List<DeployedComponent>>() {

                @Override
                public void onFailure(Throwable caught) {
                    ErrorHandler.post(I18N.ERROR.dcLoadError(), caught);

                }

                @Override
                public void onSuccess(List<DeployedComponent> result) {
                    callback.onSuccess(new PagingLoadResultBean<DeployedComponent>(result,
                            result.size(), 0));
                }

            });
        }

    }

}
