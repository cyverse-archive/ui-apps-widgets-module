package org.iplantc.core.widgets.client.appWizard.models.selection;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;

public interface HierarchicalSelection {

    CheckCascade getSelectionCascade();

    void setSelectionCascade(CheckCascade selectionCascade);

    @PropertyName("isSingleSelect")
    boolean isSingleSelect();

    @PropertyName("isSingleSelect")
    void setSingleSelect(boolean singleSelect);

    List<SelectionArgument> getArguments();

    void setArguments(List<SelectionArgument> arguments);

    List<SelectionGroup> getGroups();

    void setGroups(List<SelectionGroup> groups);

}
