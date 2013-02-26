package org.iplantc.core.uiapps.widgets.client.appWizard.models.legacy;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface LegacyArgumentGroup extends HasId, HasName, HasLabel {

    String getType();

    void setType(String type);

    @PropertyName("properties")
    List<LegacyArgument> getArguments();

    @PropertyName("properties")
    void setArguments(List<LegacyArgument> arguments);
}
