package org.iplantc.de.apps.widgets.client.models.metadata;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

public interface FileInfoTypeList {

    @PropertyName("info_types")
    List<FileInfoType> getFileInfoTypes();
}
