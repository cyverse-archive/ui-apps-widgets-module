package org.iplantc.core.client.widgets.validator;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.factory.IPlantRuleFactory;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.validator.rules.IPlantRule;
import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;
import org.iplantc.core.uicommons.client.util.RegExp;

import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Validator;

/**
 * Class used for wizard field and form level validation.
 * 
 * @author amuir
 * 
 */
public class IPlantValidator implements Validator {
    public static final String RESTRICTED_CHARS_CMDLINE_ARG_VALUE = "&;<>`~\n"; //$NON-NLS-1$
    public static final String RESTRICTED_CHARS_CMDLINE = "!\"#$'%()*+,/\\:?@[]^{}|\t" //$NON-NLS-1$
            + RESTRICTED_CHARS_CMDLINE_ARG_VALUE;

    private final String name;
    private final boolean isRequired;
    private final List<IPlantRule> rules;
    private final ComponentValueTable tblComponentVals;

    /**
     * Instantiate from a component value table and meta data template.
     * 
     * @param tblComponentVals component value table for validation.
     * @param validatorIn meta data validator.
     */
    public IPlantValidator(ComponentValueTable tblComponentVals, MetaDataValidator validatorIn) {
        this.tblComponentVals = tblComponentVals;

        rules = new ArrayList<IPlantRule>();

        name = validatorIn.getName();
        isRequired = validatorIn.isRequired();
        addRules(validatorIn);
    }

    private void addRules(MetaDataValidator validatorIn) {
        List<MetaDataRule> rulesIn = validatorIn.getRules();

        for (MetaDataRule rule : rulesIn) {
            IPlantRule add = IPlantRuleFactory.build(rule);

            if (add != null) {
                rules.add(add);
            }
        }
    }

    private String buildErrorString(final List<String> errors) {
        String ret = new String();
        boolean firstPass = true;

        for (String error : errors) {
            if (firstPass) {
                firstPass = false;
            } else {
                ret += "\n"; //$NON-NLS-1$
            }

            ret += error;
        }

        return ret;
    }

    private String buildRequiredFieldError() {
        return name + " is a required field.";
    }

    private String validateRequired(String value) {
        String ret = null; // assume success

        if (isRequired) {
            // null is treated the same as empty when it comes to validation
            if (value == null) {
                ret = buildRequiredFieldError();
            } else {
                String test = value.trim();

                if (test.isEmpty()) {
                    ret = buildRequiredFieldError();
                }
            }
        }

        return ret;
    }

    /**
     * Validates a string against required flag and rules.
     * 
     * @param value value to validate.
     * @return null on success. Error string on failure.
     */
    public String validate(final String value) {
        List<String> errors = new ArrayList<String>();

        // if this is a required field, it must have a value
        String error = validateRequired(value);
        // if we have an empty mandatory field, add our error
        if (error != null) {
            errors.add(error);
        } else {
            // validate only if required or value is not null and not empty
            if (isRequired || (value != null && !value.isEmpty())) {
                for (IPlantRule rule : rules) {
                    error = rule.validate(tblComponentVals, name, value);

                    if (error != null) {
                        errors.add(error);
                    }
                }
            }
        }

        return errors.isEmpty() ? null : buildErrorString(errors);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(Field<?> field, String value) {
        return validate(value);
    }

    /**
     * Retrieve required flag.
     * 
     * @return whether this field is required for form validation.
     */
    public boolean isRequired() {
        return isRequired;
    }

    /**
     * Retrieve IPlant rules generated from the meta-data rules from the given validator.
     * 
     * @return a list of IPlant rules.
     */
    public List<IPlantRule> getRules() {
        return rules;
    }

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
