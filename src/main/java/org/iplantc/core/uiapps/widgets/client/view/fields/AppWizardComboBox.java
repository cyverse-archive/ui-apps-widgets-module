package org.iplantc.core.uiapps.widgets.client.view.fields;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionArgument;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionProperties;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * XXX JDS Still not sure if this will stay a standalone class. Currently, it is a convenience constructor.
 
 * @author jstroot
 * 
 */
public class AppWizardComboBox extends ComboBox<SelectionArgument> {

    public AppWizardComboBox(Argument argument) {
        this(GWT.<SelectionProperties> create(SelectionProperties.class));
        getStore().addAll(argument.getArguments());
        setTriggerAction(TriggerAction.ALL);

    }

    protected AppWizardComboBox(SelectionProperties props) {
        super(new ListStore<SelectionArgument>(props.id()), props.display());
    }

}
