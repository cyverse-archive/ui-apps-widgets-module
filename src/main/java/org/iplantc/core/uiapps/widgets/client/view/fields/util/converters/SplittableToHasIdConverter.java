package org.iplantc.core.uiapps.widgets.client.view.fields.util.converters;

import org.iplantc.core.uicommons.client.models.CommonModelUtils;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.common.base.Strings;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;

public class SplittableToHasIdConverter implements Converter<Splittable, HasId> {

    @Override
    public Splittable convertFieldValue(HasId object) {
        if (object == null)
            return null;
        return AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(object));
    }

    @Override
    public HasId convertModelValue(Splittable object) {
        if (object == null)
            return null;
        HasId ret = null;
        Splittable idKeyValue = object.get("id");
        if ((idKeyValue != null) && !Strings.isNullOrEmpty(idKeyValue.asString())) {
            ret = CommonModelUtils.createHasIdFromString(idKeyValue.asString());
        } else if (!Strings.isNullOrEmpty(object.asString())) {
            ret = CommonModelUtils.createHasIdFromString(object.asString());
        }
        return ret;
    }

}
