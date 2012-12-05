package org.iplantc.core.client.widgets.appWizard.models;

import java.util.Arrays;

public enum TemplateValidatorType {
    INT_RANGE("IntRange"),
    INT_ABOVE("IntAbove"),
    INT_BELOW("IntBelow"),
    DOUBLE_RANGE("DoubleRange"),
    DOUBLE_ABOVE("DoubleAbove"),
    DOUBLE_BELOW("DoubleBelow"),
    NON_EMPTY_CLASS("NonEmptyClass"),
    GENOTYPE_NAME("GenotypeName"),
    FILE_NAME("FileName"),
    INT_BELOW_FIELD("IntBelowField"),
    INT_ABOVE_FIELD("IntAboveField"),
    CLIPPER_DATA("ClipperData"),
    MUST_CONTAIN("MustContain"),
    SELECT_ONE_CHECKBOX("SelectOneCheckbox"),
    REGEX("Regex"),
    CHARACTER_LIMIT("CharacterLimit");
    
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
