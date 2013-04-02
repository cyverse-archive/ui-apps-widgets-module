package org.iplantc.core.uiapps.widgets.client.presenter;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidator;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidatorType;

import com.google.common.base.Strings;
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
 * TODO JDS Should create "Legacy" autobeans for deserialization
 * 
 * @author jstroot
 * 
 */
public class AppWizardPresenterJsonAdapter {

    public static Splittable adaptAppTemplateJsonString(Splittable legacyAppTemplateJson) {
        
        // Create AppTemplateSplittable and assign top level values.
        Splittable appTemplateSplit = StringQuoter.createSplittable();
        legacyAppTemplateJson.get("id").assign(appTemplateSplit, "id");
        legacyAppTemplateJson.get("name").assign(appTemplateSplit, "name");
        legacyAppTemplateJson.get("label").assign(appTemplateSplit, "label");
        legacyAppTemplateJson.get("type").assign(appTemplateSplit, "type");
        
        Splittable groups = StringQuoter.createIndexed();
        // Loop over the json list of "Template Groups"
        Splittable inputGroups = legacyAppTemplateJson.get("groups");
        for(int i = 0; i < inputGroups.size(); i++){
            getAppArgumentGroup(inputGroups.get(i)).assign(groups, i);
        }
        
        groups.assign(appTemplateSplit, "groups");
        
        return appTemplateSplit;
    }
    
    /**
     * @param unFormattedArgumentGroup a splittable representing an unformatted (from server) template
     *            group
     * @return a splittable representing a single <code>ArgumentGroup</code>.
     */
    private static Splittable getAppArgumentGroup(Splittable unFormattedArgumentGroup) {

        Splittable subGroup = StringQuoter.createSplittable();
        unFormattedArgumentGroup.get("id").assign(subGroup, "id");
        unFormattedArgumentGroup.get("label").assign(subGroup, "label");
        unFormattedArgumentGroup.get("name").assign(subGroup, "name");
        unFormattedArgumentGroup.get("type").assign(subGroup, "type");
        
        Splittable arguments = StringQuoter.createIndexed();
        
        Splittable inputPropertyList = unFormattedArgumentGroup.get("properties");
        for(int i = 0; i < inputPropertyList.size(); i++){
            Splittable subProp = getAppArgument(inputPropertyList.get(i));
            subProp.assign(arguments, i);
        }
        
        arguments.assign(subGroup, "properties");
        
        if (!unFormattedArgumentGroup.isUndefined("groups")) {
            Splittable groups = StringQuoter.createIndexed();

            Splittable inputGroups = unFormattedArgumentGroup.get("groups");
            for(int i = 0; i < inputGroups.size(); i++){
                getAppArgumentGroup(inputGroups.get(i)).assign(groups, i);
            }
            groups.assign(subGroup, "groups");
        }
        return subGroup;
    }

    /**
     * @param splittable
     * @return a splittable representing a single <code>Argument</code>.
     */
    private static Splittable getAppArgument(Splittable splittable) {
        Splittable subArgument = StringQuoter.createSplittable();
        
        
        splittable.get("id").assign(subArgument, "id");
        splittable.get("isVisible").assign(subArgument, "isVisible");
        splittable.get("description").assign(subArgument, "description");
        splittable.get("name").assign(subArgument, "name");
        splittable.get("label").assign(subArgument, "label");
        splittable.get("type").assign(subArgument, "type");
        ArgumentType type = ArgumentType.valueOf(splittable.get("type").asString());
        
        // If the following are defined (not not defined), then assign them
        if(!splittable.isUndefined("validator")){
            Splittable tpValidator = splittable.get("validator");
            tpValidator.get("required").assign(subArgument, "required");
            
            if(!tpValidator.isUndefined("rules")){
                Splittable inputRules = tpValidator.get("rules");
                getAppTemplateValidator(inputRules, type).assign(subArgument, "validators");
                getSelectionArguments(inputRules, type).assign(subArgument, "arguments");
            } else {

            }
        }
        if(!splittable.isUndefined("value")){
            Splittable valueSplittable = splittable.get("value");
            if (valueSplittable.isString() && !Strings.isNullOrEmpty(valueSplittable.asString())) {
                String asString = valueSplittable.asString();
                try {
                    StringQuoter.split(asString).assign(subArgument, "value");
                } catch (RuntimeException e) {
                    valueSplittable.assign(subArgument, "value");
                }
            }else if (valueSplittable.isNumber()){
                String asNumber = Double.toString(valueSplittable.asNumber());
                StringQuoter.split(asNumber).assign(subArgument, "value");
            }else{
                valueSplittable.assign(subArgument, "value");
            }
        }
        if(!splittable.isUndefined("omit_if_blank")){
            splittable.get("omit_if_blank").assign(subArgument, "omit_if_blank");
        }
        if(!splittable.isUndefined("order")){
            splittable.get("order").assign(subArgument, "order");
        }
        
        return subArgument;
    }

    /**
     * This method may return an empty list.
     * 
     * @param rulesSplittable
     * @param tpType
     * @return a splittable representing an un-keyed list of <code>TemplateValidator</code> objects.
     */
    private static Splittable getAppTemplateValidator(Splittable rulesSplittable, ArgumentType tpType) {
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
                AutoBean<ArgumentValidator> validatorBean = factory.argumentValidator();

                ArgumentValidatorType tvType = ArgumentValidatorType.valueOf(key);
                // We don't want to add "MustContain" as a validator
                if (tvType.equals(ArgumentValidatorType.MustContain))
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
    private static Splittable getSelectionArguments(Splittable splittable, ArgumentType type) {
        Splittable arguments = StringQuoter.createIndexed();
        if (!splittable.isIndexed()) {
            GWT.log("Not indexed splittable!");
        }

        for(int i = 0; i < splittable.size(); i++){
            Splittable rule = splittable.get(i);
            List<String> ruleKeys = rule.getPropertyKeys();
            
            for(String key : ruleKeys){
                if (key.equalsIgnoreCase("MustContain") && rule.get(key).isIndexed()) {
                    if (type.equals(ArgumentType.TextSelection) 
                            || type.equals(ArgumentType.IntegerSelection)
                            || type.equals(ArgumentType.DoubleSelection)
                            || type.equals(ArgumentType.Selection)) {
                        Splittable mustContain = rule.get(key);
                        for (int j = 0; j < mustContain.size(); j++) {
                            Splittable arg = StringQuoter.createSplittable();

                            mustContain.get(j).get("display").assign(arg, "display");
                            mustContain.get(j).get("name").assign(arg, "name");
                            mustContain.get(j).get("value").assign(arg, "value");
                            mustContain.get(j).get("isDefault").assign(arg, "isDefault");

                            arg.assign(arguments, j);
                        }

                    } else if (type.equals(ArgumentType.TreeSelection)) {

                    }

                }
            }
        }
        return arguments;

    }
}
