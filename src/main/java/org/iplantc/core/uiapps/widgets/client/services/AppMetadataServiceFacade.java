package org.iplantc.core.uiapps.widgets.client.services;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSource;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSourceProperties;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoType;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoTypeProperties;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenome;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenomeProperties;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppMetadataServiceFacade {

    void getFileInfoTypes(AsyncCallback<List<FileInfoType>> callback);

    void getDataSources(AsyncCallback<List<DataSource>> callback);

    void getReferenceGenomes(AsyncCallback<List<ReferenceGenome>> callback);

    FileInfoTypeProperties getFileInfoTypeProperties();
    
    DataSourceProperties getDataSourceProperties();

    ReferenceGenomeProperties getReferenceGenomeProperties();

}
