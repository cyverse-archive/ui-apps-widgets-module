package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;

/**
 * Rule for enforcing that a number is a valid integer with a value below a user defined threshold.
 * 
 * @author sriram
 * 
 */
public class IntBelowRule extends IntegerRule {

    protected int top;
    
    public IntBelowRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseParams(List<String> params) {
        if (params.size() > 0) {
            top = Integer.parseInt(params.get(0));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(ComponentValueTable tbl, String name, String value) {
        String err = I18N.RULES.notValidIntegerBelowMsg(name, top);

        if (ValidatorHelper.isInteger(value)) {
            err = I18N.RULES.notBelowValueMsg(name, top);

            int val = Integer.parseInt(value);

            // are we within valid?
            if (val < top) {
                // success
                err = null;
            }
        }

        return err;
    }
}
