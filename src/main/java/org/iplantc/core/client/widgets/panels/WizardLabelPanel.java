package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.metadata.client.property.Property;

import com.extjs.gxt.ui.client.widget.Label;

/**
 * Custom widget for displaying a label in a wizard.
 * 
 * @author amuir
 * 
 */
public class WizardLabelPanel extends WizardWidgetPanel {
    private Label caption;

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public WizardLabelPanel(final Property property, final ComponentValueTable tblComponentVals,
            final List<String> params) {
        super(property, tblComponentVals, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInstanceVariables(Property property, final List<String> params) {
        caption = new Label(property.getLabel());
        caption.setStyleAttribute("font-weight", "bold");
        setToolTip(caption, property);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setWidgetId(String id) {
        // intentionally left blank
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponentValueTable() {
        // intentionally left blank
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        add(caption);
    }

    @Override
    protected void setValue(String value) {
        caption.setText(value);
    }
}
