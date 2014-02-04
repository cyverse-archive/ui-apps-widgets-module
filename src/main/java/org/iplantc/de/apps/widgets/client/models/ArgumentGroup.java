package org.iplantc.de.apps.widgets.client.models;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import org.iplantc.de.commons.client.models.HasId;
import org.iplantc.de.commons.client.models.HasLabel;

import java.util.List;

public interface ArgumentGroup extends HasId, HasLabel, HasName{
    /**
     * A constant for annotating autobeans as newly created or not. Typically used via
     * {@link AutoBean#setTag(String, Object)}.
     */
    String IS_NEW = "new argument group";
    
    @PropertyName("properties")
    List<Argument> getArguments();
    
    @PropertyName("properties")
    void setArguments(List<Argument> arguments);

}

