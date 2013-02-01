package org.iplantc.core.widgets.client;

import com.google.gwt.core.client.GWT;

public class I18N {
    public static final WidgetConstants CONSTANTS = (WidgetConstants)GWT.create(WidgetConstants.class);
    public static final WidgetDisplayStrings DISPLAY = (WidgetDisplayStrings)GWT
            .create(WidgetDisplayStrings.class);
    public static final IPlantValidationMessages RULES = (IPlantValidationMessages)GWT
            .create(IPlantValidationMessages.class);
}
