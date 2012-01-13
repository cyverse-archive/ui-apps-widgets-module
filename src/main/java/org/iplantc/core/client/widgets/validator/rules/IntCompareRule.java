package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

/**
 * Rule for enforcing that string is a valid integer.
 * 
 * @author psarando
 * 
 */
public abstract class IntCompareRule extends FieldCompareRule {

    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    protected IntCompareRule(List<String> params) {
        super(params);
    }

}
