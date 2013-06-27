package org.iplantc.core.uiapps.widgets.client.services;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSource;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSourceProperties;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoType;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoTypeProperties;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppMetadataServiceFacade {

    void getFileInfoTypes(AsyncCallback<List<FileInfoType>> callback);

    void getDataSources(AsyncCallback<List<DataSource>> callback);

    FileInfoTypeProperties getFileInfoTypeProperties();
    
    DataSourceProperties getDataSourceProperties();
}
