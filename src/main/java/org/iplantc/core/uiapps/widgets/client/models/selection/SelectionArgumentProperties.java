package org.iplantc.core.uiapps.widgets.client.models.selection;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface SelectionArgumentProperties extends PropertyAccess<SelectionArgument> {

    ValueProvider<SelectionArgument, String> display();

    ValueProvider<SelectionArgument, String> name();

    ValueProvider<SelectionArgument, String> value();

}
