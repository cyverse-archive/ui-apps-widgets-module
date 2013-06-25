package org.iplantc.core.uiapps.widgets.client.view.fields.util.converters;

import java.util.Collections;
import java.util.List;

import org.iplantc.core.uicommons.client.models.CommonModelUtils;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.util.DiskResourceUtil;

import com.google.common.collect.Lists;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;

public class SplittableToHasIdListConverter implements Converter<Splittable, List<HasId>> {

    @Override
    public Splittable convertFieldValue(List<HasId> object) {
        return DiskResourceUtil.createStringIdListSplittable(object);
    }

    @Override
    public List<HasId> convertModelValue(Splittable object) {
        // Assume that the splittable is an array of strings
        if ((object == null) || ((object != null) && !object.isIndexed()))
            return Collections.emptyList();

        List<HasId> hasIdList = Lists.newArrayList();
        for (int i = 0; i < object.size(); i++) {
            String asString = object.get(i).asString();
            hasIdList.add(CommonModelUtils.createHasIdFromString(asString));
        }

        return hasIdList;
    }

}
