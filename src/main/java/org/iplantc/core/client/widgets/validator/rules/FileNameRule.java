package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;

/**
 * Rule for enforcing that a string be a valid filename.
 * 
 * @author amuir
 * 
 */
public class FileNameRule extends IPlantRule {
    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    public FileNameRule(List<String> params) {
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
        return test.matches("[A-Za-z 0-9_.]{4,24}");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(ComponentValueTable tbl, String name, String value) {
        String err = null; // assume success

        if (value == null || !isValidString(value)) {
            err = I18N.RULES.invalidFilenameMsg(name);
        }

        return err;
    }
}
