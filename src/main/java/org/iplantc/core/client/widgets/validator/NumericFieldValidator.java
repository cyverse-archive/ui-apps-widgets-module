package org.iplantc.core.client.widgets.validator;

import org.iplantc.core.client.widgets.I18N;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 * A validator class for validating fields thats accepts only numbers
 * 
 * @author sriram
 * 
 */
public class NumericFieldValidator implements Validator {

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(Field<?> field, String value) {
        String validChars = "0123456789";

        boolean flag = false;
        if (value != null && value.length() > 0) {
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if (validChars.indexOf(c) == -1) {
                    flag = true;
                    break;
                }
            }
        }
        if (flag) {
            return I18N.RULES.numbersOnly();
        } else {
            return null;
        }
    }

}
