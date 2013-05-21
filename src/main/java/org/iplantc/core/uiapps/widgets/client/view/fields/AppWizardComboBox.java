package org.iplantc.core.uiapps.widgets.client.view.fields;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemProperties;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * XXX JDS Still not sure if this will stay a standalone class. Currently, it is a convenience constructor.
 
 * @author jstroot
 * 
 */
public class AppWizardComboBox extends ComboBox<SelectionItem> {

    public AppWizardComboBox(Argument argument) {
        this(GWT.<SelectionItemProperties> create(SelectionItemProperties.class));
        setTriggerAction(TriggerAction.ALL);
        if (argument.getSelectionItems() != null) {
            getStore().addAll(argument.getSelectionItems());
        }
    }

    protected AppWizardComboBox(SelectionItemProperties props) {
        super(new ListStore<SelectionItem>(props.id()), props.displayLabel());
    }

}
