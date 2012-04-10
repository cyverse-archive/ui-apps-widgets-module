package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;

/**
 * Custom widget for displaying a checkbox in a wizard.
 * 
 * @author amuir
 * 
 */
public class WizardCheckboxPanel extends WizardWidgetPanel {
    private CheckBox checkbox;
    private IPlantValidator validator;

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public WizardCheckboxPanel(final Property property, final ComponentValueTable tblComponentVals,
            final List<String> params) {
        super(property, tblComponentVals, params);
    }

    private IPlantValidator buildValidator(final Property property) {
        IPlantValidator ret = null; // assume failure
        MetaDataValidator mdv = property.getValidator();

        if (mdv != null) {
            ret = new IPlantValidator(tblComponentVals, mdv);
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initValidators(final Property property) {
        validator = buildValidator(property);

        if (validator != null) {
            tblComponentVals.setValidator(property.getId(), validator);
        }
    }

    /**
     * Set the format of the checkbox's box label.
     * 
     * @param caption text to be displayed as a caption for the panel.
     * @return a string representing the caption text.
     */
    protected String buildBoxLabel(final String caption) {
        return caption;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInstanceVariables(final Property property, final List<String> params) {
        checkbox = new CheckBox();

        setToolTip(checkbox, property);
        checkbox.setBoxLabel(buildBoxLabel(property.getLabel()));

        // we need to update our value table on change
        checkbox.addListener(Events.Change, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent be) {
                updateComponentValueTable();
                // after we update the table, we need to validate the entire table
                tblComponentVals.validate();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setWidgetId(String id) {
        checkbox.setId(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInitialState(final Property property) {
        checkbox.setValue(Boolean.parseBoolean(property.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponentValueTable() {
        tblComponentVals.setValue(checkbox.getId(), Boolean.toString(checkbox.getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        add(checkbox);
    }

    @Override
    protected void setValue(String value) {
        if (value != null && !value.isEmpty()) {
            checkbox.setValue(Boolean.parseBoolean(value));
        }

    }
}
