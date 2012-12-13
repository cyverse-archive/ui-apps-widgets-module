package org.iplantc.core.client.widgets.appWizard.models;

import java.util.List;

import org.iplantc.core.client.widgets.appWizard.presenter.AppWizardPresenterJsonAdapter;
import org.iplantc.core.uicommons.client.models.HasDescription;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasVisibility;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * This interface contains all the data required to assemble a single form field in an App Wizard UI.
 * 
 * Some fields require unique data which is not necessary to most of the other fields, and is held in
 * the generic "dataObject" property. {@link #dataObject()} returns a {@code Splittable}, which is
 * individually interpreted in the field corresponding to the value returned by {@link #getType()}.
 * This allows unique data to be stored for each field.
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
     * It is kept as a splittable in order to support multiple types, which
     * map one-to-one with the {@code getType()}.
     * 
     * @return
     */
    Splittable getValue();
    
    void setValue(Splittable formValue);
    
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
