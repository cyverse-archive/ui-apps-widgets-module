package org.iplantc.core.uiapps.widgets.client.view.editors.properties.lists;

import java.util.List;

import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemProperties;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.util.SelectionItemValueChangeStoreHandler;
import org.iplantc.core.uiapps.widgets.client.view.util.SelectionItemValueChangeStoreHandler.HasEventSuppression;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.NorthSouthContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.AbstractGridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * 
 * @author jstroot
 * 
 */
public class SelectionItemPropertyEditor extends Composite implements ValueAwareEditor<Argument>, HasValueChangeHandlers<List<SelectionItem>>, HasEventSuppression {

    private static SelectionListEditorUiBinder BINDER = GWT.create(SelectionListEditorUiBinder.class);
    interface SelectionListEditorUiBinder extends UiBinder<Widget, SelectionItemPropertyEditor> {}

    @UiField
    ListStore<SelectionItem> selectionArgStore;

    @UiField
    NorthSouthContainer con;

    @Ignore
    @UiField
    TextButton add;

    @Ignore
    @UiField
    TextButton delete;

    @UiField
    Grid<SelectionItem> grid;

    // The Editor for Argument.getSelectionItems()
    ListStoreEditor<SelectionItem> selectionItems;

    private ColumnConfig<SelectionItem, String> displayCol;

    private ColumnConfig<SelectionItem, String> nameCol;

    private ColumnConfig<SelectionItem, String> valueCol;

    private final GridInlineEditing<SelectionItem> editing;

    private boolean suppressEvent = false;

    private Argument model;

    private final UUIDServiceAsync uuidService;

    protected int selectionItemCount = 1;
    private final AppsWidgetsPropertyPanelLabels labels;

    public SelectionItemPropertyEditor(final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService) {
        this.uuidService = uuidService;
        this.labels = presenter.getAppearance().getPropertyPanelLabels();
        initWidget(BINDER.createAndBindUi(this));
        grid.getView().setEmptyText(labels.selectionCreateWidgetEmptyText());

        editing = new GridInlineEditing<SelectionItem>(grid);
        ((AbstractGridEditing<SelectionItem>)editing).setClicksToEdit(ClicksToEdit.TWO);
        TextField field = new TextField();
        field.setSelectOnFocus(true);
        editing.addEditor(valueCol, field);
        editing.addEditor(nameCol, field);
        

        // Add selection handler to grid to control enabled state of "delete" button
        grid.getSelectionModel().addSelectionChangedHandler(new GridSelectionChangedHandler());
        selectionItems = new MyListStoreEditor(selectionArgStore, presenter);
    }


    @UiFactory
    ListStore<SelectionItem> createListStore() {
        ListStore<SelectionItem> listStore = new ListStore<SelectionItem>(new ModelKeyProvider<SelectionItem>() {
            @Override
            public String getKey(SelectionItem item) {
                return item.getId();
            }
        });
        listStore.addStoreHandlers(new MyStoreHandler(this, this));
        return listStore;
    }

    @UiFactory
    ColumnModel<SelectionItem> createColumnModel() {
        List<ColumnConfig<SelectionItem, ?>> list = Lists.newArrayList();
        SelectionItemProperties props = GWT.create(SelectionItemProperties.class);
        displayCol = new ColumnConfig<SelectionItem, String>(props.display(), 90, labels.singleSelectDisplayColumnHeader());
        nameCol = new ColumnConfig<SelectionItem, String>(props.name(), 50, labels.singleSelectNameColumnHeader());
        valueCol = new ColumnConfig<SelectionItem, String>(props.value(), 50, labels.singleSelectValueColumnHeader());

        list.add(displayCol);
        list.add(nameCol);
        list.add(valueCol);
        return new ColumnModel<SelectionItem>(list);
    }

    @UiHandler("add")
    void onAddButtonClicked(SelectEvent event) {
        AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
        final SelectionItem sa = factory.selectionItem().as();

        uuidService.getUUIDs(1, new AsyncCallback<List<String>>() {

            @Override
            public void onSuccess(List<String> result) {

                // JDS Set up a default id to satisfy ListStore's ModelKeyProvider
                sa.setId(result.get(0));
                sa.setDefault(false);
                sa.setDescription("Default Description");
                sa.setValue("Value " + selectionItemCount++);

                /*
                 * JDS Suppress ValueChange event, then manually fire afterward.
                 * This is to prevent a race condition in the GridView which essentially adds the same
                 * item twice
                 * to the view. This is a result of the StoreAdd events firing as a result of this method
                 * and
                 * then as part of the Editor hierarchy refresh. Both of those events are listened to by
                 * the
                 * GridView, but in dev mode, the GridView was getting those events one after the other,
                 * resulting in a view which does not reflect the actual bound ListStore.<br>
                 * By suppressing the ValueChange events from firing, we are preventing the Editor
                 * hierarchy from
                 * doing its refresh, thus allowing the GridView to "catch up". Then, we manually fire
                 * the
                 * ValueChange event in order to propagate these changes throughout the editor hierarchy.
                 */
                setSuppressEvent(true);
                selectionItems.getStore().add(sa);
                setSuppressEvent(false);
                ValueChangeEvent.fire(SelectionItemPropertyEditor.this, selectionArgStore.getAll());
            }

            @Override
            public void onFailure(Throwable caught) {
                // TODO Auto-generated method stub

            }
        });

    }

    @UiHandler("delete")
    void onDeleteButtonClicked(SelectEvent event) {
        List<SelectionItem> selection = grid.getSelectionModel().getSelection();
        if (selection == null) {
            return;
        }
        for (SelectionItem sa : selection) {
            selectionItems.getStore().remove(sa);
        }
    }

    @Override
    public void setValue(Argument value) {
        if((value == null) 
                || ((value != null) 
                && (!AppTemplateUtils.isSelectionArgumentType(value.getType()) || value.getType().equals(ArgumentType.TreeSelection)))) {
            return;
        }
        this.model = value;

        // May be able to set the proper editor by adding it at this time. The column model will use the
        // value as a String, but I can adjust the editing to be string, integer, double.
        if (editing.getEditor(displayCol) == null) {
            switch (model.getType()) {
                case Selection:
                case TextSelection:
                    TextField textField = new TextField();
                    textField.setSelectOnFocus(true);
                    editing.addEditor(displayCol, textField);
                    break;

                case DoubleSelection:
                    NumberField<Double> dblField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
                    dblField.setSelectOnFocus(true);
                    editing.addEditor(displayCol, new StringToDoubleConverter(), dblField);
                    break;

                case IntegerSelection:
                    NumberField<Integer> intField = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
                    intField.setSelectOnFocus(true);
                    editing.addEditor(displayCol, new StringToIntegerConverter(), intField);
                    break;

                default:
                    // The current argument is not a valid type for this control.
                    // So, disable and hide ourself so the user doesn't see it.
                    con.setEnabled(false);
                    con.setVisible(false);
                    break;
            }
        }
    }

    @Override
    public void flush() {/* Do Nothing */}

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {/* Do Nothing */}

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<SelectionItem>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }


    @Override
    public boolean isSuppressEvent() {
        return suppressEvent;
    }

    @Override
    public void setSuppressEvent(boolean suppressEventFire) {
        this.suppressEvent = suppressEventFire;
    }

    private final class MyStoreHandler extends SelectionItemValueChangeStoreHandler {
        private MyStoreHandler(HasEventSuppression hasEventSuppression, HasValueChangeHandlers<List<SelectionItem>> valueChangeTarget) {
            super(hasEventSuppression, valueChangeTarget);
        }
    
        @Override
        protected List<SelectionItem> getCurrentValue() {
            return selectionArgStore.getAll();
        }
    
        @Override
        public void onRecordChange(StoreRecordChangeEvent<SelectionItem> event) {
            SelectionItem si = event.getRecord().getModel();
            if (si.isDefault() && (model != null)) {
                Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(si));
                model.setDefaultValue(encode);
                model.setValue(encode);
            }
            super.onRecordChange(event);
        }
    }

    private final class MyListStoreEditor extends ListStoreEditor<SelectionItem> {
        private final AppTemplateWizardPresenter presenter;
    
        private MyListStoreEditor(ListStore<SelectionItem> store, AppTemplateWizardPresenter presenter) {
            super(store);
            this.presenter = presenter;
        }
    
        @Override
        public void flush() {
            if (!shouldFlush()) {
                return;
            }
            setSuppressEvent(true);
            super.flush();
            setSuppressEvent(false);
        }
    
        @Override
        public void setValue(List<SelectionItem> value) {
            setSuppressEvent(true);
            super.setValue(value);
            setSuppressEvent(false);
        }
    
        private boolean shouldFlush() {
            return presenter.getValueChangeEventSource() == SelectionItemPropertyEditor.this;
        }
    }

    private final class GridSelectionChangedHandler implements SelectionChangedHandler<SelectionItem> {
        @Override
        public void onSelectionChanged(SelectionChangedEvent<SelectionItem> event) {
            if ((event.getSelection() == null) || event.getSelection().isEmpty()) {
                delete.setEnabled(false);
            } else if ((event.getSelection() != null) && (event.getSelection().size() >= 1)) {
                delete.setEnabled(true);
            }
        }
    }

    private final class StringToIntegerConverter implements Converter<String, Integer> {
        @Override
        public String convertFieldValue(Integer object) {
            if (object == null) {
                return null;
            }
            return object.toString();
        }
    
        @Override
        public Integer convertModelValue(String object) {
            if (object == null) {
                return null;
            }
            return Integer.parseInt(object);
        }
    }

    private final class StringToDoubleConverter implements Converter<String, Double> {
        @Override
        public String convertFieldValue(Double object) {
            if (object == null) {
                return null;
            }
            return object.toString();
        }
    
        @Override
        public Double convertModelValue(String object) {
            if (object == null) {
                return null;
            }
            return Double.parseDouble(object);
        }
    }

}
