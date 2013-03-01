package org.iplantc.core.uiapps.widgets.client.models.legacy;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasName;

public interface LegacyAppTemplate extends HasId, HasName, HasLabel {

    String getType();

    void setType(String type);

    List<LegacyArgumentGroup> getGroups();

    void setGroups(List<LegacyArgumentGroup> groups);
}
