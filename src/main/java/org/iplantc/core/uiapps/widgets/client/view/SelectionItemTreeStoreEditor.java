package org.iplantc.core.uiapps.widgets.client.view;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;

import com.google.common.collect.Lists;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.TreeStore;

/**
 * Binds a {@link TreeStore} of {@link SelectionItem}s to a {@link List} property in an edited model
 * object.
 * This class is modelled after {@link ListStoreEditor}.
 * <p>
 * If bound to a null value, no changes will be made when flushed.
 * </p>
 * 
 * @author jstroot
 * 
 */
public class SelectionItemTreeStoreEditor implements ValueAwareEditor<List<SelectionItem>> {
    private final TreeStore<SelectionItem> store;
    private List<SelectionItem> model;

    public SelectionItemTreeStoreEditor(TreeStore<SelectionItem> store) {
        this.store = store;
    }

    @Override
    public void flush() {
        store.commitChanges();

        if (model != null) {
            model.clear();
            model.addAll(store.getRootItems());
        }
    }

    public TreeStore<SelectionItem> getStore() {
        return store;
    }

    @Override
    public void setValue(List<SelectionItem> value) {
        store.clear();
        if(value != null && value.size() > 0){
            // The first item in this list should be a SelectionItemGroup. This group will contain the
            // values for isSingleSelect and selectionCascade, which the trees will need for display.

            // Q: How will the tree get the single select and selection cascade items?
            // JDS Populate TreeStore.
            addSelectionItemGroups(null, value);
        }
    }

    private void addSelectionItemGroups(SelectionItemGroup parent, List<SelectionItem> children) {
    
        if (parent == null) {
            store.add(children);
        } else {
            store.add(parent, children);
        }
    
        for (SelectionItem si : children) {
            if (si instanceof SelectionItemGroup) {
                SelectionItemGroup selectionItemGroup = (SelectionItemGroup)si;
                addSelectionItemGroups(selectionItemGroup, selectionItemGroup.getArguments());
                addSelectionItemGroups(selectionItemGroup, Lists.<SelectionItem> newArrayList(selectionItemGroup.getGroups()));
            }
        }
    }

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void setDelegate(EditorDelegate<List<SelectionItem>> delegate) {
        // ignore for now, this could be used to pass errors into the view
    }

}
