package org.iplantc.core.uiapps.widgets.client.models;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface ArgumentGroup extends HasId, HasLabel, HasName{
    
    @PropertyName("properties")
    List<Argument> getArguments();
    
    @PropertyName("properties")
    void setArguments(List<Argument> arguments);

}
