package org.iplantc.core.uiapps.widgets.client.view.fields;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentEditorAdapter;

import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.form.IsField;
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
 * @param <U>
 * 
 * @param <E> The type of validator which the property editor supports.
 */
public interface ArgumentField extends IsField<Splittable> {
    
    void setToolTipConfig(ToolTipConfig config);

    // JDS May need to return a list of available validators.

}
