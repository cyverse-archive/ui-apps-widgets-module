package org.iplantc.core.uiapps.widgets.client.view.fields;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;

import com.google.gwt.editor.client.CompositeEditor;
import com.google.gwt.editor.client.CompositeEditor.EditorChain;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.IsWidget;

public interface ArgumentSelectionField extends ValueAwareEditor<Argument>, IsWidget, HasValueChangeHandlers<List<SelectionItem>> {

    /**
     * This method is called from {@link CompositeEditor}s to manually set the bound list. This is due to
     * the fact that the {@link EditorChain} does not descend into child widgets created within
     * composites.
     * 
     * @param value
     */
    void setList(Argument value);

}
