package org.iplantc.core.client.widgets.appWizard.view.fields;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.view.editors.TemplatePropertyEditorAdapter;
import org.iplantc.core.uicommons.client.validators.HasValidators;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.HasInvalidHandlers;
import com.sencha.gxt.widget.core.client.event.ValidEvent.HasValidHandlers;

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
public interface TemplatePropertyEditorBase extends Editor<String>, HasValidators<String>, HasInvalidHandlers, HasValidHandlers, IsWidget{
    
//    IsField<?> getField();
    
}
