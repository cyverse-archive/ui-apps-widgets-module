package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.factory.IPlantRuleFactory;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.client.widgets.validator.rules.IPlantRule;
import org.iplantc.core.client.widgets.validator.rules.MustContainRule;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataRule;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;

/**
 * A WizardWidgetPanel with methods common to List Selector type widgets.
 * 
 * @author psarando
 * 
 */
public abstract class WizardSelectorPanel extends WizardWidgetPanel {

    /**
     * Instantiate from a property, component value table.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     */
    public WizardSelectorPanel(Property property, ComponentValueTable tblComponentVals) {
        super(property, tblComponentVals, null);
    }

    /**
     * Gets the MustContainRule values required to populate the selector widget.
     * 
     * @param property The property with the MustContainRule.
     * @return A list of MustContainRule values.
     */
    protected List<String> getMustContainRuleItems(Property property) {
        // do we have a validator?
        if (property != null && property.getValidator() != null) {
            MetaDataValidator validator = property.getValidator();

            List<MetaDataRule> rules = validator.getRules();

            for (MetaDataRule mdr : rules) {
                IPlantRule first = IPlantRuleFactory.build(mdr);

                // is the first rule the type we are looking for?
                if (first != null && first instanceof MustContainRule) {
                    MustContainRule source = (MustContainRule)first;
                    return source.getItems();
                }
            }
        }

        return null;
    }

    /**
     * Builds an IPlantValidator from the MetaDataValidator in the given property
     * 
     * @param property The property with the MetaDataValidator.
     * @return An IPlantValidator built with the property's MetaDataValidator.
     */
    protected IPlantValidator buildValidator(Property property) {
        // do we have a validator?
        if (property != null && property.getValidator() != null) {
            MetaDataValidator mdv = property.getValidator();

            if (mdv != null) {
                return new IPlantValidator(tblComponentVals, mdv);
            }
        }

        return null;
    }
}
