package org.iplantc.core.uiapps.widgets.client.models;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface DataObject {

    @PropertyName("is_implicit")
    boolean isImplicit();

    @PropertyName("is_implicit")
    void setImplicit(boolean implicit);
}
