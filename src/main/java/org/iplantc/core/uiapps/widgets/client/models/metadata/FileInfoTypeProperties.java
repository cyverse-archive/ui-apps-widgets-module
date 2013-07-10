package org.iplantc.core.uiapps.widgets.client.models.metadata;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface FileInfoTypeProperties extends PropertyAccess<FileInfoType> {

    ModelKeyProvider<FileInfoType> id();

    LabelProvider<FileInfoType> label();

    @Path("label")
    ValueProvider<FileInfoType, String> labelValue();

}
