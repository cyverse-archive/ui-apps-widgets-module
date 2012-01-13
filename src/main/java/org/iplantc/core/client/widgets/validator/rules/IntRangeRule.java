package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;

/**
 * Rule enforcing that a string is a valid integer and within a user-defined range.
 * 
 * @author amuir
 * 
 */
public class IntRangeRule extends IntegerRule {
    protected int high;
    protected int low;

    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    public IntRangeRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseParams(List<String> params) {
        if (params.size() > 1) {
            int first = Integer.parseInt(params.get(0));
            int second = Integer.parseInt(params.get(1));

            if (first > second) {
                high = first;
                low = second;
            } else {
                high = second;
                low = first;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(ComponentValueTable tbl, String name, String value) {
        String err = I18N.RULES.notValidIntegerMsg(name, low, high);

        if (ValidatorHelper.isInteger(value)) {
            err = I18N.RULES.notWithinRangeMsg(name, low, high);

            int val = Integer.parseInt(value);

            // are we within range?
            if (val >= low && val <= high) {
                // success
                err = null;
            }
        }

        return err;
    }
}
