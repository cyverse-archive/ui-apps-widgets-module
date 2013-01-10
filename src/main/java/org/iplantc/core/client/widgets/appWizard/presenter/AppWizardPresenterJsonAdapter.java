package org.iplantc.core.client.widgets.appWizard.presenter;

import java.util.List;

import org.iplantc.core.client.widgets.appWizard.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.client.widgets.appWizard.models.TemplatePropertyType;
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
        TemplatePropertyType type = TemplatePropertyType.valueOf(splittable.get("type").asString());
        
        // If the following are defined (not not defined), then assign them
        if(!splittable.isUndefined("validator")){
            Splittable tpValidator = splittable.get("validator");
            tpValidator.get("required").assign(subTemplateProperty, "required");
            
            if(!tpValidator.isUndefined("rules")){
                Splittable inputRules = tpValidator.get("rules");
                getAppTemplateValidator(inputRules, type).assign(subTemplateProperty, "validators");
                getSelectionArguments(inputRules, type).assign(subTemplateProperty, "arguments");
            } else {

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
     * This method may return an empty list.
     * 
     * @param rulesSplittable
     * @param tpType
     * @return a splittable representing an un-keyed list of <code>TemplateValidator</code> objects.
     */
    private static Splittable getAppTemplateValidator(Splittable rulesSplittable, TemplatePropertyType tpType) {
        Splittable subTemplateValidator = StringQuoter.createIndexed();
        
        if (!rulesSplittable.isIndexed()) {
            GWT.log("WRONG!!");
        }
        for (int i = 0; i < rulesSplittable.size(); i++) {
            Splittable rule = rulesSplittable.get(i);
            List<String> ruleKeys = rule.getPropertyKeys();
            for (String key : ruleKeys) {

                // TODO JDS need to integrate the validators. This has not been tested.
                AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
                AutoBean<TemplateValidator> validatorBean = factory.teplateValidator();

                TemplateValidatorType tvType = TemplateValidatorType.valueOf(key);
                // We don't want to add "MustContain" as a validator
                if (tvType.equals(TemplateValidatorType.MustContain))
                    continue;
                Splittable params = rule.get(key);

                validatorBean.as().setType(tvType);
                validatorBean.as().setParams(params);
                Splittable encode = AutoBeanCodex.encode(validatorBean);
                encode.assign(subTemplateValidator, ruleKeys.indexOf(key));
            }
        }
        
        return subTemplateValidator;
    }

    /**
     * This method may return an empty list.
     * 
     * TODO JDS This is ugly. Clean it up.
     * 
     * @param splittable
     * @return
     */
    private static Splittable getSelectionArguments(Splittable splittable, TemplatePropertyType type) {
        Splittable arguments = StringQuoter.createIndexed();
        if (!splittable.isIndexed()) {
            GWT.log("Not indexed splittable!");
        }

        for(int i = 0; i < splittable.size(); i++){
            Splittable rule = splittable.get(i);
            List<String> ruleKeys = rule.getPropertyKeys();
            
            for(String key : ruleKeys){
                if (key.equalsIgnoreCase("MustContain") && rule.get(key).isIndexed()) {
                    if (type.equals(TemplatePropertyType.TextSelection) 
                            || type.equals(TemplatePropertyType.IntegerSelection)
                            || type.equals(TemplatePropertyType.DoubleSelection)
                            || type.equals(TemplatePropertyType.Selection)) {
                        Splittable mustContain = rule.get(key);
                        for (int j = 0; j < mustContain.size(); j++) {
                            Splittable arg = StringQuoter.createSplittable();

                            mustContain.get(j).get("display").assign(arg, "display");
                            mustContain.get(j).get("name").assign(arg, "name");
                            mustContain.get(j).get("value").assign(arg, "value");
                            mustContain.get(j).get("isDefault").assign(arg, "isDefault");

                            arg.assign(arguments, j);
                        }

                    } else if (type.equals(TemplatePropertyType.TreeSelection)) {

                    }

                }
            }
        }
        return arguments;

    }
}
