package org.iplantc.core.uiapps.widgets.client.view.fields;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;

import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.IsWidget;

public interface ArgumentSelectionField extends ValueAwareEditor<Argument>, IsWidget, HasValueChangeHandlers<List<SelectionItem>>, TakesValue<Argument> {

}
