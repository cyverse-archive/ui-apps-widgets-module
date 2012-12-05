package org.iplantc.core.client.widgets.appWizard.presenter;

import java.util.List;

import org.iplantc.core.client.widgets.appWizard.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.client.widgets.appWizard.models.TemplateValidator;
import org.iplantc.core.client.widgets.appWizard.models.TemplateValidatorType;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

/**
 * A helper class which adapts the App Template JSON string returned from the server into 
 * a <code>Splittable</code> which can be used to generate an <code>AutoBean&lt;AppTemplate&gt;</code>
 *  
 * @author jstroot
 *
 */
public class AppWizardPresenterJsonAdapter {

    public static Splittable adaptAppTemplateJsonString(String unformattedAppTemplateJson){
        Splittable input = StringQuoter.split(unformattedAppTemplateJson);
        
        // Create AppTemplateSplittable and assign top level values.
        Splittable appTemplateSplit = StringQuoter.createSplittable();
        appTemplateSplit.assign(input.get("id"), "id");
        appTemplateSplit.assign(input.get("name"), "name");
        appTemplateSplit.assign(input.get("label"), "label");
        appTemplateSplit.assign(input.get("type"), "type");
        
        Splittable groups = StringQuoter.createSplittable();
        // Loop over the json list of "Template Groups"
        Splittable inputGroups = input.get("groups");
        for(int i = 0; i < inputGroups.size(); i++){
            groups.assign(getAppTemplateGroup(inputGroups.get(i)), i);
        }
        
        appTemplateSplit.assign(groups, "groups");
        
        return appTemplateSplit;
    }
    
    /**
     * @param unFormattedTemplateGroup a splittable representing an unformatted (from server) template group
     * @return a splittable representing a single <code>TemplateGroup</code>.
     */
    private static Splittable getAppTemplateGroup(Splittable unFormattedTemplateGroup) {

        Splittable subGroup = StringQuoter.createSplittable();
        subGroup.assign(unFormattedTemplateGroup.get("id"), "id");
        subGroup.assign(unFormattedTemplateGroup.get("label"), "label");
        subGroup.assign(unFormattedTemplateGroup.get("name"), "name");
        subGroup.assign(unFormattedTemplateGroup.get("type"), "type");
        
        Splittable properties = StringQuoter.createSplittable();
        
        Splittable inputPropertyList = unFormattedTemplateGroup.get("properties");
        for(int i = 0; i < inputPropertyList.size(); i++){
            Splittable subProp = getAppTemplateProperty(inputPropertyList.get(i));
            properties.assign(subProp, i);
        }
        
        subGroup.assign(properties, "properties");
        
        if(!unFormattedTemplateGroup.isUndefined("groups")){
            Splittable groups = StringQuoter.createSplittable();

            Splittable inputGroups = unFormattedTemplateGroup.get("groups");
            for(int i = 0; i < inputGroups.size(); i++){
                groups.assign(getAppTemplateGroup(inputGroups.get(i)), i);
            }
            subGroup.assign(groups, "groups");
        }
        return subGroup;
    }

    /**
     * @param splittable
     * @return a splittable representing a single <code>TemplateProperty</code>.
     */
    private static Splittable getAppTemplateProperty(Splittable splittable) {
        Splittable subTemplateProperty = StringQuoter.createSplittable();
        
        Splittable tpValidator = splittable.get("validator");
        
        subTemplateProperty.assign(splittable.get("id"), "id");
        subTemplateProperty.assign(splittable.get("isVisible"), "isVisible");
        subTemplateProperty.assign(null, "description");
        subTemplateProperty.assign(splittable.get("name"), "name");
        subTemplateProperty.assign(splittable.get("label"), "label");
        subTemplateProperty.assign(splittable.get("type"), "type");
        subTemplateProperty.assign(tpValidator.get("required"), "isRequired");
        
        // If the following are defined (not not defined), then assign them
        if(!splittable.isUndefined("value")){
            subTemplateProperty.assign(splittable.get("value"), "value");
        }
        if(!splittable.isUndefined("omit_if_blank")){
            subTemplateProperty.assign(splittable.get("omit_if_blank"), "omit_if_blank");
        }
        if(!splittable.isUndefined("value")){
            subTemplateProperty.assign(splittable.get("value"), "defaultValue");
        }
        if(!splittable.isUndefined("order")){
            subTemplateProperty.assign(splittable.get("order"), "order");
        }
        
        if(!tpValidator.isUndefined("rules")){
            Splittable inputRules = splittable.get("rules");
            subTemplateProperty.assign(getAppTemplateValidator(inputRules), "validators");
        }
        return subTemplateProperty;
    }

    /**
     * @param rulesSplittable
     * @return a splittable representing an un-keyed list of <code>TemplateValidator</code> objects.
     */
    private static Splittable getAppTemplateValidator(Splittable rulesSplittable) {
        Splittable subTemplateValidator = StringQuoter.createSplittable();
        
        // transforming "rules" key into a list of TemplateValidators
        List<String> ruleKeys = rulesSplittable.getPropertyKeys();
        for(String key : ruleKeys){
            AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
            AutoBean<TemplateValidator> validatorBean = factory.teplateValidator();
            
            TemplateValidatorType type = TemplateValidatorType.getTypeByValue(key);
            Splittable params = rulesSplittable.get(key);
            
            validatorBean.as().setType(type);
            validatorBean.as().setParams(params);
            subTemplateValidator.assign(AutoBeanCodex.encode(validatorBean), ruleKeys.indexOf(key));
        }
        
        return subTemplateValidator;
    }
}
