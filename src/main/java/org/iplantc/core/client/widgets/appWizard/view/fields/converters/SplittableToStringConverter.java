package org.iplantc.core.client.widgets.appWizard.view.fields.converters;

import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.data.shared.Converter;

public class SplittableToStringConverter implements Converter<Splittable, String> {

    @Override
    public Splittable convertFieldValue(String object) {
        return StringQuoter.create(object);
    }

    @Override
    public String convertModelValue(Splittable object) {
        return object.asString();
    }

}
