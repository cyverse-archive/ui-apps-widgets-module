package org.iplantc.core.widgets.client.appWizard.models.legacy;

import org.iplantc.core.uicommons.client.models.HasDescription;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface LegacyArgument extends HasId, HasName, HasLabel, HasDescription, HasVisibility, TakesValue<String> {

    @PropertyName("isVisible")
    @Override
    boolean isVisible();

    @PropertyName("isVisible")
    @Override
    void setVisible(boolean visible);
}
