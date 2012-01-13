package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;

/**
 * Rule for enforcing a string does not represent an empty JSON class.
 * 
 * @author amuir
 * 
 */
public class NonEmptyClassRule extends IPlantRule {
    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    public NonEmptyClassRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseParams(List<String> params) {
        // we ignore params
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(ComponentValueTable tbl, String name, String value) {
        String err = null; // assume success

        // this is a terrible way of validating whether a class is empty or not.
        // change this later.
        if (value == null || value.equals("{}")) {
            err = I18N.RULES.nonEmptyClassMsg(name);
        }

        return err;
    }
}
