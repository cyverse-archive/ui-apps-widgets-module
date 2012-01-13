package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.metadata.client.property.Property;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.TableData;

/**
 * Custom widget for editing base pairs X variable.
 * 
 * @author amuir
 * 
 */
public class XBasePairsTextField extends WizardTextField {
    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public XBasePairsTextField(Property property, ComponentValueTable tblComponentVals,
            final List<String> params) {
        super(property, tblComponentVals, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void displayTextField() {
        HorizontalPanel pnl = new HorizontalPanel();

        TableData td = new TableData();
        td.setHorizontalAlign(HorizontalAlignment.RIGHT);

        pnl.add(new LabelField("X = "), td);
        pnl.add(entry, td);

        add(pnl);
    }
}
