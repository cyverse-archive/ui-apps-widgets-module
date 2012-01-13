package org.iplantc.core.client.widgets.panels;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.metadata.client.property.Property;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.TableData;

/**
 * Custom widget to properly display percentage entry to the user
 * 
 * @author amuir
 * 
 */
public class PercentEntryPanel extends WizardTextField {
    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public PercentEntryPanel(final Property property, final ComponentValueTable tblComponentVals) {
        super(property, tblComponentVals, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void displayTextField() {
        HorizontalPanel pnl = new HorizontalPanel();

        TableData td = new TableData();
        td.setPadding(3);
        td.setHorizontalAlign(HorizontalAlignment.RIGHT);

        entry.setWidth(36);
        pnl.add(caption, td);
        pnl.add(entry, td);
        pnl.add(new LabelField("%"), td);

        add(pnl);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        setWidth("100%");
        displayTextField();
    }
}
