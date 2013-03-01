package org.iplantc.core.uiapps.widgets.client.view.fields.util.converters;

import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.data.shared.Converter;

public class SplittableToDoubleConverter implements Converter<Splittable, Double> {

    @Override
    public Splittable convertFieldValue(Double object) {
        if (object == null)
            return StringQuoter.createSplittable();

        return StringQuoter.create(object);
    }

    @Override
    public Double convertModelValue(Splittable object) {
        if (object == null)
            return null;

        return Double.valueOf(object.asNumber());
    }

}
