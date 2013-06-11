package org.iplantc.core.uiapps.widgets.client.models.selection;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;

public interface SelectionItemGroup extends SelectionItem {

    List<SelectionItem> getArguments();

    void setArguments(List<SelectionItem> arguments);

    @PropertyName("isSingleSelect")
    boolean isSingleSelect();

    @PropertyName("isSingleSelect")
    void setSingleSelect(boolean singleSelect);

    List<SelectionItemGroup> getGroups();

    void setGroups(List<SelectionItemGroup> groups);

    CheckCascade getSelectionCascade();

    void setSelectionCascade(CheckCascade value);

}
