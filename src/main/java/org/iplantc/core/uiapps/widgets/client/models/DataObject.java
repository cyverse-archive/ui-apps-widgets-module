package org.iplantc.core.uiapps.widgets.client.models;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface DataObject {

    @PropertyName("file_info_type")
    FileInfoType getFileInfoType();
    
    @PropertyName("file_info_type")
    void setFileInfoType(FileInfoType fileInfoType);
    
    @PropertyName("data_source")
    String getDataSource();

    @PropertyName("data_source")
    void setDataSource(String dataSource);

    boolean isRetain();

    void setRetain(boolean retain);

    String getFormat();

    void setFormat(String format);

    @PropertyName("is_implicit")
    boolean isImplicit();

    @PropertyName("is_implicit")
    void setImplicit(boolean implicit);

    void setCmdSwitch(String string);

    String getCmdSwitch();
}
