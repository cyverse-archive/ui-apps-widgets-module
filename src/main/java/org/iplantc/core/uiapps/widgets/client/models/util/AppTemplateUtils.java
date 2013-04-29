package org.iplantc.core.uiapps.widgets.client.models.util;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

public class AppTemplateUtils {
    private static final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);

    public static AppTemplate copyAppTemplate(AppTemplate value) {
        AutoBean<AppTemplate> argAb = AutoBeanUtils.getAutoBean(value);
        Splittable splitCopy = AutoBeanCodex.encode(argAb);

        return AutoBeanCodex.decode(factory, AppTemplate.class, splitCopy.getPayload()).as();
    }

    public static Argument copyArgument(Argument value) {
        AutoBean<Argument> argAb = AutoBeanUtils.getAutoBean(value);
        Splittable splitCopy = AutoBeanCodex.encode(argAb);

        return AutoBeanCodex.decode(factory, Argument.class, splitCopy.getPayload()).as();
    }

    public static ArgumentGroup copyArgumentGroup(ArgumentGroup value) {
        AutoBean<ArgumentGroup> argGrpAb = AutoBeanUtils.getAutoBean(value);
        Splittable splitCopy = AutoBeanCodex.encode(argGrpAb);

        return AutoBeanCodex.decode(factory, ArgumentGroup.class, splitCopy).as();
    }
}
