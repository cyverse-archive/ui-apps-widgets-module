package org.iplantc.core.uiapps.widgets.client.services.impl.converters;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSource;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSourceList;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

public class DataSourceCallbackConverter extends AsyncCallbackConverter<String, List<DataSource>> {

    private final List<DataSource> dataSourceList;
    private final AppTemplateAutoBeanFactory factory;

    public DataSourceCallbackConverter(final List<DataSource> dataSourceList, final AppTemplateAutoBeanFactory factory, final AsyncCallback<List<DataSource>> callback) {
        super(callback);
        this.factory = factory;
        this.dataSourceList = dataSourceList;
    }

    @Override
    protected List<DataSource> convertFrom(String object) {
        Splittable split = StringQuoter.split(object);

        DataSourceList dsList = AutoBeanCodex.decode(factory, DataSourceList.class, split).as();
        dataSourceList.clear();
        dataSourceList.addAll(dsList.getDataSources());
        return dataSourceList;
    }

}
