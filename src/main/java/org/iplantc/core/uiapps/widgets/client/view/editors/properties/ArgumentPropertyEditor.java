package org.iplantc.core.uiapps.widgets.client.view.editors.properties;

import java.util.List;

import org.iplantc.core.resources.client.IplantContextualHelpAccessStyle;
import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.resources.client.uiapps.widgets.ArgumentValidatorMessages;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidator;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSource;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSourceProperties;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoType;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoTypeProperties;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.lists.SelectionItemPropertyEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.trees.SelectionItemTreePropertyEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.properties.validation.ArgumentValidatorEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardPropertyContentPanelAppearance;
import org.iplantc.core.uiapps.widgets.client.view.fields.AppWizardComboBox;
import org.iplantc.core.uiapps.widgets.client.view.fields.CheckBoxAdapter;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.widgets.ContextualHelpPopup;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

/**
 * The <code>Editor</code> provides a means for editing the properties of an <code>Argument</code>.
 * 
 * @author jstroot
 * 
 */
public class ArgumentPropertyEditor extends Composite implements ValueAwareEditor<Argument> {

    interface ArgumentPropertyBaseUiBinder extends UiBinder<Widget, ArgumentPropertyEditor> {}
    private static ArgumentPropertyBaseUiBinder BINDER = GWT.create(ArgumentPropertyBaseUiBinder.class);

    @UiField
    ContentPanel cp;
    
    @UiField
    VerticalLayoutContainer con;

    @UiField
    CheckBoxAdapter requiredEditor, omitIfBlank;

    @Path("visible")
    @UiField
    CheckBoxAdapter doNotDisplay;

    @Ignore
    @UiField
    CheckBoxAdapter isImplicit;

    @UiField
    TextField label;

    @UiField
    TextField description;

    @UiField
    TextField name;

    @UiField(provided = true)
    @Ignore
    DefaultArgumentValueEditor defaultValue;

    @Path("")
    @UiField(provided = true)
    ArgumentValidatorEditor validatorsEditor;

    @UiField(provided = true)
    @Ignore
    AppWizardComboBox selectionItemDefaultValue;

    @UiField
    FieldLabel selectionItemDefaultValueLabel, nameLabel, argLabelLabel, descriptionLabel, fileInfoTypeLabel, dataSourceLabel;
//    FieldLabel selectionItemDefaultValueLabel, nameLabel, argLabelLabel, descriptionLabel, fileInfoTypeLabel, dataSourceLabel, listSelectionLabel;

    // @Path("")
    // @UiField(provided = true)
    // SelectionItemPropertyEditor selectionItemListEditor;

    @UiField
    @Ignore
    TextButton editSimpleListBtn;

    // @Path("")
    // @UiField(provided = true)
    // SelectionItemTreePropertyEditor selectionItemTreeEditor;

    @UiField
    @Ignore
    TextButton editTreeListBtn;

    @Ignore
    @UiField(provided = true)
    ComboBox<FileInfoType> fileInfoTypeComboBox;

    @Ignore
    @UiField(provided = true)
    ComboBox<DataSource> dataSourceComboBox;

    private final AppTemplateWizardPresenter presenter;

    private Argument model;


    private HandlerRegistration fileInfoTypeStoreAddHandlerReg;

    private HandlerRegistration dataSourceStoreAddHandlerReg;

    private EditorDelegate<Argument> delegate;

    private final ArgumentValidatorMessages argValMessages = GWT.create(ArgumentValidatorMessages.class);

    private final UUIDServiceAsync uuidService;
    private final IplantContextualHelpAccessStyle style = IplantResources.RESOURCES.getContxtualHelpStyle();

    public ArgumentPropertyEditor(final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService, final AppMetadataServiceFacade appMetadataService) {
        this.presenter = presenter;
        this.uuidService = uuidService;
        defaultValue = new DefaultArgumentValueEditor(presenter.getAppearance(), appMetadataService);
        validatorsEditor = new ArgumentValidatorEditor(presenter.getAppearance(), argValMessages);
        selectionItemDefaultValue = new AppWizardComboBox(presenter);
//         selectionItemListEditor = new SelectionItemPropertyEditor(presenter, uuidService);
//         selectionItemTreeEditor = new SelectionItemTreePropertyEditor(presenter);

        fileInfoTypeComboBox = createFileInfoTypeComboBox(appMetadataService);
        dataSourceComboBox = createDataSourceComboBox(appMetadataService);

        /*
         * Validation control and selection creation control will be created here, bound to argument.
         * Each will be a value aware editor, and will only instantiate the visible components for the
         * appropriate Argument type.
         * 
         * You will want to get the selective instantiate logic in place before getting the functionality
         * together.
         */
        initWidget(BINDER.createAndBindUi(this));
        QuickTip quickTip = new QuickTip(this);
        quickTip.getToolTipConfig().setDismissDelay(0);
    }

    @UiFactory
    ContentPanel createContentPanel() {
        return new ContentPanel(new AppTemplateWizardPropertyContentPanelAppearance());
    }

    private ComboBox<FileInfoType> createFileInfoTypeComboBox(AppMetadataServiceFacade appMetadataService) {
        final FileInfoTypeProperties props = appMetadataService.getFileInfoTypeProperties();

        final ListStore<FileInfoType> store = new ListStore<FileInfoType>(props.id());
        fileInfoTypeStoreAddHandlerReg = store.addStoreAddHandler(new StoreAddHandler<FileInfoType>() {
            @Override
            public void onAdd(StoreAddEvent<FileInfoType> event) {
                updateFileInfoTypeSelection(model);
            }
        });
        appMetadataService.getFileInfoTypes(new AsyncCallback<List<FileInfoType>>() {

            @Override
            public void onSuccess(List<FileInfoType> result) {
                if (store.getAll().isEmpty()) {
                    store.addAll(result);
                    store.addSortInfo(new StoreSortInfo<FileInfoType>(props.labelValue(), SortDir.ASC));
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
        ComboBox<FileInfoType> comboBox = new ComboBox<FileInfoType>(store, props.label());
        comboBox.setTriggerAction(TriggerAction.ALL);
        // JDS Add valueChangeHandler manually since there are errors if you try to do it with UIBinder
        comboBox.addValueChangeHandler(new ValueChangeHandler<FileInfoType>() {
            @Override
            public void onValueChange(ValueChangeEvent<FileInfoType> event) {
                presenter.onArgumentPropertyValueChange(event.getSource());
            }
        });
        return comboBox;
    }

    private ComboBox<DataSource> createDataSourceComboBox(AppMetadataServiceFacade appMetadataService) {
        final DataSourceProperties props = appMetadataService.getDataSourceProperties();
        final ListStore<DataSource> store = new ListStore<DataSource>(props.id());
        dataSourceStoreAddHandlerReg = store.addStoreAddHandler(new StoreAddHandler<DataSource>() {
            @Override
            public void onAdd(StoreAddEvent<DataSource> event) {
                updateDataSourceSelection(model);
            }
        });
        appMetadataService.getDataSources(new AsyncCallback<List<DataSource>>() {
            @Override
            public void onSuccess(List<DataSource> result) {
                if (store.getAll().isEmpty()) {
                    store.addAll(result);
                    store.addSortInfo(new StoreSortInfo<DataSource>(props.type(), SortDir.ASC));
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
        ComboBox<DataSource> comboBox = new ComboBox<DataSource>(store, new LabelProvider<DataSource>() {

            @Override
            public String getLabel(DataSource src) {
                return src.getType().getLabel();
            }
        });
        comboBox.setTriggerAction(TriggerAction.ALL);
        comboBox.addValueChangeHandler(new ValueChangeHandler<DataSource>() {
            @Override
            public void onValueChange(ValueChangeEvent<DataSource> event) {
                presenter.onArgumentPropertyValueChange(event.getSource());
            }
        });
        return comboBox;
    }

    /*
     * JDS The following UI Handlers are necessary to inform the presenter that values have changed.
     * This has the effect of propagating data back and forth through the editor hierarchy.
     */
    @UiHandler({"requiredEditor", "omitIfBlank", "doNotDisplay", "isImplicit"})
    void onBooleanValueChanged(ValueChangeEvent<Boolean> event) {
        if (event.getSource() == requiredEditor) {
            if (event.getValue()) {
                omitIfBlank.setValue(false);
                omitIfBlank.setEnabled(false);
            } else {
                omitIfBlank.setEnabled(true);
            }
        }
        if (event.getSource() == doNotDisplay) {
            updateDisplayInGuiVisibilities(event.getValue());
        }
        presenter.onArgumentPropertyValueChange(event.getSource());
        
    }

    private void updateDisplayInGuiVisibilities(boolean doNotDisplayInGui) {
        // If the "Do not display in GUI" checkbox is selected
        if (doNotDisplayInGui) {
            // Clear the "require user input" checkbox
            requiredEditor.setValue(false);

            omitIfBlank.setVisible(false);
            requiredEditor.setVisible(false);
            descriptionLabel.setVisible(false);
            if (validatorsEditor != null) {
                validatorsEditor.setVisible(false);
            }
            if ((model != null) && AppTemplateUtils.isSimpleSelectionArgumentType(model.getType())) {
                editSimpleListBtn.setVisible(false);
            }
            if ((model != null) && model.getType().equals(ArgumentType.TreeSelection)) {
                editTreeListBtn.setVisible(false);
            }
//            if (selectionItemListEditor != null) {
//                selectionItemListEditor.setVisible(false);
//            }
//            if (selectionItemTreeEditor != null) {
//                selectionItemTreeEditor.setVisible(false);
//            }
        } else {
            if ((model != null) && !model.getType().equals(ArgumentType.EnvironmentVariable)) {
                omitIfBlank.setVisible(true);
                omitIfBlank.setEnabled(true);
            }

            requiredEditor.setVisible(true);
            descriptionLabel.setVisible(true);
            if (validatorsEditor != null) {
                validatorsEditor.setVisible(true);
            }

            if ((model != null) && AppTemplateUtils.isSimpleSelectionArgumentType(model.getType())) {
                editSimpleListBtn.setVisible(true);
            }
            if ((model != null) && model.getType().equals(ArgumentType.TreeSelection)) {
                editTreeListBtn.setVisible(true);
            }
//            if (selectionItemListEditor != null) {
//                selectionItemListEditor.setVisible(true);
//            }
//            if (selectionItemTreeEditor != null) {
//                selectionItemTreeEditor.setVisible(true);
//            }
        }
    }

    @UiHandler({"label", "description", "name"})
    void onStringValueChanged(ValueChangeEvent<String> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
    }

    @UiHandler("defaultValue")
    void onSplittableValueChanged(ValueChangeEvent<Splittable> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
    }

    @UiHandler("editSimpleListBtn")
    void onSimpleListBtnClicked(SelectEvent event) {
        IPlantDialog dlg = new IPlantDialog();
        dlg.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        dlg.setHeadingText(presenter.getAppearance().getPropertyPanelLabels().singleSelectionCreateLabel());
        dlg.setModal(true);
        dlg.setOkButtonText(I18N.DISPLAY.done());
        dlg.setAutoHide(false);
        final SelectionItemPropertyEditor selectionItemListEditor = new SelectionItemPropertyEditor(model.getSelectionItems(), model.getType(), presenter, uuidService);
        selectionItemListEditor.setSize("640", "480");
        dlg.add(selectionItemListEditor);
        dlg.addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                model.getSelectionItems().clear();
                model.getSelectionItems().addAll(selectionItemListEditor.getValues());
                presenter.onArgumentPropertyValueChange(ArgumentPropertyEditor.this);
            }
        });
        final ToolButton toolBtn = new ToolButton(style.contextualHelp());
        toolBtn.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                ContextualHelpPopup popup = new ContextualHelpPopup();
                popup.setWidth(450);
                popup.add(new HTML(presenter.getAppearance().getContextHelpMessages().singleSelectionCreateList()));
                popup.showAt(toolBtn.getAbsoluteLeft(), toolBtn.getAbsoluteTop() + 15);
            }
        });
        dlg.addTool(toolBtn);

        dlg.show();
    }
    
    @UiHandler("editTreeListBtn")
    void onTreeListBtnClicked(SelectEvent event) {
        IPlantDialog dlg = new IPlantDialog();
        dlg.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        dlg.setHeadingText(presenter.getAppearance().getPropertyPanelLabels().singleSelectionCreateLabel());
        dlg.setModal(true);
        dlg.setOkButtonText(I18N.DISPLAY.done());
        dlg.setAutoHide(false);
        final SelectionItemTreePropertyEditor selectionItemTreeEditor = new SelectionItemTreePropertyEditor(presenter, model.getSelectionItems());
        selectionItemTreeEditor.setSize("640", "480");
        dlg.add(selectionItemTreeEditor);
        dlg.addOkButtonSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                // Manually grab values
                model.getSelectionItems().clear();

                /*
                 * JDS Grab the AutoBean tag for items which should be removed. This is to communicate to
                 * the center tree store that some items should be removed from the store.
                 */
                final AutoBean<SelectionItemGroup> values = selectionItemTreeEditor.getValues();
                AutoBeanUtils.getAutoBean(model).setTag(SelectionItem.TO_BE_REMOVED, values.getTag(SelectionItem.TO_BE_REMOVED));
                values.setTag(SelectionItem.TO_BE_REMOVED, null);
                model.getSelectionItems().add(values.as());
                presenter.onArgumentPropertyValueChange(ArgumentPropertyEditor.this);

            }
        });

        final ToolButton toolBtn = new ToolButton(style.contextualHelp());
        toolBtn.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                ContextualHelpPopup popup = new ContextualHelpPopup();
                popup.setWidth(450);
                popup.add(new HTML(presenter.getAppearance().getContextHelpMessages().treeSelectionCreateTree()));
                popup.showAt(toolBtn.getAbsoluteLeft(), toolBtn.getAbsoluteTop() + 15);
            }
        });
        dlg.addTool(toolBtn);

        dlg.show();
        
    }
    
    @UiHandler("selectionItemDefaultValue")
    void onSelectionItemDefaultValueChanged(ValueChangeEvent<List<SelectionItem>> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
    }

    @UiHandler("validatorsEditor")
    void onValidatorListChanged(ValueChangeEvent<List<ArgumentValidator>> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
    }

    @Override
    public void setDelegate(EditorDelegate<Argument> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void flush() {
        // JDS Manually flush default value editor
        if (defaultValue != null) {
            defaultValue.flush();
            List<EditorError> errors = defaultValue.getErrors();
            for (EditorError e : errors) {
                delegate.recordError(e.getMessage(), e.getValue(), e.getEditor());
            }
        } else if (selectionItemDefaultValue != null) {
            selectionItemDefaultValue.flush();
        }

        // JDS Manually put FileInfoType into model,
        if (AppTemplateUtils.isDiskResourceArgumentType(model.getType())) {
            FileInfoType fit = fileInfoTypeComboBox.getCurrentValue();
            if (fit != null) {
                model.getDataObject().setFileInfoType(fit.getType());
            }
            if (AppTemplateUtils.isDiskResourceOutputType(model.getType())) {
                model.getDataObject().setImplicit(isImplicit.getValue());
                DataSource dataSource = dataSourceComboBox.getCurrentValue();
                if (dataSource != null) {
                    model.getDataObject().setDataSource(dataSource.getType());
                }
            }
        }

    }

    @Override
    public void onPropertyChange(String... paths) {/* Do Nothing */}

    @Override
    public void setValue(Argument value) {
        if (value == null) {
            return;
        }

        boolean isInfoType = value.getType().equals(ArgumentType.Info);
        boolean isMultiSelectorType = value.getType().equals(ArgumentType.MultiFileSelector);
        boolean isTreeSelectionType = value.getType().equals(ArgumentType.TreeSelection);
        boolean isDiskResourceArgumentType = AppTemplateUtils.isDiskResourceArgumentType(value.getType());

        boolean isDiskResourceOutputType = AppTemplateUtils.isDiskResourceOutputType(value.getType());
        if (model == null) {
            // JDS First time through, remove any components which aren't applicable to the current
            // ArgumentType
            updateFieldLabels(value.getType());
            if (!AppTemplateUtils.isSelectionArgumentType(value.getType())) {
                // JDS The ArgumentType is NOT a Selection-based type. Remove all 'list' related controls
//                listSelectionLabel.setVisible(false);
//                con.remove(selectionItemListEditor);
//                con.remove(selectionItemTreeEditor);
                editSimpleListBtn.setVisible(false);
                editTreeListBtn.setVisible(false);
                con.remove(selectionItemDefaultValueLabel);
                selectionItemDefaultValueLabel = null;
                selectionItemDefaultValue = null;
//                selectionItemListEditor = null;
//                selectionItemTreeEditor = null;
                if ((isDiskResourceArgumentType && !isDiskResourceOutputType) || isInfoType) {
                    con.remove(defaultValue);
                    defaultValue = null;
                }
            } else if (isTreeSelectionType) {
                // JDS Remove all controls except for those related to TreeSelection
//                listSelectionLabel.setVisible(false);
//                con.remove(selectionItemListEditor);
                editSimpleListBtn.setVisible(false);
                con.remove(selectionItemDefaultValueLabel);
                con.remove(defaultValue);
                selectionItemDefaultValueLabel = null;
                selectionItemDefaultValue = null;
//                selectionItemListEditor = null;
                defaultValue = null;
            } else if (!isTreeSelectionType) {
                // JDS Remove all controls except for those related to Simple selections (i.e. non-tree)
//                con.remove(selectionItemTreeEditor);
                editTreeListBtn.setVisible(false);
                con.remove(defaultValue);
//                selectionItemTreeEditor = null;
                defaultValue = null;
            }

            if (value.getType().equals(ArgumentType.ReferenceAnnotation) || value.getType().equals(ArgumentType.ReferenceGenome) || value.getType().equals(ArgumentType.ReferenceSequence)) {
                con.remove(defaultValue);
                defaultValue = null;
            }

            // DISABLE CONTROLS
            if (!isDiskResourceArgumentType) {
                // This is only visible to Disk Resource types
                fileInfoTypeLabel.setVisible(false);
            }
            if (!isDiskResourceOutputType) {
                // This is only visible to Disk Resource output types
                isImplicit.setVisible(false);
                dataSourceLabel.setVisible(false);
            }
            if (!AppTemplateUtils.typeSupportsValidators(value.getType())) {
                validatorsEditor.setVisible(false);
                validatorsEditor.disable();
                con.remove(validatorsEditor);
            }
            switch (value.getType()) {
                case TextSelection:
                case IntegerSelection:
                case DoubleSelection:
                case Selection:
                case ValueSelection:
                case TreeSelection:
                    doNotDisplay.setVisible(false);
                    nameLabel.setVisible(false);
                    break;

                case Info:
                    requiredEditor.setVisible(false);
                    omitIfBlank.setVisible(false);
                    doNotDisplay.setVisible(false);
                    descriptionLabel.setVisible(false);
                    nameLabel.setVisible(false);
                    break;

                case Flag:
                    requiredEditor.setVisible(false);
                    omitIfBlank.setVisible(false);
                    break;

                case Input:
                case FileInput:
                case FolderInput:
                case MultiFileSelector:
                    doNotDisplay.setVisible(false);
                    break;

                case FolderOutput:
                case MultiFileOutput:
                    dataSourceLabel.setVisible(false);
                    break;

                case EnvironmentVariable:
                    omitIfBlank.setVisible(false);
                    break;

                case Output:
                case FileOutput:
                case Double:
                case Group:
                case Integer:
                case MultiLineText:
                case Number:
                case Text:
                default:
                    break;
            }

            if (presenter.isOnlyLabelEditMode()) {
                // Disable all controls, then re-enable valid controls
                for (int i = 0; i < con.getWidgetCount(); i++) {
                    Widget w = con.getWidget(i);
                    if (w instanceof HasEnabled) {
                        ((HasEnabled)w).setEnabled(false);
                    }
                }

                argLabelLabel.enable();
                descriptionLabel.enable();
            }
            con.forceLayout();
        }

        this.model = value;
        if (!isInfoType) {
            updateDisplayInGuiVisibilities(!model.isVisible());
        }
        cp.setHeadingHtml(presenter.getAppearance().getPropertyPanelLabels().detailsPanelHeader(model.getLabel()));
        // JDS Manually forward the value to the non-bound controls
        if (AppTemplateUtils.isSimpleSelectionArgumentType(value.getType())) {
            selectionItemDefaultValue.setValue(model);
        } else if (isMultiSelectorType || isTreeSelectionType || isInfoType || isDiskResourceArgumentType) {

            // JDS Manually set the DataObject items
            updateFileInfoTypeSelection(model);
            updateIsImplicit(model);
            updateDataSourceSelection(model);
            if (isDiskResourceOutputType) {
                defaultValue.setValue(model);
            }
        } else if (!model.getType().equals(ArgumentType.ReferenceAnnotation) && !model.getType().equals(ArgumentType.ReferenceGenome) && !model.getType().equals(ArgumentType.ReferenceSequence)) {
            // It is not a selection type, nor a MultiFileSelector
            defaultValue.setValue(model);

        }
    }

    private void updateFieldLabels(ArgumentType type) {
        AppsWidgetsPropertyPanelLabels labels = presenter.getAppearance().getPropertyPanelLabels();
        AppsWidgetsContextualHelpMessages help = presenter.getAppearance().getContextHelpMessages();

        
        descriptionLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.toolTipText(), help.toolTip()));
        nameLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.argumentOption(), help.argumentOption()));
        doNotDisplay.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(labels.doNotDisplay()).toSafeHtml());

        requiredEditor.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(labels.isRequired()).toSafeHtml());

        QuickTip descQt = new QuickTip(descriptionLabel);
        QuickTip nameQt = new QuickTip(nameLabel);
        QuickTip argLabelQt = new QuickTip(argLabelLabel);
//        QuickTip listSelectionQt = new QuickTip(listSelectionLabel);
        QuickTip selectionItemDefQt = new QuickTip(selectionItemDefaultValueLabel);
        QuickTip dataSourceQt = new QuickTip(dataSourceLabel);
        QuickTip omitQT = new QuickTip(omitIfBlank);
        QuickTip isImplicitQt = new QuickTip(isImplicit);
        descQt.getToolTipConfig().setDismissDelay(0);
        nameQt.getToolTipConfig().setDismissDelay(0);
        argLabelQt.getToolTipConfig().setDismissDelay(0);
//        listSelectionQt.getToolTipConfig().setDismissDelay(0);
        selectionItemDefQt.getToolTipConfig().setDismissDelay(0);
        dataSourceQt.getToolTipConfig().setDismissDelay(0);
        omitQT.getToolTipConfig().setDismissDelay(0);
        isImplicitQt.getToolTipConfig().setDismissDelay(0);

        // JDS Change field labels based on Type
        switch (type) {
            case FileInput:
                argLabelLabel.setText(labels.fileInputLabel());
                fileInfoTypeLabel.setHTML(labels.fileInputFileInfoType());
                label.setEmptyText(labels.fileInputEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.fileInputExcludeArgument())).toSafeHtml());
                break;
            case FolderInput:
                argLabelLabel.setText(labels.folderInputLabel());
                fileInfoTypeLabel.setHTML(labels.folderInputFileInfoType());
                label.setEmptyText(labels.folderInputEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.folderInputExcludeArgument())).toSafeHtml());
                break;
            case MultiFileSelector:
                argLabelLabel.setText(labels.multiFileInputLabel());
                fileInfoTypeLabel.setHTML(labels.multiFileInputFileInfoType());
                label.setEmptyText(labels.multiFileInputEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.multiFileInputExcludeArgument())).toSafeHtml());
                break;

            case TextSelection:
                argLabelLabel.setText(labels.textSelectionLabel());
//                listSelectionLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.singleSelectionCreateLabel(), help.singleSelectionCreateList()));
                selectionItemDefaultValueLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.singleSelectionDefaultValue(), help.singleSelectDefaultItem()));
                label.setEmptyText(labels.textSelectionEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.singleSelectExcludeArgument())).toSafeHtml());
                break;
            case IntegerSelection:
                argLabelLabel.setText(labels.integerSelectionLabel());
//                listSelectionLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.singleSelectionCreateLabel(), help.singleSelectionCreateList()));
                selectionItemDefaultValueLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.singleSelectionDefaultValue(), help.singleSelectDefaultItem()));
                label.setEmptyText(labels.textSelectionEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.singleSelectExcludeArgument())).toSafeHtml());
                break;
            case DoubleSelection:
                argLabelLabel.setText(labels.doubleSelectionLabel());
//                listSelectionLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.singleSelectionCreateLabel(), help.singleSelectionCreateList()));
                selectionItemDefaultValueLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.singleSelectionDefaultValue(), help.singleSelectDefaultItem()));
                label.setEmptyText(labels.textSelectionEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.singleSelectExcludeArgument())).toSafeHtml());
                break;
            case TreeSelection:
                argLabelLabel.setText(labels.treeSelectionLabel());
                label.setEmptyText(labels.textSelectionEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.singleSelectExcludeArgument())).toSafeHtml());
                break;

            case Info:
                argLabelLabel.setText(labels.infoLabel());
                label.setEmptyText(labels.infoEmptyText());
                break;
            case EnvironmentVariable:
                argLabelLabel.setText(labels.envVarLabel());
                label.setEmptyText(labels.envVarEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.envVarExcludeArgument())).toSafeHtml());
                break;
            case Text:
                argLabelLabel.setText(labels.textInputLabel());
                label.setEmptyText(labels.textInputEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.textInputExcludeArgument())).toSafeHtml());
                break;
            case MultiLineText:
                argLabelLabel.setText(labels.multiLineTextLabel());
                label.setEmptyText(labels.textInputEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.textInputExcludeArgument())).toSafeHtml());
                break;
            case Flag:
                argLabelLabel.setText(labels.checkboxLabel());
                label.setEmptyText(labels.checkboxEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.checkboxInputExcludeArgument())).toSafeHtml());
                break;
            case Integer:
                argLabelLabel.setText(labels.integerInputLabel());
                label.setEmptyText(labels.envVarEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.integerInputExcludeArgument())).toSafeHtml());
                break;
            case Double:
                argLabelLabel.setText(labels.doubleInputLabel());
                label.setEmptyText(labels.envVarEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.integerInputExcludeArgument())).toSafeHtml());
                break;

            case FileOutput:
                argLabelLabel.setText(labels.fileOutputLabel());
                dataSourceLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.fileOutputSourceLabel(), help.fileOutputOutputSource()));
                fileInfoTypeLabel.setHTML(labels.folderInputFileInfoType());
                label.setEmptyText(labels.fileOutputEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.fileOutputExcludeArgument())).toSafeHtml());
                isImplicit.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.doNotPass(), help.doNotPass()))
                        .toSafeHtml());
                break;
            case FolderOutput:
                argLabelLabel.setText(labels.folderOutputLabel());
                dataSourceLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.folderOutputSourceLabel(), help.fileOutputOutputSource()));
                fileInfoTypeLabel.setHTML(labels.folderInputFileInfoType());
                label.setEmptyText(labels.folderOutputEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.fileOutputExcludeArgument())).toSafeHtml());
                isImplicit.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.doNotPass(), help.doNotPass()))
                        .toSafeHtml());
                break;
            case MultiFileOutput:
                argLabelLabel.setText(labels.multiFileOutputLabel());
                dataSourceLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.multiFileOutputSourceLabel(), help.fileOutputOutputSource()));
                fileInfoTypeLabel.setHTML(labels.multiFileInputFileInfoType());
                label.setEmptyText(labels.multiFileOutputEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.fileOutputExcludeArgument())).toSafeHtml());
                isImplicit.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.doNotPass(), help.doNotPass()))
                        .toSafeHtml());
                break;

            case ReferenceGenome:
                argLabelLabel.setText(labels.referenceGenomeLabel());
                label.setEmptyText(labels.referenceGenomeEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.excludeReference())).toSafeHtml());
                break;
            case ReferenceSequence:
                argLabelLabel.setText(labels.referenceSequenceLabel());
                label.setEmptyText(labels.referenceSequenceEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.excludeReference())).toSafeHtml());
                break;
            case ReferenceAnnotation:
                argLabelLabel.setText(labels.referenceAnnotationLabel());
                label.setEmptyText(labels.referenceAnnotationEmptyText());
                omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                        .append(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.excludeWhenEmpty(), help.excludeReference())).toSafeHtml());
                break;

            default:
                break;
        }
    }

    private void updateIsImplicit(Argument model) {
        if ((model != null) && (model.getDataObject() != null)) {
            isImplicit.setValue(model.getDataObject().isImplicit());
        }
    }

    private void updateFileInfoTypeSelection(Argument model) {
        if ((model != null) && (model.getDataObject() != null) && (model.getDataObject().getFileInfoType() != null)) {

            List<FileInfoType> fileInfoTypeList = fileInfoTypeComboBox.getStore().getAll();
            if ((fileInfoTypeStoreAddHandlerReg != null) && !fileInfoTypeList.isEmpty()) {
                fileInfoTypeStoreAddHandlerReg.removeHandler();
                fileInfoTypeStoreAddHandlerReg = null;
            }
            for (final FileInfoType fit : fileInfoTypeList) {
                if (fit.getType().equals(model.getDataObject().getFileInfoType())) {
                    fileInfoTypeComboBox.setValue(fit);
                    break;
                }
            }
        }

    }

    private void updateDataSourceSelection(Argument model) {
        if ((model != null) && (model.getDataObject() != null) && (model.getDataObject().getDataSource() != null)) {
            List<DataSource> dataSourceList = dataSourceComboBox.getStore().getAll();
            if ((dataSourceStoreAddHandlerReg != null) && !dataSourceList.isEmpty()) {
                dataSourceStoreAddHandlerReg.removeHandler();
                dataSourceStoreAddHandlerReg = null;
            }
            for (DataSource ds : dataSourceList) {
                if (ds.getType().equals(model.getDataObject().getDataSource())) {
                    dataSourceComboBox.setValue(ds);
                    break;
                }
            }
        }

    }

    public boolean hasErrors() {
        return !FormPanelHelper.isValid(con) || ((defaultValue != null) && (defaultValue.getErrors() != null) && !defaultValue.getErrors().isEmpty());
    }

}
