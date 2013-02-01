package org.iplantc.core.widgets.client.appWizard.view.fields;

import org.iplantc.core.widgets.client.appWizard.models.Argument;
import org.iplantc.core.widgets.client.appWizard.view.editors.ArgumentEditorAdapter;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.HasInvalidHandlers;
import com.sencha.gxt.widget.core.client.event.ValidEvent.HasValidHandlers;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * Interface definition for all Argument Editors.
 * 
 * It is highly suggested the <code>IsWidget</code> implementation return something which
 * implements <code>IsField</code>, since this is what will be added to the AppWizard
 * <code>FieldLabel</code>s.
 * 
 * @see ArgumentEditorAdapter#setValue(Argument)
 * @author jstroot
 * 
 * @param <E> The type of validator which the property editor supports.
 */
public interface ArgumentField extends Editor<Splittable>, HasInvalidHandlers, HasValidHandlers, IsWidget, TakesValue<Splittable> {
    
    /**
     * Initializes the property editor. This includes applying any validators specified by the input
     * parameter.
     * 
     * @param property
     */
    void initialize(Argument property);

    void setToolTip(ToolTipConfig toolTip);

    // JDS May need to return a list of available validators.

}
