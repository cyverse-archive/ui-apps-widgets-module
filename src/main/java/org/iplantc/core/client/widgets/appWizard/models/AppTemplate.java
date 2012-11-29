package org.iplantc.core.client.widgets.appWizard.models;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasName;

public interface AppTemplate extends HasId, HasLabel, HasName {
    
    List<TemplateGroup> getGroups();
    
    void setGroups(List<TemplateGroup> groups);

}
