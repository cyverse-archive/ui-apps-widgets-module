package org.iplantc.core.uiapps.widgets.client.models;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface ArgumentProperties extends PropertyAccess<Argument> {

    ValueProvider<Argument, String> name();

    ValueProvider<Argument, Integer> order();
}
