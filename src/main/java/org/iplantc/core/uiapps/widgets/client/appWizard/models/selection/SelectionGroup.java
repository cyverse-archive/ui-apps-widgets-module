package org.iplantc.core.uiapps.widgets.client.appWizard.models.selection;

import java.util.List;


public interface SelectionGroup extends SelectionArgument {

    List<SelectionArgument> getArguments();

    void setArguments(List<SelectionArgument> arguments);
}
