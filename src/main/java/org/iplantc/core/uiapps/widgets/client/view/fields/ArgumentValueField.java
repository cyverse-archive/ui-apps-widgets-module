package org.iplantc.core.uiapps.widgets.client.view.fields;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidator;

import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * Interface definition for all Argument Editors.
 * 
 * TODO Determine if this interface is necessary anymore
 */
public interface ArgumentValueField extends IsField<Splittable>, HasValueChangeHandlers<Splittable> {
    
    void setToolTipConfig(ToolTipConfig config);

    IsField<?> getField();

    /**
     * This method applies the given validators this object's field.
     * 
     * This method assumes that the each <code>ArgumentValidator</code> has the corresponding
     * {@link Validator} object contained in the <code>ArgumentValidator</code> autobean metadata.
     * 
     * @param validators
     */
    void applyValidators(List<ArgumentValidator> validators);

    List<EditorError> getErrors();
}
