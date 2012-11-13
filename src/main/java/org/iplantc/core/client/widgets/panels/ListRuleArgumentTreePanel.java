package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.ListRuleArgumentTree;
import org.iplantc.core.metadata.client.validation.ListRuleArgument;
import org.iplantc.core.metadata.client.validation.ListRuleArgumentGroup;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

/**
 * A Panel that contains a ListRuleArgumentTree and a filtering field that filters the tree items.
 * 
 * @author psarando
 * 
 */
public class ListRuleArgumentTreePanel extends FlowLayoutContainer {

    private ListRuleArgumentTree tree;

    public ListRuleArgumentTreePanel() {
        TreeStore<ListRuleArgument> store = buildStore();

        initTree(store);

        // This handler must be added after the store is added to the tree, since the tree adds its own
        // handlers that must trigger before this one.
        addCheckedRestoreFilterHandler(store);

        add(buildFilter(store));
        add(tree);
    }

    private TreeStore<ListRuleArgument> buildStore() {
        TreeStore<ListRuleArgument> store = new TreeStore<ListRuleArgument>(
                new ModelKeyProvider<ListRuleArgument>() {
                    @Override
                    public String getKey(ListRuleArgument item) {
                        return item.getId();
                    }
                });

        return store;
    }

    private void initTree(TreeStore<ListRuleArgument> store) {
        ValueProvider<ListRuleArgument, String> valueProvider = new ValueProvider<ListRuleArgument, String>() {

            @Override
            public String getValue(ListRuleArgument object) {
                return object.getDisplay();
            }

            @Override
            public void setValue(ListRuleArgument object, String value) {
                // intentionally do nothing, since this is not an editor
            }

            @Override
            public String getPath() {
                return "display"; //$NON-NLS-1$
            }
        };

        tree = new ListRuleArgumentTree(store, valueProvider);

        // Store the tree's Checked state in each item's isDefault field.
        tree.addCheckChangeHandler(new CheckChangeHandler<ListRuleArgument>() {

            @Override
            public void onCheckChange(CheckChangeEvent<ListRuleArgument> event) {
                ListRuleArgument ruleArg = event.getItem();
                boolean checked = event.getChecked() == CheckState.CHECKED;
                boolean isGroup = ruleArg instanceof ListRuleArgumentGroup;

                // Don't set the checked value for Groups if the store is filtered, since a check cascade
                // can check a group when its filtered-out children are not checked.
                if (!(checked && isGroup && tree.getStore().isFiltered())) {
                    ruleArg.setDefault(checked);
                }
            }
        });
    }

    /**
     * Adds a handler to the store that will restore the CheckState of the tree, and expand any Group
     * that is checked or has checked children. This handler must be added after the store is added to
     * the tree, since the tree adds its own handlers that must trigger before this one.
     * 
     * @param store
     */
    private void addCheckedRestoreFilterHandler(TreeStore<ListRuleArgument> store) {
        // Restore the tree's Checked state from each item's isDefault field after it's filtered.
        store.addStoreFilterHandler(new StoreFilterHandler<ListRuleArgument>() {

            @Override
            public void onFilter(StoreFilterEvent<ListRuleArgument> event) {
                TreeStore<ListRuleArgument> treeStore = tree.getStore();

                for (ListRuleArgument ruleArg : treeStore.getAll()) {
                    if (ruleArg.isDefault()) {
                        tree.setChecked(ruleArg, CheckState.CHECKED);
                    }
                }

                boolean filtered = treeStore.isFiltered();

                for (ListRuleArgument ruleArg : treeStore.getAll()) {
                    // Ensure any groups with filtered-out children still display the group icon.
                    if (ruleArg instanceof ListRuleArgumentGroup) {
                        tree.setLeaf(ruleArg, false);
                        tree.refresh(ruleArg);
                    }

                    if (!filtered && ruleArg.isDefault()) {
                        // Restore the tree's expanded state when the filter is cleared.
                        tree.setExpanded(ruleArg, true);
                    }
                }
            }
        });
    }

    private StoreFilterField<ListRuleArgument> buildFilter(TreeStore<ListRuleArgument> store) {
        StoreFilterField<ListRuleArgument> treeFilter = new StoreFilterField<ListRuleArgument>() {
            @Override
            protected void onKeyUp(Event event) {
                tree.mask();

                super.onKeyUp(event);
            }

            @Override
            protected void onFilter() {
                super.onFilter();

                tree.unmask();
            }

            @Override
            protected boolean doSelect(Store<ListRuleArgument> store, ListRuleArgument parent,
                    ListRuleArgument item, String filter) {
                String itemDisplay = item.getDisplay().toLowerCase();
                String searchTerm = filter.toLowerCase();

                return (itemDisplay.indexOf(searchTerm) >= 0);
            }
        };

        treeFilter.bind(store);
        treeFilter.setEmptyText(I18N.DISPLAY.treeSelectorFilterEmptyText());
        treeFilter.setWidth(250);
        treeFilter.setValidationDelay(750);

        return treeFilter;
    }

    /**
     * Sets the Command to execute after the tree's checked selection changes.
     * 
     * @param updateCmd The component value table update Command to execute after selection changes.
     */
    public void addCheckChangedUpdateCommand(Command updateCmd) {
        tree.setCheckChangedUpdateCommand(updateCmd);
    }

    public void setItems(String json) {
        tree.setItems(json);
    }

    public List<ListRuleArgument> getSelection() {
        return tree.getSelection();
    }

    /**
     * Sets the tree's checked selection with the ListRuleArguments in the given JSON Array string.
     * 
     * @param value A string representation of a JSON Array of the values to select.
     */
    public void setSelection(String value) {
        tree.setSelection(value);

        for (ListRuleArgument ruleArg : tree.getStore().getAll()) {
            if (ruleArg.isDefault()) {
                tree.setExpanded(ruleArg, true);
            }
        }
    }
}
