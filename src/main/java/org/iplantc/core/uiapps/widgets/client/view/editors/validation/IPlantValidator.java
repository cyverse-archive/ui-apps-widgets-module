package org.iplantc.core.uiapps.widgets.client.view.editors.validation;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.util.RegExp;

import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.form.TextField;

/**
 * Class used for wizard field validation of command line characters.
 * 
 * @author psarando
 * 
 */
public class IPlantValidator {
    public static final String RESTRICTED_CHARS_CMDLINE_ARG_VALUE = "&;<>`~\n"; //$NON-NLS-1$
    public static final String RESTRICTED_CHARS_CMDLINE = "!\"#$'%()*+,/\\:?@[]^{}|\t" //$NON-NLS-1$
            + RESTRICTED_CHARS_CMDLINE_ARG_VALUE;


    public static void setRegexRestrictedCmdLineChars(TextField<?> field, String fieldDisplayName) {
        setRegexRestrictedChars(RESTRICTED_CHARS_CMDLINE, field, fieldDisplayName);
    }

    public static void setRegexRestrictedArgValueChars(TextField<?> field, String fieldDisplayName) {
        setRegexRestrictedChars(RESTRICTED_CHARS_CMDLINE_ARG_VALUE, field, fieldDisplayName);
    }

    private static void setRegexRestrictedChars(String invalidCharSet, TextField<?> field,
            String fieldDisplayName) {
        if (field == null) {
            return;
        }

        if (fieldDisplayName == null) {
            fieldDisplayName = ""; //$NON-NLS-1$
        }

        String invalidCharSetRegex = buildRestrictedCharSetRegex(invalidCharSet);
        String invalidCharSetDisplay = I18N.RULES.invalidCharacterMsg(fieldDisplayName, invalidCharSet
                .replaceAll("\t", "(tab)").replaceAll("\n", "(newline)")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        field.setRegex(invalidCharSetRegex);
        field.getMessages().setRegexText(invalidCharSetDisplay);
    }

    public static String buildRestrictedCmdLineCharSetRegex() {
        return buildRestrictedCharSetRegex(RESTRICTED_CHARS_CMDLINE);
    }

    public static String buildRestrictedArgValueCharSetRegex() {
        return buildRestrictedCharSetRegex(RESTRICTED_CHARS_CMDLINE_ARG_VALUE);
    }

    private static String buildRestrictedCharSetRegex(String invalidCharSet) {
        return Format.substitute("[^{0}]*", RegExp.escapeCharacterClassSet(invalidCharSet)); //$NON-NLS-1$
    }
}
