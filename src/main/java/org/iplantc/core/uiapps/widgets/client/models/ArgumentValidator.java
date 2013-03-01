package org.iplantc.core.uiapps.widgets.client.models;

import com.google.web.bindery.autobean.shared.Splittable;

public interface ArgumentValidator {

    ArgumentValidatorType getType();
    
    void setType(ArgumentValidatorType type);
    
    Splittable getParams();
    
    void setParams(Splittable params);
}
