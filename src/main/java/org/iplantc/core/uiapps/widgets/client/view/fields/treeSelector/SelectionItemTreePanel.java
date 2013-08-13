package org.iplantc.core.uiapps.widgets.client.view.fields.treeSelector;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemList;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemProperties;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.fields.ArgumentSelectionField;
import org.iplantc.core.uiapps.widgets.client.view.util.SelectionItemTreeStoreEditor;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.core.client.Style.SelectionMode;
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

    /**
     * Restores the CheckState of the tree, and expand any Group that is checked or has checked children.
     * This handler must be added after the store is added to the tree, since the tree adds its own
     * handlers that must trigger before this one.
     * 
     * @author jstroot
     * 
     */
    private final class MyStoreFilterHandler implements StoreFilterHandler<SelectionItem> {
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
    }

    private final class TreeValueChangeHandler implements ValueChangeHandler<List<SelectionItem>> {
        @Override
        public void onValueChange(ValueChangeEvent<List<SelectionItem>> event) {
            if (!selectionItemsEditor.isSuppressEvent()) {
                ValueChangeEvent.fire(SelectionItemTreePanel.this, Lists.<SelectionItem> newArrayList(selectionItemsEditor.getCurrentTree()));
            }
        }
    }

    private final class MyTreeStoreEditor extends SelectionItemTreeStoreEditor {
        private final AppTemplateWizardPresenter presenter;

        private MyTreeStoreEditor(TreeStore<SelectionItem> store, HasValueChangeHandlers<List<SelectionItem>> valueChangeTarget, AppTemplateWizardPresenter presenter) {
            super(store, valueChangeTarget);
            this.presenter = presenter;
        }

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
    }

    private final SelectionItemTree tree;

    SelectionItemTreeStoreEditor selectionItemsEditor;

    private final SelectionItemProperties props = GWT.create(SelectionItemProperties.class);
    private Argument model;

    private final AppTemplateWizardPresenter presenter;

    public SelectionItemTreePanel(final AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        TreeStore<SelectionItem> store = new TreeStore<SelectionItem>(props.id());

        tree = new SelectionItemTree(store, props.display());
        if (presenter.isEditingMode()) {
            /*
             * KLUDGE CORE-4653 JDS Set selection model to locked. This is to prevent the user from
             * making any selections due to the issue described in CORE-4653.
             */
            tree.getSelectionModel().setLocked(true);
        }
        tree.setHeight(presenter.getAppearance().getDefaultTreeSelectionHeight());

        tree.addValueChangeHandler(new TreeValueChangeHandler());
        selectionItemsEditor = new MyTreeStoreEditor(store, this, presenter);

        // This handler must be added after the store is added to the tree, since the tree adds its own
        // handlers that must trigger before this one.
        // Restore the tree's Checked state from each item's isDefault field after it's filtered.
        store.addStoreFilterHandler(new MyStoreFilterHandler());

        add(buildFilter(store), new VerticalLayoutData(1, -1));
        add(tree);
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

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        tree.setEnabled(enabled);
        // tree.getSelectionModel().setLocked(!enabled);
    }

    /**
     * @param selection a splittable which this function assumes is a list of {@link SelectionItem}s
     */
    public void setSelection(Splittable selection) {
        selectionItemsEditor.setSuppressEvent(true);
        AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
        SelectionItemList siList = AutoBeanCodex.decode(factory, SelectionItemList.class, "{\"selectionItems\": " + selection.getPayload() + "}").as();
        List<SelectionItem> items = siList.getSelectionItems();
        tree.setSelection(items);
        selectionItemsEditor.setSuppressEvent(false);

    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void flush() {
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
        boolean restoreSelectionsFromDefaultValue = !presenter.isEditingMode() && (value.getDefaultValue() != null) && (value.getDefaultValue().isIndexed()) && (value.getDefaultValue().size() > 0);
        tree.setRestoreCheckedSelectionFromTree(!restoreSelectionsFromDefaultValue);
        selectionItemsEditor.setValue(value.getSelectionItems());
        if (restoreSelectionsFromDefaultValue) {
            /*
             * JDS If we're not in editing mode, and there is a list of items in the defaultValue field.
             * This should only happen when an app is being re-launched from the Analysis window, and
             * there were values selected for the Tree.
             */
            Splittable defValCopy = value.getDefaultValue().deepCopy();
            setSelection(defValCopy);
            /*
             * JDS Clear default value so future selections can be made (since they are made through
             * value.getSelectionItems() and not value.defaultValue()
             */
            value.setDefaultValue(null);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<SelectionItem>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public Argument getValue() {
        return model;
    }

    public TreeStore<SelectionItem> getTreeStore() {
        return tree.getStore();
    }
}
