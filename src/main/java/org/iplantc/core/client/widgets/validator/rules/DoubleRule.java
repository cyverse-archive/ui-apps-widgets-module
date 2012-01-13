package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

/**
 * Rule for enforcing that a string is a valid double-precision number.
 * 
 * @author psarando
 * 
 */
public abstract class DoubleRule extends IPlantRule {
    protected double doubleValue;

    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */

    protected DoubleRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseParams(List<String> params) {
        if (params.size() > 0) {
            doubleValue = Double.parseDouble(params.get(0));
        }
    }

}
