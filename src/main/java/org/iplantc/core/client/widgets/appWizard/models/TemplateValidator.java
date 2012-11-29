package org.iplantc.core.client.widgets.appWizard.models;

import java.util.List;

public interface TemplateValidator {

    TemplateValidatorType getType();
    
    void setType(TemplateValidatorType type);
    
    List<String> getParams();
    
    void setParams(List<String> params);
}
