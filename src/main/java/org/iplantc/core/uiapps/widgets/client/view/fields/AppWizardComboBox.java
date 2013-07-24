package org.iplantc.core.uiapps.widgets.client.view.fields;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemProperties;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.fields.util.converters.SplittableToSelectionArgConverter;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * This has to bind the ComboBox's Store to the Argument's selection Items.<br>
 * This has to bind the ComboBox's selection to the argument's value
 * 
 * @author jstroot
 * 
 */
public class AppWizardComboBox extends Composite implements ArgumentSelectionField {

    private final class MySelectionHandler implements SelectionHandler<SelectionItem> {
        @Override
        public void onSelection(SelectionEvent<SelectionItem> event) {
            SelectionItem selectedItem = event.getSelectedItem();
            ValueChangeEvent.fire(AppWizardComboBox.this, Lists.<SelectionItem> newArrayList(selectedItem));
        }
    }

    private final SelectionItemProperties props = GWT.<SelectionItemProperties> create(SelectionItemProperties.class);

    private final ListStore<SelectionItem> listStore;
    @Path("selectionItems")
    ListStoreEditor<SelectionItem> selectionItemsStoreBinder;

    private final ComboBox<SelectionItem> selectionItemsEditor;

    @Path("value")
    ConverterFieldAdapter<SelectionItem, ComboBox<SelectionItem>> valueEditor;

    private final AppTemplateWizardPresenter presenter;

    private Argument model;

    @UiConstructor
    public AppWizardComboBox(AppTemplateWizardPresenter presenter) {
        this.presenter = presenter;
        // JDS Initialize list store, and its editor
        listStore = new ListStore<SelectionItem>(props.id());
        selectionItemsStoreBinder = new ListStoreEditor<SelectionItem>(listStore);

        // JDS Initialize combobox and its editor converter
        selectionItemsEditor = new ComboBox<SelectionItem>(listStore, props.displayLabel());
        selectionItemsEditor.setEmptyText(presenter.getAppearance().getMessages().emptyListSelectionText());
        selectionItemsEditor.setTriggerAction(TriggerAction.ALL);
        valueEditor = new ConverterFieldAdapter<SelectionItem, ComboBox<SelectionItem>>(selectionItemsEditor, new SplittableToSelectionArgConverter());
        initWidget(selectionItemsEditor);

        selectionItemsEditor.addSelectionHandler(new MySelectionHandler());
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        selectionItemsEditor.setEnabled(enabled);
    }

    @Override
    public void flush() {
        // JDS This may cause a flush to occur twice if this object is actually bound
        // selectionItemsStoreBinder.flush();
        SelectionItem currSi = selectionItemsEditor.getCurrentValue();
        if (currSi == null) {
            return;
        }
        Splittable currSiSplittable = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(currSi));

        // JDS Set value, if the current value payload does not equal the model's value payload
        if ((model.getValue() == null) || ((model.getValue() != null) && !model.getValue().getPayload().equals(currSiSplittable.getPayload()))) {
            if (presenter.isEditingMode()) {
                // JDS Reset default value of all items in the current list
                for (SelectionItem si : listStore.getAll()) {
                    si.setDefault(false);
                }
                currSi.setDefault(true);
            }
            currSiSplittable = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(currSi));
            model.setValue(currSiSplittable);
            model.setDefaultValue(currSiSplittable);
        }
    }

    @Override
    public void setValue(final Argument value) {
        ArgumentType type = value.getType();
        if ((value == null) || !AppTemplateUtils.isSelectionArgumentType(value.getType()) || type.equals(ArgumentType.TreeSelection)) {
            return;
        }
        this.model = value;

        if (model.getSelectionItems() != null) {
            selectionItemsStoreBinder.setValue(model.getSelectionItems());
        }
        selectionItemsEditor.clear();
        selectionItemsEditor.setText("");
        if (model.getValue() != null) {
            // JDS Defer updating of combo box to avoid "race condition" with bound liststore
            Scheduler.get().scheduleFinally(new ScheduledCommand() {

                @Override
                public void execute() {
                    if ((model.getValue() != null) && model.getValue().isKeyed() && !model.getValue().isUndefined("id")) {
                        String id = model.getValue().get("id").asString();
                        SelectionItem si = listStore.findModelWithKey(id);
                        if (si != null) {
                            // KLUDGE JDS Call setValue twice in order to get the view to refresh with updated
                            // values.
                            selectionItemsEditor.setValue(si);
                            selectionItemsEditor.setValue(si);
                            return;
                        }
                    }
                }
            });
        }
        if (presenter.isOnlyLabelEditMode()) {
            setEnabled(false);
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<SelectionItem>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public Argument getValue() {
        return model;
    }

}
