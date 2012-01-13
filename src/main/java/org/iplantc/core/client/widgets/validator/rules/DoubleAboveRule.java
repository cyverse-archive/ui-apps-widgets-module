package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;

/**
 * Rule for enforcing that a string is a valid double-precision number with a value above a user defined
 * threshold.
 * 
 * @author amuir
 * 
 */
public class DoubleAboveRule extends DoubleRule {
    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    public DoubleAboveRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(ComponentValueTable tbl, String name, String value) {
        String err = I18N.RULES.notValidDoubleAboveMsg(name, doubleValue);

        if (ValidatorHelper.isDouble(value)) {
            err = I18N.RULES.notAboveValueMsg(name, doubleValue);

            double val = Double.parseDouble(value);

            // are we within valid?
            if (val > doubleValue) {
                // success
                err = null;
            }
        }

        return err;
    }
}
