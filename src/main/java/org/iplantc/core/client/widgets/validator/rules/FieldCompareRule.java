package org.iplantc.core.client.widgets.validator.rules;

import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;

/**
 * Base rule for comparing the value in one field with that in another.
 * 
 * @author amuir
 * 
 */
public abstract class FieldCompareRule extends IPlantRule {
    protected String idDependent;
    protected String nameDependent;

    /**
     * Instantiate from a parameter list.
     * 
     * @param params list of parameters to configure rule.
     */
    protected FieldCompareRule(List<String> params) {
        super(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void parseParams(List<String> params) {
        if (params.size() > 0) {
            idDependent = params.get(0);
        }

        nameDependent = (params.size() > 1) ? params.get(1) : "";
    }

    /**
     * Retrieve the value from our dependent widget.
     * 
     * @param tbl table of components to retrieve the dependent widget from.
     * @return dependent widgets value.
     */
    protected String getDependentValue(ComponentValueTable tbl) {
        String ret = null; // assume failure

        if (idDependent != null) {
            ret = tbl.getValue(idDependent);
        }

        return ret;
    }

    /**
     * Perform the comparison between our value and the dependent widget's value.
     * 
     * @param name field name.
     * @param value value to validate.
     * @param valDependant value of dependent field to validate against.
     * @return null on success. Error message on failure.
     */
    protected abstract String performComparison(String name, String value, String valDependant);

    /**
     * {@inheritDoc}
     */
    @Override
    public String validate(ComponentValueTable tblComponentVals, String name, String value) {
        String err = null; // assume success

        String valDependant = getDependentValue(tblComponentVals);

        if (valDependant != null) {
            err = performComparison(name, value, valDependant);
        }

        return err;
    }
}
