package org.iplantc.core.client.widgets;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.metadata.client.validation.ListRuleArgument;
import org.iplantc.core.metadata.client.validation.ListRuleArgumentFactory;
import org.iplantc.core.metadata.client.validation.ListRuleArgumentGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeView;

/**
 * A Checkable Tree for displaying ListRuleArgument in a wizard.
 * 
 * @author psarando
 * 
 */
public class ListRuleArgumentTree extends Tree<ListRuleArgument, String> {
    private final ListRuleArgumentFactory factory = GWT.create(ListRuleArgumentFactory.class);

    public ListRuleArgumentTree() {
        this(new TreeStore<ListRuleArgument>(new ModelKeyProvider<ListRuleArgument>() {
            @Override
            public String getKey(ListRuleArgument item) {
                return item.getId();
            }
        }), new ValueProvider<ListRuleArgument, String>() {

            @Override
            public String getValue(ListRuleArgument object) {
                return object.getDisplay();
            }

            @Override
            public void setValue(ListRuleArgument object, String value) {
                // intentionally do nothing; this is not an editor
            }

            @Override
            public String getPath() {
                return "display"; //$NON-NLS-1$
            }
        });
    }

    public ListRuleArgumentTree(TreeStore<ListRuleArgument> store,
            ValueProvider<ListRuleArgument, String> valueProvider) {
        super(store, valueProvider);

        setBorders(true);
        setAutoLoad(true);

        setCheckable(true);
        setCheckStyle(CheckCascade.TRI);
    }

    @Override
    protected SafeHtml renderChild(ListRuleArgument parent, ListRuleArgument child, int depth,
            TreeView.TreeViewRenderMode renderMode) {
        SafeHtml html = super.renderChild(parent, child, depth, renderMode);

        String tooltip = child.getDescription();
        if (tooltip == null || tooltip.isEmpty()) {
            // This node has no description.
            return html;
        }

        // Apply the description as a tool-tip to this node.
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendHtmlConstant("<span title='" + tooltip + "'>"); //$NON-NLS-1$ //$NON-NLS-2$
        builder.append(html);
        builder.appendHtmlConstant("</span>"); //$NON-NLS-1$

        return builder.toSafeHtml();
    }

    /**
     * Resets the tree's contents with the ListRuleArguments in the given root JSON string.
     * 
     * @param root A JSON string of a ListRuleArgumentGroup.
     */
    public void setItems(String json) {
        setItems(AutoBeanCodex.decode(factory, ListRuleArgumentGroup.class, json).as());
    }

    /**
     * Resets the tree's contents with the ListRuleArguments in the given root.
     * 
     * @param root A ListRuleArgumentGroup containing the items to populate in this tree.
     */
    public void setItems(ListRuleArgumentGroup root) {
        store.clear();

        if (root == null) {
            return;
        }

        List<ListRuleArgument> defaultSelection = new ArrayList<ListRuleArgument>();

        if (root.getGroups() != null) {
            for (ListRuleArgumentGroup group : root.getGroups()) {
                defaultSelection.addAll(addGroupToStore(null, group));
            }
        }

        if (root.getArguments() != null) {
            for (ListRuleArgument ruleArg : root.getArguments()) {
                store.add(ruleArg);

                if (ruleArg.isDefault()) {
                    defaultSelection.add(ruleArg);
                }
            }
        }

        if (!defaultSelection.isEmpty()) {
            setCheckedSelection(defaultSelection);
        }
    }

    /**
     * Adds the group to the parent, or as a root if parent is null, then adds group's children to the
     * store. Returns a list of any items that should be selected by default.
     * 
     * @param parent The parent of group.
     * @param group The group to add to the store.
     * @return A list of any items that should be selected by default.
     */
    private List<ListRuleArgument> addGroupToStore(ListRuleArgumentGroup parent,
            ListRuleArgumentGroup group) {
        List<ListRuleArgument> defaultSelection = new ArrayList<ListRuleArgument>();

        if (group != null) {
            if (parent != null) {
                store.add(parent, group);
            } else {
                store.add(group);
            }

            setLeaf(group, false);
            store.update(group);

            if (group.getGroups() != null) {
                for (ListRuleArgumentGroup child : group.getGroups()) {
                    defaultSelection.addAll(addGroupToStore(group, child));
                }
            }

            if (group.getArguments() != null) {
                for (ListRuleArgument child : group.getArguments()) {
                    store.add(group, child);

                    if (child.isDefault()) {
                        defaultSelection.add(child);
                    }
                }
            }

            if (group.isDefault()) {
                defaultSelection.add(group);
            }
        }

        return defaultSelection;
    }
}
