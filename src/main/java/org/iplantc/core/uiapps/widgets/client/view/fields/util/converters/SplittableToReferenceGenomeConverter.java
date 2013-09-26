package org.iplantc.core.uiapps.widgets.client.view.fields.util.converters;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenome;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.HasSplittable;
import com.sencha.gxt.data.shared.Converter;

public class SplittableToReferenceGenomeConverter implements Converter<Splittable, ReferenceGenome> {
    private final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);

    @Override
    public Splittable convertFieldValue(ReferenceGenome object) {
        /*
         * JDS We can use HasSplittable here since we have no worries about ReferenceGenomes being edited
         * at runtime.
         * FIXME JDS This logic needs to be cleaned up. Throws NPEs in app preview mode.
         */
        return ((HasSplittable)AutoBeanUtils.getAutoBean(object)).getSplittable();
    }

    @Override
    public ReferenceGenome convertModelValue(Splittable object) {
        if (object == null)
            return null;

        AutoBean<ReferenceGenome> ab = AutoBeanCodex.decode(factory, ReferenceGenome.class, object);
        return ab.as();
    }
}