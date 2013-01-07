package org.iplantc.core.client.widgets.appWizard.models;

import java.util.Arrays;

import org.iplantc.core.uicommons.client.validators.MaxDoubleValidator;
import org.iplantc.core.uicommons.client.validators.MaxIntegerValidator;
import org.iplantc.core.uicommons.client.validators.MinDoubleValidator;
import org.iplantc.core.uicommons.client.validators.MinIntegerValidator;
import org.iplantc.core.uicommons.client.validators.NameValidator3;

import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

public enum TemplateValidatorType {
    INT_RANGE("IntRange", null), /** {@link MinNumberValidator} and {@link MaxNumberValidator} */
    INT_ABOVE("IntAbove", MinIntegerValidator.class), /** {@link MinNumberValidator} */
    INT_BELOW("IntBelow", MaxIntegerValidator.class), /** {@link MaxNumberValidator} */
    DOUBLE_RANGE("DoubleRange", null), /** {@link MinNumberValidator} and {@link MaxNumberValidator} */
    DOUBLE_ABOVE("DoubleAbove", MinDoubleValidator.class), /** {@link MinNumberValidator} */
    DOUBLE_BELOW("DoubleBelow", MaxDoubleValidator.class), /** {@link MaxNumberValidator} */
    NON_EMPTY_CLASS("NonEmptyClass", null),
    GENOTYPE_NAME("GenotypeName", null),
 FILE_NAME("FileName", NameValidator3.class),
    INT_BELOW_FIELD("IntBelowField", null),
    INT_ABOVE_FIELD("IntAboveField", null),
    CLIPPER_DATA("ClipperData", null),
    MUST_CONTAIN("MustContain", null),
    SELECT_ONE_CHECKBOX("SelectOneCheckbox", null),
    REGEX("Regex", RegExValidator.class), // TEXT
    CHARACTER_LIMIT("CharacterLimit", MaxLengthValidator.class); // TEXT
    
    /**
     * The value which is received in JSON.
     */
    private String valueType;
    private Class<? extends Validator<?>> validator;
    
    private TemplateValidatorType(String valueType, Class<? extends Validator<?>> validator) {
        this.valueType = valueType;
        this.validator = validator;
    }
    
    @Override
    public String toString() {
        return valueType;
    }
    
    public static TemplateValidatorType getTypeByValue(String value){
        for(TemplateValidatorType type : Arrays.asList(values())){
            if(type.valueType.equals(value)){
                return type;
            }
        }
        return null;
    }
}
