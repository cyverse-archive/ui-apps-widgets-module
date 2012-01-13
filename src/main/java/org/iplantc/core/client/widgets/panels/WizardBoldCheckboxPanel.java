package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.metadata.client.property.Property;

/**
 * Custom widget for displaying a bold checkbox in a wizard.
 * 
 * @author amuir
 * 
 */
public class WizardBoldCheckboxPanel extends WizardCheckboxPanel {
    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public WizardBoldCheckboxPanel(Property property, ComponentValueTable tblComponentVals,
            List<String> params) {
        super(property, tblComponentVals, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String buildBoxLabel(final String caption) {
        return "<b>" + caption + "</b>";
    }
}
