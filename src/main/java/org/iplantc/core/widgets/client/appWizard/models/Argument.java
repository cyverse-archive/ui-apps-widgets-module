package org.iplantc.core.widgets.client.appWizard.models;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasDescription;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;
import org.iplantc.core.widgets.client.appWizard.models.selection.SelectionArgument;
import org.iplantc.core.widgets.client.appWizard.presenter.AppWizardPresenterJsonAdapter;

import com.google.gwt.user.client.ui.HasVisibility;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * This interface contains all the data required to assemble a single form field in an App Wizard UI.
 * 
 * @author jstroot
 * @see AppWizardPresenterJsonAdapter
 */
public interface Argument extends HasId, HasLabel, HasDescription, HasVisibility {
    
    @PropertyName("name")
    String getCmdLineOption();
    
    @PropertyName("name")
    void setCmdLineOption(String cmdLineOption);
    
    ArgumentType getType();

    void setType(ArgumentType type);

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
    @PropertyName("formValue")
    Splittable getValue();
    
    @PropertyName("formValue")
    void setValue(Splittable formValue);
    
    boolean isRequired();
    
    void setRequired(boolean required);
    
    List<ArgumentValidator> getValidators();
    
    void setValidators(List<ArgumentValidator> validators);
    
    @Override
    @PropertyName("isVisible")
    boolean isVisible();
    
    @Override
    @PropertyName("isVisible")
    void setVisible(boolean visible);
    
    /*
     * right now the only extra information we need is the combo list stuff.
     * it's just a pain to parse since it is held in the same place as the validators
     */

    List<SelectionArgument> getArguments();

    void setArguments(List<SelectionArgument> arguments);

}
