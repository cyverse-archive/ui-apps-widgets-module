package org.iplantc.core.client.widgets.appWizard.models.selection;

import org.iplantc.core.uicommons.client.models.HasDescription;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasText;
import com.google.web.bindery.autobean.shared.Splittable;

public interface TemplatePropertySelection {

    Splittable getArguments();

    Splittable getGroups();

    <T extends HasText & HasId & HasName & HasDescription> T getThing();
}