package org.iplantc.core.uiapps.widgets.client.view.editors.properties.trees;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemProperties;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizardPresenter;
import org.iplantc.core.uiapps.widgets.client.view.util.SelectionItemTreeStoreEditor;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.validators.CmdLineArgCharacterValidator;
import org.iplantc.de.client.UUIDService;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.TreeGridDragSource;
import com.sencha.gxt.dnd.core.client.TreeGridDropTarget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

/**
 * A container with a TreeGrid editor and button toolbar for editing hierarchical list selectors.
 * 
 * @author psarando, jstroot
 * 
 */
public class SelectionItemTreePropertyEditor extends Composite implements HasValueChangeHandlers<List<SelectionItem>> {

    private static final String LIST_RULE_ARG_IS_DEFAULT = "isDefault"; //$NON-NLS-1$

    private final SelectionItemProperties siProps = GWT.create(SelectionItemProperties.class);
    private static SelectionItemTreePropertyEditorUiBinder BINDER = GWT.create(SelectionItemTreePropertyEditorUiBinder.class);

    interface SelectionItemTreePropertyEditorUiBinder extends UiBinder<Widget, SelectionItemTreePropertyEditor> {
    }

    TreeStore<SelectionItem> store;

    @UiField(provided = true)
    TreeGrid<SelectionItem> treeGrid;

    @UiField
    CheckBox forceSingleSelectCheckBox;

    @UiField
    SimpleComboBox<CheckCascade> cascadeOptionsCombo;

    SelectionItemTreeStoreEditor selectionItemsEditor;

    private int countGroupLabel = 1;
    private int countArgLabel = 1;
    private final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
    private final UUIDServiceAsync uuidService = GWT.create(UUIDService.class);
    private final AppsWidgetsPropertyPanelLabels labels;

    private final List<SelectionItem> toBeRemoved = Lists.newArrayList();

    public SelectionItemTreePropertyEditor(final AppTemplateWizardPresenter presenter, List<SelectionItem> selectionItems) {
        this.labels = presenter.getAppearance().getPropertyPanelLabels();
        buildTreeGrid();
        initWidget(BINDER.createAndBindUi(this));
        treeGrid.getView().setEmptyText(labels.selectionCreateWidgetEmptyText());

        selectionItemsEditor = new MyTreeStoreEditor(store, this, presenter);
        cascadeOptionsCombo.add(CheckCascade.TRI);
        cascadeOptionsCombo.add(CheckCascade.PARENTS);
        cascadeOptionsCombo.add(CheckCascade.CHILDREN);
        cascadeOptionsCombo.add(CheckCascade.NONE);
        cascadeOptionsCombo.setValue(CheckCascade.TRI);
        cascadeOptionsCombo.addSelectionHandler(new CascadeOptionsComboSelectionHandler());

        initDragNDrop();

        selectionItemsEditor.setValue(selectionItems);
    }

    private void buildTreeGrid() {
        // Build treeStore
        store = new TreeStore<SelectionItem>(siProps.id());
        store.setAutoCommit(true);

        // Build ColumnModel
      //  RowNumberer<SelectionItem> numberer = new RowNumberer<SelectionItem>(new IdentityValueProvider<SelectionItem>());
        ColumnConfig<SelectionItem, String> displayConfig = new ColumnConfig<SelectionItem, String>(siProps.display(), 90, labels.singleSelectDisplayColumnHeader());
        ColumnConfig<SelectionItem, String> nameConfig = new ColumnConfig<SelectionItem, String>(siProps.name(), 60, labels.singleSelectNameColumnHeader());
        ColumnConfig<SelectionItem, String> valueConfig = new ColumnConfig<SelectionItem, String>(siProps.value(), 40, labels.singleSelectValueColumnHeader());
        ColumnConfig<SelectionItem, String> descriptionConfig = new ColumnConfig<SelectionItem, String>(siProps.description(), 90, labels.singleSelectToolTipColumnHeader());
        ColumnConfig<SelectionItem, Boolean> defaultColumn = buildIsDefaultConfig();

        defaultColumn.setSortable(false);
        displayConfig.setSortable(false);
        nameConfig.setSortable(false);
        valueConfig.setSortable(false);
        descriptionConfig.setSortable(false);

        ArrayList<ColumnConfig<SelectionItem, ?>> newArrayList = Lists.<ColumnConfig<SelectionItem, ?>> newArrayList();
     //   newArrayList.add(numberer);
        newArrayList.add(defaultColumn);
        newArrayList.add(displayConfig);
        newArrayList.add(nameConfig);
        newArrayList.add(valueConfig);
        newArrayList.add(descriptionConfig);

        ColumnModel<SelectionItem> cm = new ColumnModel<SelectionItem>(newArrayList);

        // Create and configure TreeGrid
        treeGrid = new TreeGrid<SelectionItem>(store, cm, displayConfig) {
            @Override
            protected void onDoubleClick(Event e) {
                // KLUDGE CORE-4754 intentionally do nothing to prevent groups from expanding instead of
                // editing, since the GridInlineEditing below is configured for double-click editing.
                // According to Sven in the support forums, this is currently the only way to disable
                // expand/collapse on double-click.
            }
        };

        GridRowEditing<SelectionItem> editing = new GridRowEditing<SelectionItem>(treeGrid){
            
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
        editing.addEditor(defaultColumn, new CheckBox());
        editing.addEditor(displayConfig, buildEditorField(null,false));
        editing.addEditor(nameConfig, buildEditorField(new CmdLineArgCharacterValidator(I18N.V_CONSTANTS.restrictedCmdLineChars()),false));
        editing.addEditor(valueConfig, buildEditorField(new CmdLineArgCharacterValidator(I18N.V_CONSTANTS.restrictedCmdLineArgCharsExclNewline()),false));
        editing.addEditor(descriptionConfig, buildEditorField(null, false));
        editing.setClicksToEdit(ClicksToEdit.TWO);

        treeGrid.getView().setAutoExpandColumn(descriptionConfig);
        treeGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    private TextField buildEditorField(Validator<String> validator, boolean readonly) {
        final TextField field1 = new TextField();
        field1.setSelectOnFocus(true);
        if(validator != null) {
            field1.addValidator(validator);
            field1.setAutoValidate(true);
        }
        field1.addInvalidHandler(new InvalidHandler() {
            
            @Override
            public void onInvalid(InvalidEvent event) {
               field1.clear();
            }
        });
        return field1;
    }

    @UiFactory
    SimpleComboBox<CheckCascade> buildCascadeComboBoxLabelProvider() {
        return new SimpleComboBox<CheckCascade>(new LabelProvider<CheckCascade>() {
            @Override
            public String getLabel(CheckCascade item) {
                String ret = "";
                switch (item) {
                    case CHILDREN:
                        ret = I18N.DISPLAY.treeSelectorCascadeChildren();
                        break;
                    case NONE:
                        ret = I18N.DISPLAY.treeSelectorCascadeNone();
                        break;
                    case PARENTS:
                        ret = I18N.DISPLAY.treeSelectorCascadeParent();
                        break;
                    case TRI:
                        ret = I18N.DISPLAY.treeSelectorCascadeTri();
                        break;

                    default:
                        break;
                }
                return ret;
            }
        });
    }

    @UiHandler("treeGrid")
    void onTreeGridViewReady(ViewReadyEvent event) {
        List<SelectionItem> rootItems = store.getRootItems();
        if (rootItems != null) {
            for (SelectionItem root : rootItems) {
                if (root instanceof SelectionItemGroup) {
                    SelectionItemGroup group = (SelectionItemGroup)root;

                    if (treeGrid.isLeaf(group)) {
                        treeGrid.setLeaf(group, false);
                        treeGrid.refresh(group);
                    }
                }
            }
        }
    }

    @UiHandler("addGrpBtn")
    void onAddGroupClicked(SelectEvent event) {
        uuidService.getUUIDs(1, new AsyncCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> uuids) {
                addGroup(uuids.get(0));
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post("Unable to generate a UUID", caught);
            }
        });
    }

    @UiHandler("addArgBtn")
    void onAddArgumentClicked(SelectEvent event) {
        uuidService.getUUIDs(1, new AsyncCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> uuids) {
                addArgument(uuids.get(0));
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post("Unable to generate a UUID", caught);
            }
        });
    }

    @UiHandler("forceSingleSelectCheckBox")
    void onSingleSelectChanged(ChangeEvent event) {
        if (isSingleSelect()) {
            List<SelectionItem> checked = new ArrayList<SelectionItem>();

            for (SelectionItem ruleArg : store.getAll()) {
                if (ruleArg.isDefault() && !(ruleArg instanceof SelectionItemGroup)) {
                    checked.add(ruleArg);
                }
            }

            if (checked.size() > 1) {
                for (SelectionItem ruleArg : checked) {
                    setDefaultValue(ruleArg, false);
                }
            }
        }
        ValueChangeEvent.fire(this, Lists.<SelectionItem> newArrayList(selectionItemsEditor.getCurrentTree()));
    }

    @UiHandler("treeGrid")
    void onExpand(ExpandItemEvent<SelectionItem> event) {
        SelectionItemGroup parent = (SelectionItemGroup)event.getItem();

        if (parent.getGroups() != null) {
            for (SelectionItemGroup group : parent.getGroups()) {
                treeGrid.setLeaf(group, false);
                treeGrid.refresh(group);
            }
        }
    }

    @UiHandler("deleteBtn")
    void onDeleteClicked(SelectEvent event) {
        SelectionItem selected = treeGrid.getSelectionModel().getSelectedItem();
        if (selected != null) {
            SelectionItem parent = store.getParent(selected);
            if (parent != null) {
                SelectionItemGroup group = (SelectionItemGroup)parent;

                if (selected instanceof SelectionItemGroup) {
                    group.getGroups().remove(selected);
                } else {
                    group.getArguments().remove(selected);
                }
            }

            toBeRemoved.add(selected);
            store.remove(selected);
        }
    }

    private ColumnConfig<SelectionItem, Boolean> buildIsDefaultConfig() {
        ColumnConfig<SelectionItem, Boolean> defaultConfig = new ColumnConfig<SelectionItem, Boolean>(new IsDefaultColumnValueProvider(), 45, labels.singleSelectIsDefaultColumnHeader());

        CheckBoxCell cell = new CheckBoxCell();
        defaultConfig.setCell(cell);
        defaultConfig.setSortable(false);
        defaultConfig.setMenuDisabled(true);

        return defaultConfig;
    }

    private void setDefaultValue(SelectionItem object, Boolean value) {
        if (object == null) {
            return;
        }

        object.setDefault(value);
        store.update(object);

        if ((object instanceof SelectionItemGroup) && isCascadeToChildren()) {
            SelectionItemGroup group = (SelectionItemGroup)object;

            List<SelectionItemGroup> groups = group.getGroups();
            if (groups != null) {
                for (SelectionItem child : groups) {
                    setDefaultValue(child, value);
                }
            }

            List<SelectionItem> children = group.getArguments();
            if (children != null) {
                for (SelectionItem child : children) {
                    setDefaultValue(child, value);
                }
            }
        }
    }

    private void initDragNDrop() {
        TreeGridDragSource<SelectionItem> dragSource = new TreeGridDragSource<SelectionItem>(treeGrid);
        SelectionItemTreePropertyEditorDnDHandler dndHandler = new SelectionItemTreePropertyEditorDnDHandler(this);
        dragSource.addDragStartHandler(dndHandler);

        TreeGridDropTarget<SelectionItem> target = new TreeGridDropTarget<SelectionItem>(treeGrid);
        target.setAllowSelfAsSource(true);
        target.setFeedback(Feedback.BOTH);
        target.addDropHandler(dndHandler);
    }

    private boolean isSingleSelect() {
        return forceSingleSelectCheckBox.getValue();
    }

    private boolean isCascadeToChildren() {
        return cascadeOptionsCombo.getValue() == CheckCascade.TRI || cascadeOptionsCombo.getValue() == CheckCascade.CHILDREN;
    }

    private void addGroup(String uuid) {
        SelectionItemGroup group = createGroup(uuid);

        SelectionItemGroup selectedGroup = getSelectedGroup();
        if (selectedGroup != null) {
            List<SelectionItemGroup> groups = selectedGroup.getGroups();

            if (groups == null) {
                groups = new ArrayList<SelectionItemGroup>();
                selectedGroup.setGroups(groups);
            }

            groups.add(group);
        }

        addRuleArgument(selectedGroup, group);

        treeGrid.setLeaf(group, false);
        treeGrid.refresh(group);
    }

    private void addArgument(String uuid) {
        SelectionItem ruleArg = createArgument(uuid);

        SelectionItemGroup selectedGroup = getSelectedGroup();
        if (selectedGroup != null) {
            List<SelectionItem> arguments = selectedGroup.getArguments();

            if (arguments == null) {
                arguments = new ArrayList<SelectionItem>();
                selectedGroup.setArguments(arguments);
            }

            arguments.add(ruleArg);
        }

        addRuleArgument(selectedGroup, ruleArg);
    }

    private SelectionItemGroup getSelectedGroup() {
        SelectionItem selected = treeGrid.getSelectionModel().getSelectedItem();

        if (selected != null) {
            if (selected instanceof SelectionItemGroup) {
                return (SelectionItemGroup)selected;
            } else {
                selected = store.getParent(selected);
                if (selected != null) {
                    return (SelectionItemGroup)selected;
                }
            }
        }

        return null;
    }

    private void addRuleArgument(SelectionItemGroup selectedGroup, SelectionItem ruleArg) {
        if (selectedGroup != null) {
            ruleArg.setDefault(selectedGroup.isDefault());

            store.add(selectedGroup, ruleArg);
            treeGrid.setExpanded(selectedGroup, true);
        } else {
            store.add(ruleArg);
        }
    }

    private SelectionItemGroup createGroup(String uuid) {
        SelectionItemGroup group = factory.selectionItemGroup().as();
        group.setId(uuid);

        group.setDisplay("Group " + countGroupLabel++);

        return group;
    }

    private SelectionItem createArgument(String uuid) {
        SelectionItem argString = factory.selectionItem().as();
        argString.setId(uuid);

        argString.setDisplay("Argument" + countArgLabel++);

        return argString;
    }

    private void addGroupToStore(SelectionItemGroup parent, SelectionItemGroup group) {
        if (group != null) {
            if (parent != null) {
                store.add(parent, group);
            } else {
                store.add(group);
            }

            if (group.getGroups() != null) {
                for (SelectionItemGroup child : group.getGroups()) {
                    addGroupToStore(group, child);
                }
            }

            if (group.getArguments() != null) {
                for (SelectionItem child : group.getArguments()) {
                    store.add(group, child);
                }
            }
        }
    }

    /**
     * Resets the editor with the values in the given root group.
     * 
     * @param root
     */
    public void setValues(SelectionItemGroup root) {
        store.clear();

        if (root != null) {
            forceSingleSelectCheckBox.setValue(root.isSingleSelect());

            if (root.getSelectionCascade() != null) {
                cascadeOptionsCombo.setValue(root.getSelectionCascade());

            }

            if (root.getGroups() != null) {
                for (SelectionItemGroup group : root.getGroups()) {
                    addGroupToStore(null, group);

                    if (treeGrid.isViewReady()) {
                        treeGrid.setLeaf(group, false);
                        treeGrid.refresh(group);
                    }
                }
            }

            if (root.getArguments() != null) {
                for (SelectionItem ruleArg : root.getArguments()) {
                    store.add(ruleArg);
                }
            }
        }
    }

    /**
     * Returns a group of values added by the editor.
     * 
     * @return A root group containing the groups and args added by the editor.
     */
    public AutoBean<SelectionItemGroup> getValues() {
        List<SelectionItemGroup> groups = new ArrayList<SelectionItemGroup>();
        List<SelectionItem> arguments = new ArrayList<SelectionItem>();

        for (SelectionItem ruleArg : store.getRootItems()) {
            if (ruleArg instanceof SelectionItemGroup) {
                groups.add((SelectionItemGroup)ruleArg);
            } else {
                arguments.add(ruleArg);
            }
        }

        AutoBean<SelectionItemGroup> rootAb = factory.selectionItemGroup();
        rootAb.as().setGroups(groups);
        rootAb.as().setArguments(arguments);
        rootAb.as().setSingleSelect(isSingleSelect());
        rootAb.as().setSelectionCascade(cascadeOptionsCombo.getValue());

        rootAb.setTag(SelectionItem.TO_BE_REMOVED, Lists.<SelectionItem> newArrayList(toBeRemoved));
        toBeRemoved.clear();

        return rootAb;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<SelectionItem>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    private final class IsDefaultColumnValueProvider implements ValueProvider<SelectionItem, Boolean> {
        @Override
        public Boolean getValue(SelectionItem object) {
            return object.isDefault();
        }

        @Override
        public void setValue(SelectionItem object, Boolean value) {
            if (value && isSingleSelect()) {
                if ((object instanceof SelectionItemGroup) && isCascadeToChildren()) {
                    // Do not allow a group to be checked if SingleSelection is enabled and
                    // selections cascade to children.
                    return;
                }

                // If the user is checking an argument, uncheck all other arguments.
                for (SelectionItem ruleArg : store.getAll()) {
                    if (ruleArg.isDefault()) {
                        ruleArg.setDefault(false);
                        store.update(ruleArg);
                    }
                }
            }

            // Set the default value on this item, cascading to its children if necessary.
            setDefaultValue(object, value);

            // Cascade the default value to this item's parents, if necessary.
            if (!value && isCascadeToChildren()) {
                for (SelectionItem parent = store.getParent(object); parent != null; parent = store.getParent(parent)) {
                    parent.setDefault(value);
                    store.update(parent);
                }
            }
        }

        @Override
        public String getPath() {
            return LIST_RULE_ARG_IS_DEFAULT;
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
            cascadeOptionsCombo.setValue(treeCheckCascade);
        }

        @Override
        protected void setSingleSelect(boolean singleSelect) {
            forceSingleSelectCheckBox.setValue(singleSelect);
        }

        @Override
        protected void setItems(SelectionItemGroup root) {
            setValues(root);
        }

        @Override
        protected CheckCascade getCheckStyle() {
            CheckCascade ret = null;
            if (!presenter.isEditingMode()) {
                return null;
            } else if (cascadeOptionsCombo.getCurrentValue() != null) {
                ret = cascadeOptionsCombo.getCurrentValue();
            }
            return ret;
        }

        @Override
        protected boolean getSingleSelect() {
            return forceSingleSelectCheckBox.getValue();
        }

        @Override
        protected boolean shouldFlush() {
            boolean b = presenter.getValueChangeEventSource() == SelectionItemTreePropertyEditor.this;
            return b;
        }
    }

    private final class CascadeOptionsComboSelectionHandler implements SelectionHandler<CheckCascade> {
        @Override
        public void onSelection(SelectionEvent<CheckCascade> event) {
            ValueChangeEvent.fire(SelectionItemTreePropertyEditor.this, Lists.<SelectionItem> newArrayList(selectionItemsEditor.getCurrentTree()));
        }
    }

}
