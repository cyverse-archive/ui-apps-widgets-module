package org.iplantc.core.uiapps.widgets.client.view.fields;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * Interface definition for all Argument Editors.
 * 
 * TODO Determine if this interface is necessary anymore
 */
public interface ArgumentValueField extends IsField<Splittable>, HasValueChangeHandlers<Splittable> {
    
    void setToolTipConfig(ToolTipConfig config);

    IsField<?> getField();
}
