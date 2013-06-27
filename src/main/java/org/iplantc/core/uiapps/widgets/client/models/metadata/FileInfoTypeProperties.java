package org.iplantc.core.uiapps.widgets.client.models.metadata;

import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface FileInfoTypeProperties extends PropertyAccess<FileInfoType> {

    ModelKeyProvider<FileInfoType> id();

    LabelProvider<FileInfoType> label();

}
