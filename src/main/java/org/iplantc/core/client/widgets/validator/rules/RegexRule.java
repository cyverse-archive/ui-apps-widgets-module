package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;

/**
 * Rule for verifying a string against a regular expression.
 * 
 * @author hariolf
 * 
 */
public class RegexRule extends IPlantRule {
    String regex;

    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    public RegexRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseParams(List<String> params) {
        regex = params.get(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(ComponentValueTable tbl, String name, String value) {
        if (value == null) {
            value = "";
        }

        boolean match = value.matches(regex);
        if (match) {
            return null;
        } else {
            return I18N.RULES.nonMatchingStringMsg(value, regex);
        }
    }
}
