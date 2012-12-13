package org.iplantc.core.client.widgets.appWizard.view.fields.converters;

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
        Boolean b = null;
        if (object.isBoolean()) {
            b = object.asBoolean();
        } else if (object.isString()) {
            b = Boolean.valueOf(object.asString());
        }
        return b;
    }

}
