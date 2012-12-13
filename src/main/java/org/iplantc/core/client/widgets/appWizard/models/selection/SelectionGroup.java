package org.iplantc.core.client.widgets.appWizard.models.selection;

import java.util.List;


public interface SelectionGroup extends SelectionArgument {

    List<SelectionArgument> getArguments();

    void setArguments(List<SelectionArgument> arguments);
}
