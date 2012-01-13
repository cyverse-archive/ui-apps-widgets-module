package org.iplantc.core.client.widgets.utils;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;

import com.google.gwt.json.client.JSONObject;

/**
 * Collection of utility methods used by validator objects.
 * 
 * @author amuir
 * 
 */
public class ValidatorHelper {
    /**
     * Determines whether a given string is a valid integer
     * 
     * @param test string to test.
     * @return true if tested string is a valid integer representation.
     */
    public static boolean isInteger(String test) {
        try {
            Integer.parseInt(test);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    /**
     * Determines whether a given string is a valid double
     * 
     * @param test string test.
     * @return true if tested string is a valid double representation.
     */
    public static boolean isDouble(String test) {
        boolean ret = false; // assume failure

        try {
            if (test != null) {
                Double.parseDouble(test);

                // if we get here, we know parseDouble succeeded
                ret = true;
            }
        } catch (NumberFormatException nfe) {
            // we are assuming false - setting the return value here would be redundant
        }

        return ret;
    }

    /**
     * Builds a MetaDataValidator from a JSON string.
     * 
     * @param json template of meta data validator.
     * @return newly allocated meta data validator. Null on failure.
     */
    public static MetaDataValidator buildValidator(String json) {
        MetaDataValidator ret = null; // assume failure

        if (json != null) {
            JSONObject objJson = JsonUtil.getObject(json);
            ret = new MetaDataValidator(objJson);
        }

        return ret;
    }
}
