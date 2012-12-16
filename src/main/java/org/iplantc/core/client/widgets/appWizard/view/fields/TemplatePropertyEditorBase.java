package org.iplantc.core.client.widgets.appWizard.view.fields;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.view.editors.TemplatePropertyEditorAdapter;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.HasInvalidHandlers;
import com.sencha.gxt.widget.core.client.event.ValidEvent.HasValidHandlers;

/**
 * Interface definition for all TemplateProperty Editors.
 * 
 * It is highly suggested the <code>IsWidget</code> implementation return something which
 * implements <code>IsField</code>, since this is what will be added to the AppWizard
 * <code>FieldLabel</code>s.
 * 
 * @see TemplatePropertyEditorAdapter#setValue(TemplateProperty)
 * @author jstroot
 * 
 * @param <O> The object type which this editor is based on.
 */
public interface TemplatePropertyEditorBase extends Editor<Splittable>, HasInvalidHandlers, HasValidHandlers, IsWidget, TakesValue<Splittable> {
    
    
}
