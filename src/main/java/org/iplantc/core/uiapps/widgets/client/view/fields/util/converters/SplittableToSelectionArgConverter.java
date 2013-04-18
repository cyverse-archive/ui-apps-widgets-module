package org.iplantc.core.uiapps.widgets.client.view.fields.util.converters;

import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionArgument;

import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;

public final class SplittableToSelectionArgConverter implements Converter<Splittable, SelectionArgument> {
    // private final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
    @Override
    public Splittable convertFieldValue(SelectionArgument object) {
        return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(object));
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sencha.gxt.data.shared.Converter#convertModelValue(java.lang.Object)
     * TODO JDS Determine how selection arguments will be set from a given value.
     * This is tricky, because the default values are the actual values, not the id. So, when you try to
     * set a value for a combo-box, it refers to the internal ListStore, and tries to lookup the value
     * you are setting by its ID, not by its value.
     */
    @Override
    public SelectionArgument convertModelValue(Splittable object) {
        if (object == null)
            return null;
        // AutoBean<SelectionArgument> ab = AutoBeanCodex.decode(factory, SelectionArgument.class,
        // object);
        // return ab.as();
        return null;
    }
}