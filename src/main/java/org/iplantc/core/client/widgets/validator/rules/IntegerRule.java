package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

/**
 * Rule for enforcing that string is a valid integer.
 * 
 * @author psarando
 * 
 */
public abstract class IntegerRule extends IPlantRule {

    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    protected IntegerRule(List<String> params) {
        super(params);
    }
}
