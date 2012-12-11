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
 * KLUDGE JDS This class can go away if the server side JSON data model is cleaned up.
 * 
 * @author jstroot
 * 
 */
public class AppWizardPresenterJsonAdapter {

    public static Splittable adaptAppTemplateJsonString(String unformattedAppTemplateJson){
        Splittable input = StringQuoter.split(unformattedAppTemplateJson);
        
        // Create AppTemplateSplittable and assign top level values.
        Splittable appTemplateSplit = StringQuoter.createSplittable();
        input.get("id").assign(appTemplateSplit, "id");
        input.get("name").assign(appTemplateSplit, "name");
        input.get("label").assign(appTemplateSplit, "label");
        input.get("type").assign(appTemplateSplit, "type");
        
        Splittable groups = StringQuoter.createIndexed();
        // Loop over the json list of "Template Groups"
        Splittable inputGroups = input.get("groups");
        for(int i = 0; i < inputGroups.size(); i++){
            getAppTemplateGroup(inputGroups.get(i)).assign(groups, i);
        }
        
        groups.assign(appTemplateSplit, "groups");
        
        return appTemplateSplit;
    }
    
    /**
     * @param unFormattedTemplateGroup a splittable representing an unformatted (from server) template group
     * @return a splittable representing a single <code>TemplateGroup</code>.
     */
    private static Splittable getAppTemplateGroup(Splittable unFormattedTemplateGroup) {

        Splittable subGroup = StringQuoter.createSplittable();
        unFormattedTemplateGroup.get("id").assign(subGroup, "id");
        unFormattedTemplateGroup.get("label").assign(subGroup, "label");
        unFormattedTemplateGroup.get("name").assign(subGroup, "name");
        unFormattedTemplateGroup.get("type").assign(subGroup, "type");
        
        Splittable properties = StringQuoter.createIndexed();
        
        Splittable inputPropertyList = unFormattedTemplateGroup.get("properties");
        for(int i = 0; i < inputPropertyList.size(); i++){
            Splittable subProp = getAppTemplateProperty(inputPropertyList.get(i));
            subProp.assign(properties, i);
        }
        
        properties.assign(subGroup, "properties");
        
        if(!unFormattedTemplateGroup.isUndefined("groups")){
            Splittable groups = StringQuoter.createIndexed();

            Splittable inputGroups = unFormattedTemplateGroup.get("groups");
            for(int i = 0; i < inputGroups.size(); i++){
                getAppTemplateGroup(inputGroups.get(i)).assign(groups, i);
            }
            groups.assign(subGroup, "groups");
        }
        return subGroup;
    }

    /**
     * @param splittable
     * @return a splittable representing a single <code>TemplateProperty</code>.
     */
    private static Splittable getAppTemplateProperty(Splittable splittable) {
        Splittable subTemplateProperty = StringQuoter.createSplittable();
        
        
        splittable.get("id").assign(subTemplateProperty, "id");
        splittable.get("isVisible").assign(subTemplateProperty, "isVisible");
        splittable.get("description").assign(subTemplateProperty, "description");
        splittable.get("name").assign(subTemplateProperty, "name");
        splittable.get("label").assign(subTemplateProperty, "label");
        splittable.get("type").assign(subTemplateProperty, "type");
        
        // If the following are defined (not not defined), then assign them
        if(!splittable.isUndefined("validator")){
            Splittable tpValidator = splittable.get("validator");
            tpValidator.get("required").assign(subTemplateProperty, "required");
            
            if(!tpValidator.isUndefined("rules")){
                Splittable inputRules = splittable.get("rules");
                getAppTemplateValidator(inputRules).assign(subTemplateProperty, "validators");
            }
        }
        if(!splittable.isUndefined("value")){
            splittable.get("value").assign(subTemplateProperty, "value");
        }
        if(!splittable.isUndefined("omit_if_blank")){
            splittable.get("omit_if_blank").assign(subTemplateProperty, "omit_if_blank");
        }
        if(!splittable.isUndefined("value")){
            splittable.get("value").assign(subTemplateProperty, "value");
        }
        if(!splittable.isUndefined("order")){
            splittable.get("order").assign(subTemplateProperty, "order");
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
            AutoBeanCodex.encode(validatorBean).assign(subTemplateValidator, ruleKeys.indexOf(key));
        }
        
        return subTemplateValidator;
    }
}
