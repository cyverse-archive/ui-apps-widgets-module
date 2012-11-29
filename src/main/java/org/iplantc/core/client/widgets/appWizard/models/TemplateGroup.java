package org.iplantc.core.client.widgets.appWizard.models;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

public interface TemplateGroup extends HasId, HasLabel{
    
    List<TemplateGroup> getGroups();
    
    void setGroups(List<TemplateGroup> groups);
    
    List<TemplateProperty> getProperties();
    
    void setProperties(List<TemplateProperty> properties);

}
