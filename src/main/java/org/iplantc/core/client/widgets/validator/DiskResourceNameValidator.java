package org.iplantc.core.client.widgets.validator;

import org.iplantc.core.client.widgets.I18N;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 * Validates a file or folder name.
 * 
 * @author psarando
 * 
 */
public class DiskResourceNameValidator implements Validator {
    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(Field<?> field, String value) {
        // check for spaces at the beginning and at the end of the file name
        if (value.startsWith(" ") || value.endsWith(" ")) { //$NON-NLS-1$ //$NON-NLS-2$
            return I18N.RULES.analysisNameValidationMsg();
        }

        char[] restrictedChars = (IPlantValidator.RESTRICTED_CHARS_CMDLINE + "=").toCharArray(); //$NON-NLS-1$
        StringBuilder restrictedFound = new StringBuilder();

        for (char next : value.toCharArray()) {
            for (char restricted : restrictedChars) {
                if (next == restricted) {
                    restrictedFound.append(restricted);
                }
            }
        }

        if (restrictedFound.length() > 0) {
            return I18N.RULES.analysisNameValidationMsg() + " " //$NON-NLS-1$
                    + I18N.RULES.analysisNameInvalidChars(restrictedFound.toString());
        }

        return null;
    }
}
