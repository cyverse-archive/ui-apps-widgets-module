package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;

/**
 * Base validation rule.
 * 
 * @author amuir
 * 
 */
public abstract class IPlantRule {
    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    protected IPlantRule(List<String> params) {
        parseParams(params);
    }

    /**
     * Parse context specific parameters from the string list.
     * 
     * @param params list of parameters to be used.
     */
    protected abstract void parseParams(List<String> params);

    /**
     * Perform validation.
     * 
     * @param tbl table of component values used for dependency validations.
     * @param name field name (for error messages).
     * @param value value to validate.
     * @return null on success. Error string on failure.
     */
    public abstract String validate(ComponentValueTable tbl, String name, String value);
}
