package org.iplantc.core.uiapps.widgets.client.view.fields;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionArgument;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionProperties;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * XXX JDS Still not sure if this will stay a standalone class. Currently, it is a convenience constructor.
 
 * @author jstroot
 * 
 */
public class AppWizardComboBox extends ComboBox<SelectionArgument> {
    private final SelectionProperties props = GWT.create(SelectionProperties.class);
    private final ListStore<SelectionArgument> store = new ListStore<SelectionArgument>(props.id());

    ComboBox<SelectionArgument> field = new ComboBox<SelectionArgument>(store, props.display());

    public AppWizardComboBox(Argument argument) {
        this(GWT.<SelectionProperties> create(SelectionProperties.class));
        getStore().addAll(argument.getArguments());

    }

    protected AppWizardComboBox(SelectionProperties props) {
        super(new ListStore<SelectionArgument>(props.id()), props.display());
    }

}
