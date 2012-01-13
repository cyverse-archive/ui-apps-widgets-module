package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;

/**
 * Rule for enforcing that string is a valid integer above the integer value of another field.
 * 
 * @author amuir
 * 
 */
public class IntAboveFieldRule extends IntCompareRule {
    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    public IntAboveFieldRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String performComparison(String name, String value, String valDependant) {
        String ret = null; // assume success

        if (ValidatorHelper.isInteger(value) && ValidatorHelper.isInteger(valDependant)) {
            int lVal = Integer.parseInt(value);
            int rVal = Integer.parseInt(valDependant);

            if (lVal <= rVal) {
                ret = I18N.RULES.fieldGreaterThanMsg(name, nameDependent);
            }
        }

        return ret;
    }
}
