package org.iplantc.core.client.widgets.validator;

import org.iplantc.core.client.widgets.I18N;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 * Verifies the validity of a job name.
 * 
 * @author sriram
 * 
 */
public class JobNameValidator implements Validator {
    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(Field<?> field, String value) {
        char[] punct = (IPlantValidator.RESTRICTED_CHARS_CMDLINE + "=").toCharArray(); //$NON-NLS-1$
        char[] arr = value.toCharArray();

        // check for spaces at the beginning and at the end of job name
        if (arr[0] == ' ' || arr[arr.length - 1] == ' ') {
            return I18N.RULES.analysisNameValidationMsg();
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < punct.length; j++) {
                if (arr[i] == punct[j]) {
                    return I18N.RULES.analysisNameValidationMsg();
                }
            }
        }
        return null;
    }
}
