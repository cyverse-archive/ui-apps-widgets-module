package org.iplantc.de.apps.widgets.client.models.metadata;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface DataSourceProperties extends PropertyAccess<DataSource> {

    ModelKeyProvider<DataSource> id();

    LabelProvider<DataSource> label();

    ValueProvider<DataSource, DataSourceEnum> type();
}
