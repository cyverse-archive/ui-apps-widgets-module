package org.iplantc.core.uiapps.widgets.client.models.selection;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.sencha.gxt.widget.core.client.tree.Tree;

public interface SelectionItemGroup extends SelectionItem {

    public enum CheckCascade {
        TRI(Tree.CheckCascade.TRI, I18N.DISPLAY.treeSelectorCascadeTri()),
        PARENTS(Tree.CheckCascade.PARENTS, I18N.DISPLAY.treeSelectorCascadeParent()),
        CHILDREN(Tree.CheckCascade.CHILDREN, I18N.DISPLAY.treeSelectorCascadeChildren()),
        NONE(Tree.CheckCascade.NONE, I18N.DISPLAY.treeSelectorCascadeNone());

        private final Tree.CheckCascade cascade;
        private final String display;

        private CheckCascade(Tree.CheckCascade cascade, String display) {
            this.cascade = cascade;
            this.display = display;
        }

        public Tree.CheckCascade getTreeCheckCascade() {
            return cascade;
        }

        public String getDisplay() {
            return display;
        }
    }

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
