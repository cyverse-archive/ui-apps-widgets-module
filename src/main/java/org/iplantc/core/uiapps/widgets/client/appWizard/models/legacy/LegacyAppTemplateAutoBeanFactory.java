package org.iplantc.core.uiapps.widgets.client.appWizard.models.legacy;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface LegacyAppTemplateAutoBeanFactory extends AutoBeanFactory {

    AutoBean<LegacyAppTemplate> legacyAppTemplate();

    AutoBean<LegacyArgumentGroup> legacyArgumentGroup();

    AutoBean<LegacyArgument> legacyArgument();

    AutoBean<LegacyTemplateValidator> legacyTemplateValidator();

}
