package org.iplantc.core.uiapps.widgets.client.view.fields.treeSelector;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentSelectionField;
import org.iplantc.core.uiapps.widgets.client.view.util.SelectionItemTreeStoreEditor;

import com.google.common.collect.Lists;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

/**
 * A Panel that contains a SelectionItemTree and a filtering field that filters the tree items.
 * 
 * @author psarando
 * 
 */
public class SelectionItemTreePanel extends VerticalLayoutContainer implements ValueAwareEditor<Argument>, ArgumentSelectionField {

    private SelectionItemTree tree;

    SelectionItemTreeStoreEditor selectionItemsEditor;

    private Argument model;

    public SelectionItemTreePanel(final AppTemplateWizardPresenter presenter) {
        TreeStore<SelectionItem> store = buildStore();

        initTree(store);
        selectionItemsEditor = new SelectionItemTreeStoreEditor(store, this) {
            @Override
            protected void setCheckStyle(CheckCascade treeCheckCascade) {
                if (treeCheckCascade == null) {
                    return;
                }

                tree.setCheckStyle(CheckCascade.valueOf(treeCheckCascade.name()));
            }

            @Override
            protected void setSingleSelect(boolean singleSelect) {
                if (singleSelect) {
                    tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                } else {
                    tree.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
                }
            }

            @Override
            protected void setItems(SelectionItemGroup root) {
                tree.setItems(root);
            }

            @Override
            protected CheckCascade getCheckStyle() {
                CheckCascade ret = null;
                if (!presenter.isEditingMode()) {
                    return null;
                } else {
                    ret = tree.getCheckStyle();
                }
                return ret;
            }

            @Override
            protected boolean getSingleSelect() {
                return tree.getSelectionModel().getSelectionMode().equals(SelectionMode.SINGLE);
            }

            @Override
            protected boolean shouldFlush() {
                // return presenter.getValueChangeEventSource() == SelectionItemTreePanel.this;
                return true;
            }
        };

        // This handler must be added after the store is added to the tree, since the tree adds its own
        // handlers that must trigger before this one.
        addCheckedRestoreFilterHandler(store);

        add(buildFilter(store), new VerticalLayoutData(1, -1));
        add(tree);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        tree.setEnabled(enabled);
        tree.getSelectionModel().setLocked(!enabled);
    }

    private TreeStore<SelectionItem> buildStore() {
        TreeStore<SelectionItem> store = new TreeStore<SelectionItem>(new ModelKeyProvider<SelectionItem>() {
            @Override
            public String getKey(SelectionItem item) {
                return item.getDisplay();
            }
        });

        return store;
    }

    private void initTree(TreeStore<SelectionItem> store) {
        ValueProvider<SelectionItem, String> valueProvider = new ValueProvider<SelectionItem, String>() {

            @Override
            public String getValue(SelectionItem object) {
                return object.getDisplay();
            }

            @Override
            public void setValue(SelectionItem object, String value) {
                // intentionally do nothing, since this is not an editor
            }

            @Override
            public String getPath() {
                return "display"; //$NON-NLS-1$
            }
        };

        tree = new SelectionItemTree(store, valueProvider);
        tree.setHeight(200);

        tree.addValueChangeHandler(new ValueChangeHandler<List<SelectionItem>>() {

            @Override
            public void onValueChange(ValueChangeEvent<List<SelectionItem>> event) {
                if (!selectionItemsEditor.suppressEvent()) {
                    ValueChangeEvent.fire(SelectionItemTreePanel.this, Lists.<SelectionItem> newArrayList(selectionItemsEditor.getCurrentTree()));
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
    private void addCheckedRestoreFilterHandler(TreeStore<SelectionItem> store) {
        // Restore the tree's Checked state from each item's isDefault field after it's filtered.
        store.addStoreFilterHandler(new StoreFilterHandler<SelectionItem>() {

            @Override
            public void onFilter(StoreFilterEvent<SelectionItem> event) {
                TreeStore<SelectionItem> treeStore = tree.getStore();

                for (SelectionItem ruleArg : treeStore.getAll()) {
                    if (ruleArg.isDefault()) {
                        tree.setChecked(ruleArg, CheckState.CHECKED);
                    }
                }

                boolean filtered = treeStore.isFiltered();

                for (SelectionItem ruleArg : treeStore.getAll()) {
                    // Ensure any groups with filtered-out children still display the group icon.
                    if (ruleArg instanceof SelectionItemGroup) {
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

    private StoreFilterField<SelectionItem> buildFilter(TreeStore<SelectionItem> store) {
        StoreFilterField<SelectionItem> treeFilter = new StoreFilterField<SelectionItem>() {
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
            protected boolean doSelect(Store<SelectionItem> store, SelectionItem parent, SelectionItem item, String filter) {
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

    public List<SelectionItem> getSelection() {
        return tree.getSelection();
    }

    /**
     * Sets the tree's checked selection with the SelectionItems in the given JSON Array string.
     * 
     * @param value A string representation of a JSON Array of the values to select.
     */
    public void setSelection(String value) {
        tree.setSelection(value);

        for (SelectionItem ruleArg : tree.getStore().getAll()) {
            if (ruleArg.isDefault()) {
                tree.setExpanded(ruleArg, true);
            }
        }
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void flush() {/* Do Nothing */
        selectionItemsEditor.flush();
    }

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void setValue(Argument value) {
        if ((value == null) || !value.getType().equals(ArgumentType.TreeSelection)) {
            return;
        }
        this.model = value;
        selectionItemsEditor.setValue(value.getSelectionItems());
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<SelectionItem>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void nullifyEditors() {
        selectionItemsEditor = null;
    }

    @Override
    public Argument getValue() {
        return model;
    }
}
