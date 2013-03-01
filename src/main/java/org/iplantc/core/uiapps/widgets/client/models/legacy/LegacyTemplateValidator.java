package org.iplantc.core.uiapps.widgets.client.models.legacy;

import java.util.List;

import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.Splittable;

public interface LegacyTemplateValidator extends HasId, HasName, HasLabel {

    boolean isRequired();

    void setRequired(boolean required);

    List<Splittable> getRules();

    void setRules(List<Splittable> rules);
}
