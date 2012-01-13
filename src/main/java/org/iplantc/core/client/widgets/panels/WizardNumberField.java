package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.GeneralTextFormatter;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.client.widgets.validator.rules.IPlantRule;
import org.iplantc.core.client.widgets.validator.rules.IntCompareRule;
import org.iplantc.core.client.widgets.validator.rules.IntegerRule;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.NumberField;

public class WizardNumberField extends WizardWidgetPanel {

    protected NumberField entry;

    protected Label caption;
    private IPlantValidator validator;

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public WizardNumberField(final Property property, final ComponentValueTable tblComponentVals,
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

    private int determineWidth(final Property property, final List<String> params) {
        int ret = I18N.CONSTANTS.editBoxWidthNumber();

        if (params != null && !params.isEmpty()) {
            String width = params.get(0);

            if (ValidatorHelper.isInteger(width)) {
                ret = Integer.parseInt(width);
            }
        }

        return ret;
    }

    private void initNumberField(final Property property, final List<String> params) {
        entry = new NumberField();

        setWidth(determineWidth(property, params));

        entry.setWidth(width);
        if (property.getValue() != null && !property.getValue().isEmpty()) {
            try {
                entry.setValue(Integer.parseInt(property.getValue()));
            } catch (Exception e) {
                entry.setValue(Double.parseDouble(property.getValue()));
            }
        }
        entry.setSelectOnFocus(true);
        entry.setId(property.getId());
        setToolTip(entry, property);

        entry.addListener(Events.OnBlur, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent be) {
                updateComponentValueTable();

                // after we update the table, we need to validate the entire table
                tblComponentVals.validate();
            }
        });
        addValidationListener(entry);
    }

    /**
     * Helper function for compose() to display the text
     */
    protected void displayNumberField() {
        add(entry);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInstanceVariables(final Property property, final List<String> params) {
        caption = buildAsteriskLabel(property.getLabel());
        setToolTip(caption, property);
        initNumberField(property, params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setWidgetId(String id) {
        entry.setId(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initFormatters(final Property property) {
        tblComponentVals.setFormatter(property.getId(), new GeneralTextFormatter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInitialState(final Property property) {
        super.setInitialState(property);

        setNumberType();
    }

    private void setNumberType() {
        if (validator == null || entry == null) {
            return;
        }

        boolean allowDecimals = true;
        for (IPlantRule rule : validator.getRules()) {
            if (rule instanceof IntegerRule || rule instanceof IntCompareRule) {
                allowDecimals = false;
                break;
            }
        }

        entry.setAllowDecimals(allowDecimals);

        if (!allowDecimals) {
            entry.setPropertyEditorType(Integer.class);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initValidators(final Property property) {
        validator = buildValidator(property);

        if (validator != null) {
            entry.setValidator(validator);
            entry.setAutoValidate(true);
            entry.setValidateOnBlur(true);
            entry.setAllowBlank(!validator.isRequired());

            tblComponentVals.setValidator(property.getId(), validator);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponentValueTable() {
        String value = null;
        if (entry.getValue() != null) {
            value = entry.getValue().toString();
        }

        tblComponentVals.setValue(entry.getId(), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        setWidth("100%");
        add(caption);
        displayNumberField();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableValidators() {
        tblComponentVals.clearValidator(entry.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableValidators() {
        tblComponentVals.setValidator(entry.getId(), validator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        entry.validate();
    }
}
