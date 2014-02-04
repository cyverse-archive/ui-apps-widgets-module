package org.iplantc.de.apps.widgets.client.models;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface ArgumentProperties extends PropertyAccess<Argument> {

    ValueProvider<Argument, String> name();

    ValueProvider<Argument, Integer> order();

    ModelKeyProvider<Argument> id();
}
