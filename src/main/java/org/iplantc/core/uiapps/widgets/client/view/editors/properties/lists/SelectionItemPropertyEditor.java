package org.iplantc.core.uiapps.widgets.client.view.editors.properties.lists;

import java.util.Collection;
import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemProperties;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.util.SelectionItemValueChangeStoreHandler.HasEventSuppression;
import org.iplantc.core.uicommons.client.validators.CmdLineArgCharacterValidator;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.NorthSouthContainer;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * 
 * @author jstroot
 * 
 */
public class SelectionItemPropertyEditor extends Composite implements HasValueChangeHandlers<List<SelectionItem>>, HasEventSuppression {

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
    ListStoreEditor<SelectionItem> selectionItemsEditor;

    private ColumnConfig<SelectionItem, String> displayCol;

    private ColumnConfig<SelectionItem, String> nameCol;

    private ColumnConfig<SelectionItem, String> valueCol;

     private final GridRowEditing<SelectionItem> editing;

    private boolean suppressEvent = false;

    private final UUIDServiceAsync uuidService;

    protected int selectionItemCount = 1;
    private final AppsWidgetsPropertyPanelLabels labels;

    public SelectionItemPropertyEditor(final List<SelectionItem> selectionItems, final ArgumentType type, final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService) {
        this.uuidService = uuidService;
        this.labels = presenter.getAppearance().getPropertyPanelLabels();
        initWidget(BINDER.createAndBindUi(this));
        grid.getView().setEmptyText(labels.selectionCreateWidgetEmptyText());

        editing = new GridRowEditing<SelectionItem>(grid){
            
            @Override
            protected void showTooltip(SafeHtml msg) {
                if (tooltip == null) {
                  ToolTipConfig config = new ToolTipConfig();
                  config.setAutoHide(false);
                  config.setAnchor(Side.RIGHT);
                  config.setTitleHtml(getMessages().errorTipTitleText());
                  tooltip = new ToolTip(toolTipAlignWidget, config);
                  tooltip.setMaxWidth(600);
                }
                ToolTipConfig config = tooltip.getToolTipConfig();
                config.setBodyHtml(msg);
                tooltip.update(config);
                tooltip.enable();
                if (!tooltip.isAttached()) {
                  tooltip.show();
                  tooltip.getElement().updateZIndex(0);
                }
              }
            
        };
        
        editing.setClicksToEdit(ClicksToEdit.TWO);
  
        editing.addEditor(valueCol, buildEditorField(new CmdLineArgCharacterValidator(I18N.V_CONSTANTS.restrictedCmdLineArgCharsExclNewline())));
        editing.addEditor(nameCol, buildEditorField(new CmdLineArgCharacterValidator(I18N.V_CONSTANTS.restrictedCmdLineChars())));
        

        // Add selection handler to grid to control enabled state of "delete" button
        grid.getSelectionModel().addSelectionChangedHandler(new GridSelectionChangedHandler());
        selectionItemsEditor = new MyListStoreEditor(selectionArgStore, presenter);

        initColumns(type);
        selectionItemsEditor.setValue(selectionItems);
    }
    
    
    private TextField buildEditorField(Validator<String> validator) {
        final TextField field1 = new TextField();
        field1.addValidator(validator);
        field1.setSelectOnFocus(true);
        field1.setAutoValidate(true);
        field1.addInvalidHandler(new InvalidHandler() {
            
            @Override
            public void onInvalid(InvalidEvent event) {
               field1.clear();
            }
        });
        return field1;
    }

    @UiFactory
    ListStore<SelectionItem> createListStore() {
        ListStore<SelectionItem> listStore = new ListStore<SelectionItem>(new ModelKeyProvider<SelectionItem>() {
            @Override
            public String getKey(SelectionItem item) {
                return item.getId();
            }
        });
         return listStore;
    }

    @UiFactory
    ColumnModel<SelectionItem> createColumnModel() {
        List<ColumnConfig<SelectionItem, ?>> list = Lists.newArrayList();
        SelectionItemProperties props = GWT.create(SelectionItemProperties.class);
        displayCol = new ColumnConfig<SelectionItem, String>(props.display(), 120, labels.singleSelectDisplayColumnHeader());
        nameCol = new ColumnConfig<SelectionItem, String>(props.name(), 150, labels.singleSelectNameColumnHeader());
        valueCol = new ColumnConfig<SelectionItem, String>(props.value(), 150, labels.singleSelectValueColumnHeader());

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
                sa.setValue("Value " + selectionItemCount++);
                sa.setDisplay("" + selectionItemCount);
                sa.setName("Default" + selectionItemCount);

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
                selectionItemsEditor.getStore().add(sa);
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
            setSuppressEvent(true);
            selectionItemsEditor.getStore().remove(sa);
            setSuppressEvent(false);
        }
        ValueChangeEvent.fire(SelectionItemPropertyEditor.this, selectionArgStore.getAll());
    }

    private void initColumns(ArgumentType type) {
        // May be able to set the proper editor by adding it at this time. The column model will use the
        // value as a String, but I can adjust the editing to be string, integer, double.
        if (editing.getEditor(displayCol) == null) {
            switch (type) {
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

    public Collection<? extends SelectionItem> getValues() {
        selectionArgStore.commitChanges();
        return selectionItemsEditor.getStore().getAll();
    }

}
