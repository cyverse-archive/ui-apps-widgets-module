package org.iplantc.core.widgets.client.appWizard.models.selection;

import java.util.List;


public interface SelectionGroup extends SelectionArgument {

    List<SelectionArgument> getArguments();

    void setArguments(List<SelectionArgument> arguments);
}
