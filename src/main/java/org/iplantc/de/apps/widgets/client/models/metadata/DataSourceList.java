package org.iplantc.de.apps.widgets.client.models.metadata;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface DataSourceList {

    @PropertyName("data_sources")
    List<DataSource> getDataSources();
}
