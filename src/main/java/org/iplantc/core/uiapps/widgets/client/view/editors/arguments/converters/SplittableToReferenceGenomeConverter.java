package org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.data.shared.Converter;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenome;

public class SplittableToReferenceGenomeConverter implements Converter<Splittable, ReferenceGenome> {
    private final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);

    @Override
    public Splittable convertFieldValue(ReferenceGenome object) {
        return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(object));
    }

    @Override
    public ReferenceGenome convertModelValue(Splittable object) {
        if (object == null)
            return null;

        AutoBean<ReferenceGenome> ab = AutoBeanCodex.decode(factory, ReferenceGenome.class, object);
        return ab.as();
    }
}