package org.iplantc.core.uiapps.widgets.client.models;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface ArgumentValidatorProperties extends PropertyAccess<ArgumentValidator> {

    @Path("type.name")
    ValueProvider<ArgumentValidator, String> name();

}
