package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;

/**
 * Rule for enforcing a string is a double and that it is between a user-defined range of values.
 * 
 * @author amuir
 * 
 */
public class DoubleRangeRule extends DoubleRule {
    protected double high;
    protected double low;

    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    public DoubleRangeRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseParams(List<String> params) {
        if (params.size() > 1) {
            double first = Double.parseDouble(params.get(0));
            double second = Double.parseDouble(params.get(1));

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
        String err = I18N.RULES.notValidDoubleMsg(name, low, high);

        if (ValidatorHelper.isDouble(value)) {
            err = I18N.RULES.notWithinRangeMsg(name, low, high);

            double test = Double.parseDouble(value);

            // are we within range?
            if (test >= low && test <= high) {
                // success
                err = null;
            }
        }

        return err;
    }
}
