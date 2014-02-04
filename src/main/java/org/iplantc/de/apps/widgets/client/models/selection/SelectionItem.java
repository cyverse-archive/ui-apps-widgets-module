package org.iplantc.de.apps.widgets.client.models.selection;

import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import org.iplantc.de.commons.client.models.HasDescription;
import org.iplantc.de.commons.client.models.HasDisplayText;
import org.iplantc.de.commons.client.models.HasId;

public interface SelectionItem extends HasId, HasName, HasDescription, TakesValue<String>, HasDisplayText {

    String TO_BE_REMOVED = "List of sub items to be removed";

    void setId(String id);

    @PropertyName("isDefault")
    boolean isDefault();

    @PropertyName("isDefault")
    void setDefault(boolean isDefault);
}
