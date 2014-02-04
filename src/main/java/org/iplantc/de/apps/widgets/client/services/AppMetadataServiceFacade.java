package org.iplantc.de.apps.widgets.client.services;

import java.util.List;

import org.iplantc.de.apps.widgets.client.models.metadata.DataSource;
import org.iplantc.de.apps.widgets.client.models.metadata.DataSourceProperties;
import org.iplantc.de.apps.widgets.client.models.metadata.FileInfoType;
import org.iplantc.de.apps.widgets.client.models.metadata.FileInfoTypeProperties;
import org.iplantc.de.apps.widgets.client.models.metadata.ReferenceGenome;
import org.iplantc.de.apps.widgets.client.models.metadata.ReferenceGenomeProperties;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppMetadataServiceFacade {

    DataSourceProperties getDataSourceProperties();

    void getDataSources(AsyncCallback<List<DataSource>> callback);

    FileInfoTypeProperties getFileInfoTypeProperties();

    void getFileInfoTypes(AsyncCallback<List<FileInfoType>> callback);
    
    ReferenceGenomeProperties getReferenceGenomeProperties();

    void getReferenceGenomes(AsyncCallback<List<ReferenceGenome>> callback);

}
