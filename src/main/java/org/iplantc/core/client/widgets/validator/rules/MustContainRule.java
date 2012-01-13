package org.iplantc.core.client.widgets.validator.rules;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;

/**
 * this validator always succeeds. This may change in the future.
 */
public class MustContainRule extends IPlantRule {
    private List<String> items;

    /**
     * Instantiate from parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    public MustContainRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseParams(List<String> params) {
        items = new ArrayList<String>(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(ComponentValueTable tbl, String name, String value) {
        // this validator always succeeds. This may change in the future, but currently
        // we only use this validator to initialize list boxes.
        return null;
    }

    /**
     * Retrieve list of items.
     * 
     * @return our items.
     */
    public List<String> getItems() {
        return items;
    }
}
