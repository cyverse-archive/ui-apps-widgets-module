package org.iplantc.core.client.widgets.appWizard.models;

import org.iplantc.core.client.widgets.appWizard.models.selection.SelectionArgument;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface AppTemplateAutoBeanFactory extends AutoBeanFactory {
    
    AutoBean<AppTemplate> appTemplate();
    
    AutoBean<TemplateGroup> templateGroup();
    
    AutoBean<TemplateProperty> templateProperty();

    AutoBean<TemplateValidator> teplateValidator();

    AutoBean<SelectionArgument> selectionArgument();

}
