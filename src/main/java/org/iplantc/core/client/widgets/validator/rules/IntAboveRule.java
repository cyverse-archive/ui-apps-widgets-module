package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;

/**
 * Rule for enforcing that a string is a valid integer with a value above a user defined threshold.
 * 
 * @author amuir
 * 
 */
public class IntAboveRule extends IntegerRule {
    protected int bottom;

    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    public IntAboveRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseParams(List<String> params) {
        if (params.size() > 0) {
            bottom = Integer.parseInt(params.get(0));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(ComponentValueTable tbl, String name, String value) {
        String err = I18N.RULES.notValidIntegerAboveMsg(name, bottom);

        if (ValidatorHelper.isInteger(value)) {
            err = I18N.RULES.notAboveValueMsg(name, bottom);

            int val = Integer.parseInt(value);

            // are we within valid?
            if (val > bottom) {
                // success
                err = null;
            }
        }

        return err;
    }
}
