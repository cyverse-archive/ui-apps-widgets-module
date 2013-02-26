package org.iplantc.core.uiapps.widgets.client.appWizard.models.selection;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface SelectionProperties extends PropertyAccess<SelectionArgument> {

    ModelKeyProvider<SelectionArgument> id();

    ValueProvider<SelectionArgument, String> name();

    LabelProvider<SelectionArgument> display();

}
