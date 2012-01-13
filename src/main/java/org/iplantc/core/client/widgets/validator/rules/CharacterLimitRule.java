package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;

/**
 * 
 * A validator class to validate maximum characters allowed rule in a widget.
 * 
 * @author sriram
 *
 */
public class CharacterLimitRule extends IPlantRule {

    protected int limit;
    
    public CharacterLimitRule(List<String> params) {
        super(params);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void parseParams(List<String> params) {
        if (params.size() > 0) {
            limit = Integer.parseInt(params.get(0));
        }

    }

    @Override
    public String validate(ComponentValueTable tbl, String name, String value) {
        String err = I18N.RULES.characterLimitExceeedMsg(name, limit);

        if (value != null && value.length() <= limit) {
            return null;
        }

        return err;
    }

}
