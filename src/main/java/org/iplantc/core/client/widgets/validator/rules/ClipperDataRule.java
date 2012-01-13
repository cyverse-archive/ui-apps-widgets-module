package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;

/**
 * Rule for enforcing that a string is valid clipper for 3' adapters.
 * 
 * @author amuir
 * 
 */
public class ClipperDataRule extends IPlantRule {
    /**
     * Instantiate from parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    public ClipperDataRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseParams(List<String> params) {
        // we ignore the params
    }

    private boolean isValidString(String test) {
        // only allow alphabetical characters and newlines.
        return test.matches("[A-Za-z\n]+");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(ComponentValueTable tbl, String name, String value) {
        String err = null; // assume success

        if (value == null || !isValidString(value)) {
            err = I18N.RULES.nonValidClipperDataMsg(name);
        }

        return err;
    }
}
