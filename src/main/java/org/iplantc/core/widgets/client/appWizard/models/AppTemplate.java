package org.iplantc.core.widgets.client.appWizard.models;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * This object contains all the information required to assemble a representative App UI wizard.
 * Additionally, it will also be used to hold the user data entered via the wizard forms.
 * 
 * @author jstroot
 * 
 */
public interface AppTemplate extends HasId, HasLabel, HasName {
    
    @PropertyName("groups")
    List<TemplateGroup> getGroups();
    
    @PropertyName("groups")
    void setGroups(List<TemplateGroup> groups);

}
