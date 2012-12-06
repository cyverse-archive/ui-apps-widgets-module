package org.iplantc.core.client.widgets.appWizard.models;

import com.google.web.bindery.autobean.shared.Splittable;

public interface TemplateValidator {

    TemplateValidatorType getType();
    
    void setType(TemplateValidatorType type);
    
    Splittable getParams();
    
    void setParams(Splittable params);
}
