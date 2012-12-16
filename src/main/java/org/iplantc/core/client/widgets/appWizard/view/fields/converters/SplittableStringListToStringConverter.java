package org.iplantc.core.client.widgets.appWizard.view.fields.converters;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gwt.json.client.JSONArray;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.data.shared.Converter;

public class SplittableStringListToStringConverter implements Converter<Splittable, String> {

    @Override
    public Splittable convertFieldValue(String object) {
        // Parse text field into a list
        // Convert the list to a JSONArray
        // Create Splittable from JSONArray payload.
        List<String> split = Lists.newArrayList(Splitter.on(",").split(object));
        return StringQuoter.create(JsonUtil.buildArrayFromStrings(split).toString());
    }

    @Override
    public String convertModelValue(Splittable object) {
        String payload = object.getPayload();
        JSONArray jsonArray = JsonUtil.getObject(payload).isArray();
        List<String> list = JsonUtil.buildStringList(jsonArray);
        return Joiner.on(",").join(list);
    }

}

