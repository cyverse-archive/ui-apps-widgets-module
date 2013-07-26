package org.iplantc.core.uiapps.widgets.client.view.editors.properties;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentValidator;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSource;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSourceProperties;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoType;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoTypeProperties;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
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
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
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
    VerticalLayoutContainer con;

    @UiField
    CheckBoxAdapter requiredEditor, omitIfBlank, visible;

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
    FieldLabel selectionItemDefaultValueLabel, nameLabel, argLabelLabel, descriptionLabel, fileInfoTypeLabel, dataSourceLabel, listSelectionLabel;

    @Path("")
    @UiField(provided = true)
    SelectionItemPropertyEditor selectionItemListEditor;

    @Path("")
    @UiField(provided = true)
    SelectionItemTreePropertyEditor selectionItemTreeEditor;

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

    public ArgumentPropertyEditor(final AppTemplateWizardPresenter presenter, final UUIDServiceAsync uuidService, final AppMetadataServiceFacade appMetadataService) {
        this.presenter = presenter;
        defaultValue = new DefaultArgumentValueEditor(presenter.getAppearance(), appMetadataService);
        validatorsEditor = new ArgumentValidatorEditor(presenter.getAppearance(), I18N.DISPLAY);
        selectionItemDefaultValue = new AppWizardComboBox(presenter);
        selectionItemListEditor = new SelectionItemPropertyEditor(presenter, uuidService);
        selectionItemTreeEditor = new SelectionItemTreePropertyEditor(presenter);

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
        new QuickTip(this);
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
                    store.addSortInfo(new StoreSortInfo<DataSource>(props.labelValue(), SortDir.ASC));
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
        ComboBox<DataSource> comboBox = new ComboBox<DataSource>(store, props.label());
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
    @UiHandler({"requiredEditor", "omitIfBlank", "visible", "isImplicit"})
    void onBooleanValueChanged(ValueChangeEvent<Boolean> event) {
        if (event.getSource() == requiredEditor) {
            if (event.getValue()) {
                omitIfBlank.setValue(false);
                // omitIfBlank.disable();
                omitIfBlank.setEnabled(false);
            } else {
                omitIfBlank.setEnabled(true);
                // omitIfBlank.enable();
            }
        }
        if (event.getSource() == visible) {
            updateDisplayInGuiVisibilities(event.getValue());
        }
        presenter.onArgumentPropertyValueChange(event.getSource());
        
    }

    private void updateDisplayInGuiVisibilities(boolean displayInGui) {
        // If the "Display in GUI" checkbox is not selected
        if (!displayInGui) {
            // Clear the "require user input" checkbox
            requiredEditor.setValue(false);

            omitIfBlank.setVisible(false);
            requiredEditor.setVisible(false);
            descriptionLabel.setVisible(false);
            if (validatorsEditor != null) {
                validatorsEditor.setVisible(false);
            }
            if (selectionItemListEditor != null) {
                selectionItemListEditor.setVisible(false);
            }
            if (selectionItemTreeEditor != null) {
                selectionItemTreeEditor.setVisible(false);
            }
        } else {
            omitIfBlank.setVisible(true);
            // omitIfBlank.enable();
            omitIfBlank.setEnabled(true);

            requiredEditor.setVisible(true);
            descriptionLabel.setVisible(true);
            if (validatorsEditor != null) {
                validatorsEditor.setVisible(true);
            }
            if (selectionItemListEditor != null) {
                selectionItemListEditor.setVisible(true);
            }
            if (selectionItemTreeEditor != null) {
                selectionItemTreeEditor.setVisible(true);
            }
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

    @UiHandler({"selectionItemTreeEditor", "selectionItemListEditor"})
    void onSelectionItemListChanged(ValueChangeEvent<List<SelectionItem>> event) {
        presenter.onArgumentPropertyValueChange(event.getSource());
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
                listSelectionLabel.setVisible(false);
                con.remove(selectionItemListEditor);
                con.remove(selectionItemTreeEditor);
                con.remove(selectionItemDefaultValueLabel);
                selectionItemDefaultValueLabel = null;
                selectionItemDefaultValue = null;
                selectionItemListEditor = null;
                selectionItemTreeEditor = null;
                if ((isDiskResourceArgumentType && !isDiskResourceOutputType) || isInfoType) {
                    con.remove(defaultValue);
                    defaultValue = null;
                }
            } else if (isTreeSelectionType) {
                // JDS Remove all controls except for those related to TreeSelection
                listSelectionLabel.setVisible(false);
                con.remove(selectionItemListEditor);
                con.remove(selectionItemDefaultValueLabel);
                con.remove(defaultValue);
                selectionItemDefaultValueLabel = null;
                selectionItemDefaultValue = null;
                selectionItemListEditor = null;
                defaultValue = null;
            } else if (!isTreeSelectionType) {
                // JDS Remove all controls except for those related to Simple selections (i.e. non-tree)
                con.remove(selectionItemTreeEditor);
                con.remove(defaultValue);
                selectionItemTreeEditor = null;
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
                    visible.setVisible(false);
                    nameLabel.disable();
                    break;

                case Info:
                    requiredEditor.setVisible(false);
                    omitIfBlank.setVisible(false);
                    visible.setVisible(false);
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
                    visible.setVisible(false);
                    break;

                case Output:
                case FileOutput:
                case FolderOutput:
                case MultiFileOutput:
                case Double:
                case EnvironmentVariable:
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
            updateDisplayInGuiVisibilities(model.isVisible());
        }

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
            if (defaultValue == null) {
                GWT.log("This is a problem!");
            }
            defaultValue.setValue(model);

        }
    }

    private void updateFieldLabels(ArgumentType type) {
        AppsWidgetsPropertyPanelLabels labels = I18N.DISPLAY;
        AppsWidgetsContextualHelpMessages help = I18N.DISPLAY;

        descriptionLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelToolTipText(), help.propertyToolTip()));
        nameLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelArgumentOption(), help.propertyArgumentOption()));

        new QuickTip(descriptionLabel);
        new QuickTip(nameLabel);
        new QuickTip(fileInfoTypeLabel);
        new QuickTip(argLabelLabel);
        new QuickTip(listSelectionLabel);
        new QuickTip(selectionItemDefaultValueLabel);
        new QuickTip(dataSourceLabel);
        new QuickTip(omitIfBlank);
        new QuickTip(isImplicit);

        // JDS Change field labels based on Type
        switch (type) {
            case FileInput:
                argLabelLabel.setText(labels.labelFileInputLabel());
                fileInfoTypeLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelFileInfoTypeFileLabel(), help.propertyFileInfoType()));
                label.setEmptyText(labels.labelFileInputEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentFile()));
                break;
            case FolderInput:
                argLabelLabel.setText(labels.labelFolderInputLabel());
                fileInfoTypeLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelFileInfoTypeFolderLabel(), help.propertyFileInfoType()));
                label.setEmptyText(labels.labelFolderInputEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentFolder()));
                break;
            case MultiFileSelector:
                argLabelLabel.setText(labels.labelMultiFileInputLabel());
                fileInfoTypeLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelFileInfoTypeMultiFileLabel(), help.propertyFileInfoType()));
                label.setEmptyText(labels.labelMultiFileInputEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentMulti()));
                break;

            case TextSelection:
                argLabelLabel.setText(labels.labelTextSelectionLabel());
                listSelectionLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelSingleSelectionCreateLabel(), help.propertyCreateList()));
                selectionItemDefaultValueLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelSingleSelectionDefaultValue(), help.propertyDefaultItem()));
                label.setEmptyText(labels.labelTextSelectionEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentList()));
                break;
            case IntegerSelection:
                argLabelLabel.setText(labels.labelIntegerSelectionLabel());
                listSelectionLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelSingleSelectionCreateLabel(), help.propertyCreateList()));
                selectionItemDefaultValueLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelSingleSelectionDefaultValue(), help.propertyDefaultItem()));
                label.setEmptyText(labels.labelTextSelectionEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentList()));
                break;
            case DoubleSelection:
                argLabelLabel.setText(labels.labelDoubleSelectionLabel());
                listSelectionLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelSingleSelectionCreateLabel(), help.propertyCreateList()));
                selectionItemDefaultValueLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelSingleSelectionDefaultValue(), help.propertyDefaultItem()));
                label.setEmptyText(labels.labelTextSelectionEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentList()));
                break;
            case TreeSelection:
                argLabelLabel.setText(labels.labelTreeSelectionLabel());
                label.setEmptyText(labels.labelTextSelectionEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentList()));
                break;

            case Info:
                argLabelLabel.setText(labels.labelInfoLabel());
                label.setEmptyText(labels.labelInfoEmptyText());
                break;
            case EnvironmentVariable:
                argLabelLabel.setText(labels.labelEnvVarLabel());
                label.setEmptyText(labels.labelEnvVarEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentEnvVar()));
                break;
            case Text:
                argLabelLabel.setText(labels.labelTextInputLabel());
                label.setEmptyText(labels.labelTextInputEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentText()));
                break;
            case MultiLineText:
                argLabelLabel.setText(labels.labelMultiLineTextLabel());
                label.setEmptyText(labels.labelTextInputEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentText()));
                break;
            case Flag:
                argLabelLabel.setText(labels.labelCheckboxLabel());
                label.setEmptyText(labels.labelCheckboxEmptyText());
                break;
            case Integer:
                argLabelLabel.setText(labels.labelIntegerInputLabel());
                label.setEmptyText(labels.labelEnvVarEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentInteger()));
                break;
            case Double:
                argLabelLabel.setText(labels.labelDoubleInputLabel());
                label.setEmptyText(labels.labelEnvVarEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentInteger()));
                break;

            case FileOutput:
                argLabelLabel.setText(labels.labelFileOutputLabel());
                dataSourceLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelFileOutputSourceLabel(), help.propertyOutputSource()));
                fileInfoTypeLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelFileInfoTypeFileLabel(), help.propertyFileInfoType()));
                label.setEmptyText(labels.labelFileOutputEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentOutput()));
                isImplicit.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelDoNotPass(), help.propertyDoNotPass()));
                break;
            case FolderOutput:
                argLabelLabel.setText(labels.labelFolderOutputLabel());
                dataSourceLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelFolderOutputSourceLabel(), help.propertyOutputSource()));
                fileInfoTypeLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelFileInfoTypeFolderLabel(), help.propertyFileInfoType()));
                label.setEmptyText(labels.labelFolderOutputEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentOutput()));
                isImplicit.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelDoNotPass(), help.propertyDoNotPass()));
                break;
            case MultiFileOutput:
                argLabelLabel.setText(labels.labelMultiFileOutputLabel());
                dataSourceLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelMultiFileOutputSourceLabel(), help.propertyOutputSource()));
                fileInfoTypeLabel.setHTML(presenter.getAppearance().createContextualHelpLabel(labels.labelFileInfoTypeMultiFileLabel(), help.propertyFileInfoType()));
                label.setEmptyText(labels.labelMultiFileOutputEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeArgumentOutput()));
                isImplicit.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelDoNotPass(), help.propertyDoNotPass()));
                break;

            case ReferenceGenome:
                argLabelLabel.setText(labels.labelReferenceGenomeLabel());
                label.setEmptyText(labels.labelReferenceGenomeEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeReference()));
                break;
            case ReferenceSequence:
                argLabelLabel.setText(labels.labelReferenceSequenceLabel());
                label.setEmptyText(labels.labelReferenceSequenceEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeReference()));
                break;
            case ReferenceAnnotation:
                argLabelLabel.setText(labels.labelReferenceAnnotationLabel());
                label.setEmptyText(labels.labelReferenceAnnotationEmptyText());
                omitIfBlank.setHTML(presenter.getAppearance().createContextualHelpLabelNoFloat(labels.labelExcludeWhenEmpty(), help.propertyExcludeReference()));
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
