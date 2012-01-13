package org.iplantc.core.client.widgets.validator;

import org.iplantc.core.client.widgets.I18N;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 * A validator to validate email address
 * 
 * @author sriram
 * 
 */
public class BasicEmailValidator implements Validator {

    @Override
    public String validate(Field<?> field, String value) {
        String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"; //$NON-NLS-1$

        if (value == null || !value.matches(emailPattern)) {
            return I18N.RULES.invalidEmail(field.getFieldLabel());
        }

        return null;
    }
}
