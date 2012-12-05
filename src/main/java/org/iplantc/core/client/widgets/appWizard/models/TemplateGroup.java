package org.iplantc.core.client.widgets.appWizard.models;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasName;

public interface TemplateGroup extends HasId, HasLabel, HasName{
    
    List<TemplateGroup> getGroups();
    
    void setGroups(List<TemplateGroup> groups);
    
    List<TemplateProperty> getProperties();
    
    void setProperties(List<TemplateProperty> properties);

}
