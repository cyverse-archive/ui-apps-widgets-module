package org.iplantc.core.uiapps.widgets.client.view.fields.util.converters;

import com.google.common.base.Strings;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.data.shared.Converter;

public class SplittableToIntegerConverter implements Converter<Splittable, Integer> {

    @Override
    public Splittable convertFieldValue(Integer object) {
        if (object == null)
            return null;

        return StringQuoter.create(object);
    }

    @Override
    public Integer convertModelValue(Splittable object) {
        if (object == null)
            return null;

        Integer intValue = null;
        if (object.isNumber()) {
            intValue = Double.valueOf(object.asNumber()).intValue();
        } else if (object.isString() && !Strings.isNullOrEmpty(object.asString())) {
            try {
                intValue = Integer.valueOf(object.asString());
            } catch (NumberFormatException e) {
                intValue = null;
            }
        }
        return intValue;
    }

}
