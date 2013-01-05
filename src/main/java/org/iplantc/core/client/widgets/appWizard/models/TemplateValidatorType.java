package org.iplantc.core.client.widgets.appWizard.models;

import java.util.Arrays;

import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;

public enum TemplateValidatorType {
    INT_RANGE("IntRange"), /** {@link MinNumberValidator} and {@link MaxNumberValidator} */
    INT_ABOVE("IntAbove"), /** {@link MinNumberValidator} */
    INT_BELOW("IntBelow"), /** {@link MaxNumberValidator} */
    DOUBLE_RANGE("DoubleRange"), /** {@link MinNumberValidator} and {@link MaxNumberValidator} */
    DOUBLE_ABOVE("DoubleAbove"), /** {@link MinNumberValidator} */
    DOUBLE_BELOW("DoubleBelow"), /** {@link MaxNumberValidator} */
    NON_EMPTY_CLASS("NonEmptyClass"),
    GENOTYPE_NAME("GenotypeName"),
    FILE_NAME("FileName"),
    INT_BELOW_FIELD("IntBelowField"),
    INT_ABOVE_FIELD("IntAboveField"),
    CLIPPER_DATA("ClipperData"),
    MUST_CONTAIN("MustContain"),
    SELECT_ONE_CHECKBOX("SelectOneCheckbox"),
    REGEX("Regex"), // TEXT
    CHARACTER_LIMIT("CharacterLimit"); // TEXT
    
    /**
     * The value which is received in JSON.
     */
    private String valueType;
    
    private TemplateValidatorType(String valueType){
        this.valueType = valueType;
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
