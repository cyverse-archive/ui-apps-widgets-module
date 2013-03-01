package org.iplantc.core.uiapps.widgets.client.view.fields.util.converters;

import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.data.shared.Converter;

public class SplittableToBooleanConverter implements Converter<Splittable, Boolean> {

    @Override
    public Splittable convertFieldValue(Boolean object) {
        return StringQuoter.create(object);
    }

    @Override
    public Boolean convertModelValue(Splittable object) {
        if (object == null) {
            return null;
        }
        Boolean b = null;
        if (object.isBoolean()) {
            b = object.asBoolean();
        } else if (object.isString()) {
            b = Boolean.valueOf(object.asString());
        }
        return b;
    }

}
