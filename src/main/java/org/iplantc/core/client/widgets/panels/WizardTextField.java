package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.GeneralTextFormatter;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.TextField;

/**
 * Custom widget for displaying a text field in a wizard.
 * 
 * @author amuir
 * 
 */
public class WizardTextField extends WizardWidgetPanel {
    protected TextField<String> entry;

    protected Label caption;
    private IPlantValidator validator;

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public WizardTextField(final Property property, final ComponentValueTable tblComponentVals,
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
        int ret = I18N.CONSTANTS.editBoxWidthString();

        if (params != null && !params.isEmpty()) {
            String width = params.get(0);

            if (ValidatorHelper.isInteger(width)) {
                ret = Integer.parseInt(width);
            }
        }

        return ret;
    }

    private void initTextField(final Property property, final List<String> params) {
        entry = new TextField<String>();

        setWidth(determineWidth(property, params));

        entry.setWidth(width);

        String value = property.getValue();
        if (value != null && !value.isEmpty()) {
            entry.setValue(value);
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
    protected void displayTextField() {
        add(entry);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInstanceVariables(final Property property, final List<String> params) {
        caption = buildAsteriskLabel(property.getLabel());
        setToolTip(caption, property);
        initTextField(property, params);
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
    protected void initValidators(final Property property) {
        validator = buildValidator(property);

        if (validator != null) {
            entry.setValidator(validator);
            entry.setAutoValidate(true);
            entry.setAllowBlank(!validator.isRequired());

            tblComponentVals.setValidator(property.getId(), validator);
        }

        IPlantValidator.setRegexRestrictedArgValueChars(entry, property != null ? property.getLabel()
                : caption.getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponentValueTable() {
        tblComponentVals.setValue(entry.getId(), entry.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        setWidth("100%");
        add(caption);
        displayTextField();
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
