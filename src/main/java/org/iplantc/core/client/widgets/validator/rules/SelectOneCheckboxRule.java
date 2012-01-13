package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

/**
 * A class to validate that at least one of the two checkboxes is selected
 * 
 * @author sriram
 * 
 */
public class SelectOneCheckboxRule extends FieldCompareRule {

    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    public SelectOneCheckboxRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String performComparison(String name, String value, String valDependant) {
        String ret = null; // assume success

        boolean first = Boolean.parseBoolean(value);
        boolean second = Boolean.parseBoolean(valDependant);

        if (!first && !second) {
            // TODO: this string has to i18n
            ret = "Select atleast one checkbox";
        }

        return ret;
    }

}
