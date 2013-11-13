package org.iplantc.core.uiapps.widgets.client.view.editors.arguments;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;

public final class ClearComboBoxSelectionKeyDownHandler implements KeyDownHandler, BeforeSelectionHandler<SelectionItem> {
    private final ComboBox<SelectionItem> comboBox;
    private final ListStore<SelectionItem> listStore;

    public ClearComboBoxSelectionKeyDownHandler(ComboBox<SelectionItem> selectionItemsComboBox) {
        this.listStore = selectionItemsComboBox.getStore();
        this.comboBox = selectionItemsComboBox;
    }

    @Override
    public void onBeforeSelection(BeforeSelectionEvent<SelectionItem> event) {
        if (comboBox.getCurrentValue() != null) {
            comboBox.getCurrentValue().setDefault(false);
        }
        if (event.getItem() != null) {
            event.getItem().setDefault(true);
        }

    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
            // FIXME CORE-4806 Deselection of list arguments disabled pending CORE-XXXX (CREATE TICKET
            // FOR THIS)
            /*for (SelectionItem si : listStore.getAll()) {
                si.setDefault(false);
            }
            comboBox.clear();
            ValueChangeEvent.fire(comboBox, null);*/
        }
    }
}