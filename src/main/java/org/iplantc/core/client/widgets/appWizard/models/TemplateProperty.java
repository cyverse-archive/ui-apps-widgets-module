package org.iplantc.core.client.widgets.appWizard.models;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasDescription;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasVisibility;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface TemplateProperty extends HasId, HasLabel, HasDescription, HasVisibility {
    
    @PropertyName("name")
    String getCmdLineOption();
    
    @PropertyName("name")
    void setCmdLineOption(String cmdLineOption);
    
    TemplatePropertyType getType();
    
    void setType(TemplatePropertyType type);
    
    int getOrder();
    
    void setOrder(int order);
    
    @PropertyName("omit_if_blank")
    boolean isOmitIfBlank();
    
    @PropertyName("omit_if_blank")
    void setOmitIfBlank(boolean omitIfBlank);
    
    String getDefaultValue();
    
    void setDefaultValue(String defaultValue);
    
    /**
     * XXX JDS This will need to be filled from the "Validator" key JSON object.
     * @return
     */
    boolean isRequired();
    
    void setRequired(boolean required);
    
    List<TemplateValidator> getValidators();
    
    void setValidators(List<TemplateValidator> validators);
    
}
