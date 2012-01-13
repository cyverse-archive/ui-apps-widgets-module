package org.iplantc.core.client.widgets;

import com.google.gwt.i18n.client.Messages;

/**
 * Localized strings for validation messages.
 * 
 * @author lenards
 * 
 */
public interface IPlantValidationMessages extends Messages {

    /**
     * Message provided when a user enters a value that is not a double precision value.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.DoubleRangeRule
     * 
     * @param field the name of the field or parameter that the value is for.
     * @param low the lower bound of the range
     * @param high the upper bound of the range
     * @return a parameterized string representing the message shown when the field is not valid.
     */
    public String notValidDoubleMsg(String field, Double low, Double high);

    /**
     * Message provided when a user enters a value that is not a double precision value.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.DoubleAboveRule
     * 
     * @param field the name of the field or parameter that the value is associated with.
     * @param bottom the lower bound that all values must be above
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String notValidDoubleAboveMsg(String field, Double bottom);

    /**
     * Message provided when a user enters a value that is not a double precision value.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.DoubleBelowRule
     * 
     * @param field the name of the field or parameter that the value is associated with.
     * @param bottom the lower bound that all values must be below
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String notValidDoubleBelowMsg(String field, Double bottom);

    /**
     * Message provided when a user enters a value that is not an integer value.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.IntRangeRule
     * 
     * @param field the name of the field or parameter that the value is for.
     * @param low the lower bound of the range
     * @param high the upper bound of the range
     * @return a parameterized string representing the message shown when the field is not valid.
     */
    public String notValidIntegerMsg(String field, Integer low, Integer high);

    /**
     * Message provided when a user enters a value that is not within range.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.DoubleRangeRule
     * @see org.iplantc.core.client.widgets.validator.rules.IntRangeRule
     * 
     * @param field the name of the field or parameter that the value is for.
     * @param low the lower bound of the range
     * @param high the upper bound of the range
     * @return a parameterized string representing the message shown when the field is not valid.
     */
    public String notWithinRangeMsg(String field, Number low, Number high);

    /**
     * Message provided when a user enters a value that is not an integer value.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.IntAboveRule
     * 
     * @param field the name of the field or parameter that the value is associated with.
     * @param bottom the lower bound that all values must be above
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String notValidIntegerAboveMsg(String field, Integer bottom);

    /**
     * Message provided when a user enters an integer value that is not above the lower bound.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.IntAboveRule
     * 
     * @param field the name of the field or parameter that the value is associated with.
     * @param bottom the lower bound that all values must be above
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String notAboveValueMsg(String field, Integer bottom);

    /**
     * Message provided when a user enters an double-precision value that is not above the lower bound.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.DoubleAboveRule
     * 
     * @param field the name of the field or parameter that the value is associated with.
     * @param bottom the lower bound that all values must be above
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String notAboveValueMsg(String field, Double bottom);

    /**
     * Message provided when a user enters an double-precision value that is not above the lower bound.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.DoubleBelowRule
     * 
     * @param field the name of the field or parameter that the value is associated with.
     * @param bottom the lower bound that all values must be below
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String notBelowValueMsg(String field, Double bottom);

    /**
     * Message provided when a user enters an empty array.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.IntAboveRule
     * 
     * @param field the name of the field or parameter that the value is associated with.
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String nonEmptyArrayMsg(String field);

    /**
     * Message provided when a user enters an empty class.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.NonEmptyClassRule
     * 
     * @param field the name of the field or parameter that the value is associated with.
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String nonEmptyClassMsg(String field);

    /**
     * Message provided when a user enters a string with characters that aren't A-Z, a-z or supported
     * special characters.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.GenotypeNameRule
     * 
     * @param field the name of the field that the value is associated with
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String nonValidGenotypeNameMsg(String field);

    /**
     * Message provided when a user enters a string with characters that aren't A-Z, a-z space or
     * underscore
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.FileNameRule
     * 
     * @param field the name of the field that the value is associated with
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String invalidFilenameMsg(String field);

    /**
     * Message provided when a user enters a field that is less than a dependent field.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.IntAboveFieldRule
     * @param field the name of the field that the value is associated with
     * @param fldDependant the name of the field this field is dependent on
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String fieldLessThanMsg(String field, String fldDependant);

    /**
     * Message provided when a user enters a field that is greater than a dependent field.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.IntAboveFieldRule
     * @param field the name of the field that the value is associated with
     * @param fldDependant the name of the field this field is dependent on
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String fieldGreaterThanMsg(String field, String fldDependant);

    /**
     * Message provided when a user enters a field that is not valid 3' adapter clipper. data.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.ClipperDataRule
     * @param field the name of the field that the value is associated with.
     * @return a parameterized string representing the message shown when the field is not valid.
     */
    public String nonValidClipperDataMsg(String field);

    /**
     * Localized text for display as validation message.
     * 
     * This message is displayed with a Job Name is not valid.
     * 
     * @return a string representing the localized text.
     */
    String jobNameValidationMsg();

    /**
     * Validation message for numeric field
     * 
     * @return a String representing the text.
     */
    String numbersOnly();

    /**
     * Validation message for regex.
     * 
     * @param value the value entered by the user
     * @param pattern the pattern the value should match
     * @return a String representing the text.
     */
    String nonMatchingStringMsg(String value, String pattern);
    
   /** Message provided when a user enters a field that is less than a dependent field.
    * 
    * @see org.iplantc.core.client.widgets.validator.rules.IntAboveFieldRule
    * @param field the name of the field that the value is associated with
    * @param characted limit
    * @return a parameterized string representing the message shown when the field is not valid
    */
    String characterLimitExceeedMsg(String field, int limit);

    /**
     * Message provided when a user enters a value that is not an integer value.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.IntBelowRule
     * 
     * @param field the name of the field or parameter that the value is associated with.
     * @param bottom the upper bound that all values must be above
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String notValidIntegerBelowMsg(String name, int bottom);

    /**
     * Message provided when a user enters a value that is not below an integer value.
     * 
     * @see org.iplantc.core.client.widgets.validator.rules.IntBelowRule
     * 
     * @param field the name of the field or parameter that the value is associated with.
     * @param bottom the upper bound that all values must be above
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String notBelowValueMsg(String name, int bottom);
    
    /**
     * Message provided when a user enters a value that has invalid characters.
     * 
     * @param name the name of the field or parameter that the value is associated with.
     * @param invalidCharacterSet The set of invalid characters to display to the user
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String invalidCharacterMsg(String name, String invalidCharacterSet);

    /**
     * Message provided when a user enters an invalid email address.
     *
     * @param name the name of the field or parameter that the value is associated with.
     * @return a parameterized string representing the message shown when the field is not valid
     */
    public String invalidEmail(String name);
}
