package org.iplantc.core.client.widgets.appWizard.models;

import java.util.List;

import org.iplantc.core.client.widgets.appWizard.presenter.AppWizardPresenterJsonAdapter;
import org.iplantc.core.uicommons.client.models.HasDescription;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasVisibility;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * 
 * @author jstroot
 * @see AppWizardPresenterJsonAdapter
 */
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
    
    @PropertyName("value")
    String getDefaultValue();
    
    @PropertyName("value")
    void setDefaultValue(String defaultValue);
    
    /**
     * A property used for holding the values entered via the form fields. 
     * This property is meant for use at runtime, and is not intended to 
     * be serialized or deserialized.
     * @return
     */
    String getFormValue();
    
    void setFormValue(String formValue);
    
    boolean isRequired();
    
    void setRequired(boolean required);
    
    List<TemplateValidator> getValidators();
    
    void setValidators(List<TemplateValidator> validators);
    
    @Override
    @PropertyName("isVisible")
    boolean isVisible();
    
    @Override
    @PropertyName("isVisible")
    void setVisible(boolean visible);
    
}
