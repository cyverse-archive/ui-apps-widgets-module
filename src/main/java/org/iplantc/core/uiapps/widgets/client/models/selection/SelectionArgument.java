package org.iplantc.core.uiapps.widgets.client.models.selection;

import org.iplantc.core.uicommons.client.models.HasDescription;
import org.iplantc.core.uicommons.client.models.HasDisplayText;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface SelectionArgument extends HasId, HasName, HasDescription, TakesValue<String>, HasDisplayText {

    void setId(String id);

    @PropertyName("isDefault")
    boolean isDefault();

    @PropertyName("isDefault")
    void setDefault(boolean isDefault);
}
