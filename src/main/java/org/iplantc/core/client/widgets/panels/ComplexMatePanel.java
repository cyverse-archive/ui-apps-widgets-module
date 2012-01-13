package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.GeneralTextFormatter;
import org.iplantc.core.client.widgets.utils.IDiskResourceSelectorBuilder;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.metadata.client.property.Property;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;

/**
 * Mate panel for G2P Variant Detection. Handles expected inner distance and inner distance standard
 * deviation.
 * 
 * @author amuir
 * 
 */
public class ComplexMatePanel extends SimpleMatePanel {
    private final String EXPECTED_INNER_DISTANCE_ID = "expectedInnerDistance";
    private final String INNER_DISTANCE_DEVIATION_ID = "innerDistanceStdDev";

    private TextField<String> fldExpected;
    private TextField<String> fldDeviant;

    private IPlantValidator validatorExpected;
    private IPlantValidator validatorDeviant;

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public ComplexMatePanel(Property property, ComponentValueTable tblComponentVals,
            final List<String> params, IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        super(property, tblComponentVals, params, diskResourceSelectorBuilder);
    }

    private TextField<String> buildTextField(String id, String value, IPlantValidator validator) {
        TextField<String> ret = new TextField<String>();

        ret.setId(id);
        ret.setValue(value);
        ret.setWidth(54);
        ret.setSelectOnFocus(true);
        ret.setAllowBlank(false);

        ret.addListener(Events.OnBlur, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent be) {
                updateComponentValueTable();
                tblComponentVals.validate();
            }
        });
        addValidationListener(ret);

        return ret;
    }

    private VerticalPanel buildTextFieldPanel(String caption, TextField<String> field) {
        VerticalPanel ret = new VerticalPanel();

        ret.add(new Label(caption));
        ret.add(field);

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateValidators(boolean enabled) {
        super.updateValidators(enabled);

        if (enabled) {
            // set our validators if the user checks the 'has mate pair' checkbox
            tblComponentVals.setValidator(EXPECTED_INNER_DISTANCE_ID, validatorExpected);
            tblComponentVals.setValidator(INNER_DISTANCE_DEVIATION_ID, validatorDeviant);
        } else {
            // if the user does not have a mate pair, we do not need to validate
            tblComponentVals.clearValidator(EXPECTED_INNER_DISTANCE_ID);
            tblComponentVals.clearValidator(INNER_DISTANCE_DEVIATION_ID);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addSupportingComponentVals() {
        super.addSupportingComponentVals();

        tblComponentVals.setValue(EXPECTED_INNER_DISTANCE_ID, fldExpected.getValue());
        tblComponentVals.setFormatter(EXPECTED_INNER_DISTANCE_ID, new GeneralTextFormatter());

        tblComponentVals.setValue(INNER_DISTANCE_DEVIATION_ID, fldDeviant.getValue());
        tblComponentVals.setFormatter(INNER_DISTANCE_DEVIATION_ID, new GeneralTextFormatter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInstanceVariables(final Property property, final List<String> params) {
        super.initInstanceVariables(property, params);

        fldExpected = buildTextField(EXPECTED_INNER_DISTANCE_ID, "200", validatorExpected);
        fldDeviant = buildTextField(INNER_DISTANCE_DEVIATION_ID, "10", validatorDeviant);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void composeMatePanel() {
        final String COLON = ":";
        VerticalPanel panelInner = new VerticalPanel();

        panelInner.setSpacing(5);
        panelInner.addStyleName("accordianbody");
        panelInner.setWidth(400);

        panelInner.add(new Label(I18N.DISPLAY.matePairSelection() + COLON));
        panelInner.add(fileSelector.getWidget());

        panelInner.add(buildTextFieldPanel(I18N.DISPLAY.expectedInnerDistance() + COLON, fldExpected));
        panelInner
                .add(buildTextFieldPanel(I18N.DISPLAY.expectedDistanceDeviation() + COLON, fldDeviant));

        pnlMate.add(panelInner);
    }

    private void setFieldValidator(TextField<String> field, IPlantValidator validator) {
        field.setAutoValidate(true);
        field.setValidator(validator);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initValidators(final Property property) {
        super.initValidators(property);

        String json = "{\"name\" : \"Expected inner distance field\", \"required\": true, \"rules\": [{\"IntAbove\" : [-1]}]}";
        validatorExpected = new IPlantValidator(tblComponentVals, ValidatorHelper.buildValidator(json));
        setFieldValidator(fldExpected, validatorExpected);

        json = "{\"name\" : \"Inner distance deviation field\", \"required\": true, \"rules\": [{\"IntAbove\" : [-1]}]}";
        validatorDeviant = new IPlantValidator(tblComponentVals, ValidatorHelper.buildValidator(json));
        setFieldValidator(fldDeviant, validatorDeviant);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponentValueTable() {
        super.updateComponentValueTable();

        tblComponentVals.setValue(EXPECTED_INNER_DISTANCE_ID, fldExpected.getValue());
        tblComponentVals.setValue(INNER_DISTANCE_DEVIATION_ID, fldDeviant.getValue());
    }
}
