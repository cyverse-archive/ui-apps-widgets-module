package org.iplantc.core.uiapps.widgets.client.models;

import org.iplantc.core.uicommons.client.models.HasId;

import com.google.web.bindery.autobean.shared.Splittable;

public interface ArgumentValidator extends HasId {

    void setId(String id);

    ArgumentValidatorType getType();
    
    void setType(ArgumentValidatorType type);
    
    Splittable getParams();
    
    void setParams(Splittable params);
}
