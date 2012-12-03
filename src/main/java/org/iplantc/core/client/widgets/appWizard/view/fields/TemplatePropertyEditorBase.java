package org.iplantc.core.client.widgets.appWizard.view.fields;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.view.editors.TemplatePropertyEditorAdapter;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.HasInvalidHandlers;
import com.sencha.gxt.widget.core.client.event.ValidEvent.HasValidHandlers;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.Validator;

/**
 * Interface definition for all TemplateProperty Editors.
 * 
 * It is highly suggested the <code>IsWidget</code> implementation return something which
 * implements <code>IsField</code>, since this is what will be added to the AppWizard <code>FieldLabel</code>s.
 * 
 * @see TemplatePropertyEditorAdapter#setValue(TemplateProperty)
 * @author jstroot
 *
 */
public interface TemplatePropertyEditorBase extends Editor<TemplateProperty>, IsWidget, HasInvalidHandlers, HasValidHandlers{
    
    IsField<?> getField();
    
    void addValidator(Validator<String> validator);

}
