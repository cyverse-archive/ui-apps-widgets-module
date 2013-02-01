package org.iplantc.core.widgets.client.appWizard.models.legacy;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasName;

public interface LegacyTemplateGroup extends HasId, HasName, HasLabel {

    String getType();

    void setType(String type);

    List<LegacyTemplateProperty> getProperties();

    void setProperties(List<LegacyTemplateProperty> properties);
}
