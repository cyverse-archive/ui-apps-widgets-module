package org.iplantc.core.uiapps.widgets.client.view.fields;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemProperties;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToSelectionArgConverter;
import org.iplantc.core.uicommons.client.models.CommonModelUtils;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * This has to bind the ComboBox's Store to the Argument's selection Items.<br>
 * This has to bind the ComboBox's selection to the argument's value
 * 
 * @author jstroot
 * 
 */
public class AppWizardComboBox extends Composite implements ArgumentSelectionField {

    private final SelectionItemProperties props = GWT.<SelectionItemProperties> create(SelectionItemProperties.class);

    private final ListStore<SelectionItem> listStore;
    @Path("selectionItems")
    ListStoreEditor<SelectionItem> selectionItemsStoreBinder;

    private final ComboBox<SelectionItem> selectionItemsEditor;
    @Path("value")
    ConverterFieldAdapter<SelectionItem, ComboBox<SelectionItem>> valueEditor;

    public AppWizardComboBox() {
        // JDS Initialize list store, and its editor
        listStore = new ListStore<SelectionItem>(props.id());
        selectionItemsStoreBinder = new ListStoreEditor<SelectionItem>(listStore);

        // JDS Initialize combobox and its editor converter
        selectionItemsEditor = new ComboBox<SelectionItem>(listStore, props.displayLabel());
        selectionItemsEditor.setTriggerAction(TriggerAction.ALL);
        valueEditor = new ConverterFieldAdapter<SelectionItem, ComboBox<SelectionItem>>(selectionItemsEditor, new SplittableToSelectionArgConverter());
        initWidget(selectionItemsEditor);
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub
    }

    @Override
    public void setValue(Argument value) {
        if (value.getSelectionItems() != null) {
            selectionItemsStoreBinder.setValue(value.getSelectionItems());
        }

        // JDS Set default selection
        HasId hasId = CommonModelUtils.createHasIdFromSplittable(value.getDefaultValue());
        if (hasId != null) {
            for (SelectionItem si : listStore.getAll()) {
                if (si.getId().equals(hasId.getId())) {
                    selectionItemsEditor.select(si);
                    selectionItemsEditor.setValue(si);
                    break;
                }
            }

        } else if ((value.getSelectionItems() != null) && (value.getSelectionItems().size() > 0)) {
            for (SelectionItem si : value.getSelectionItems()) {
                if (si.isDefault()) {
                    selectionItemsEditor.select(si);
                    selectionItemsEditor.setValue(si);
                    break;
                }
            }
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<SelectionItem>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

}
