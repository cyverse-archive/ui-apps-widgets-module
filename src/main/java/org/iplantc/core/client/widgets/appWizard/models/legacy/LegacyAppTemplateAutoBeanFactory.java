package org.iplantc.core.client.widgets.appWizard.models.legacy;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface LegacyAppTemplateAutoBeanFactory extends AutoBeanFactory {

    AutoBean<LegacyAppTemplate> legacyAppTemplate();

    AutoBean<LegacyTemplateGroup> legacyTemplateGroup();

    AutoBean<LegacyTemplateProperty> legacyTemplateProperty();

    AutoBean<LegacyTemplateValidator> legacyTemplateValidator();

}
