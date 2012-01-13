package org.iplantc.core.client.widgets.utils;

import org.iplantc.core.client.widgets.validator.IPlantValidator;

import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

/**
 * Wrapper for the value of a wizard component.
 * 
 * @author amuir
 * 
 */
public class ComponentValue {
    private JSONValue value;
    protected IPlantValidator validator;
    protected ComponentValueExportFormatter formatter;
    private boolean exportable;

    protected ComponentValue(final String value) {
        setValue(value);
        // by default all values are exportable
        setExportable(true);
    }

    private ComponentValue(JSONValue value) {
        setValue(value);
        // by default all values are exportable
        setExportable(true);
    }

    /**
     * Allocate from a value string.
     * 
     * @param value initialization string.
     * @return newly created ComponentValue.
     */
    public static ComponentValue create(final String value) {
        return new ComponentValue(value);
    }

    /**
     * Allocate from a JSON value.
     * 
     * @param value initialization JSON value.
     * @return newly created ComponentValueJson.
     */
    public static ComponentValue create(JSONValue value) {
        return new ComponentValue(value);
    }

    /**
     * Set an object's value.
     * 
     * @param value string representation of value to set.
     */
    public void setValue(String value) {
        if (value == null) {
            value = "";
        }

        this.value = new JSONString(value);
    }

    /**
     * Sets this component's JSON value.
     * 
     * @param value string representation of value to set.
     */
    public void setValue(JSONValue value) {
        this.value = value;
    }

    /**
     * Retrieve an object's value
     * 
     * @return value field.
     */
    public String getValue() {
        if (value != null && value.isString() != null) {
            return value.isString().stringValue();
        }

        return value == null ? "" : value.toString();
    }

    /**
     * Retrieve of value for JSON export.
     * 
     * @return value formatted for export.
     */
    public JSONValue getExportValue() {
        JSONValue ret = value;

        if (formatter != null && ret != null) {
            ret = new JSONString(formatter.format(getValue()));
        }

        return ret;
    }

    /**
     * Set the validator for this value.
     * 
     * @param validator validator for field and form level validation.
     */
    public void setValidator(IPlantValidator validator) {
        this.validator = validator;
    }

    /**
     * Remove the current validator.
     */
    public void clearValidator() {
        validator = null;
    }

    /**
     * Set our export formatter.
     * 
     * @param formatter formatter for JSON export.
     */
    public void setFormatter(ComponentValueExportFormatter formatter) {
        this.formatter = formatter;
    }

    /**
     * Perform validation.
     * 
     * @return null on success. Error string on failure.
     */
    public String validate() {
        String ret = null; // assume success

        if (validator != null) {
            ret = validator.validate(getValue());
        }

        return ret;
    }

    /**
     * @param exportable the exportable to set
     */
    public void setExportable(boolean exportable) {
        this.exportable = exportable;
    }

    /**
     * @return the exportable
     */
    public boolean isExportable() {
        return exportable;
    }
}
