package org.iplantc.core.client.widgets.appWizard.models.legacy;

import org.iplantc.core.client.widgets.appWizard.models.AppTemplate;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.sencha.gxt.data.shared.Converter;

/**
 * 
 * TODO JDS Implement this converter using AutoBeanVisitor
 * Another way to do this would be to first get all the properties of the given object
 * 
 * <pre>
 * Map&lt;String, Object&gt; allProperties = AutoBeanUtils.getAllProperties(object);
 * </pre>
 * 
 * Then, create a visitor class which accepts this map;
 * and on construction, factory creates a new AppTemplate auto bean;
 * then visits that autobean.
 * As it traverses the autobean, it will use the value property names as keys into the map
 * and set it's value.
 * 
 * This will take care of all the properties which match.
 * You will have to visit the reference properties
 * 
 * @author jstroot
 * 
 */
public class LegacyAppTemplateConverter implements Converter<AutoBean<LegacyAppTemplate>, AutoBean<AppTemplate>> {


    @Override
    public AutoBean<LegacyAppTemplate> convertFieldValue(AutoBean<AppTemplate> object) {
        LegacyAppTemplateAutoBeanFactory factory = GWT.create(LegacyAppTemplateAutoBeanFactory.class);
        LegacyAppTemplate retVal = factory.legacyAppTemplate().as();
        AppTemplate at = object.as();

        retVal.setLabel(at.getLabel());
        retVal.setName(at.getName());

        return null;
    }

    @Override
    public AutoBean<AppTemplate> convertModelValue(AutoBean<LegacyAppTemplate> object) {
        // AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
        // AppTemplate retVal = factory.appTemplate().as();
        // LegacyAppTemplate lat = object.as();

        return null;
    }


}
