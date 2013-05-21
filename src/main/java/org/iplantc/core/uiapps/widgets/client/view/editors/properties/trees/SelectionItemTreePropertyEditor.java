package org.iplantc.core.uiapps.widgets.client.view.editors.properties.trees;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup.CheckCascade;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.de.client.UUIDService;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreHandlers;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreSortEvent;
import com.sencha.gxt.data.shared.event.StoreUpdateEvent;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.TreeGridDragSource;
import com.sencha.gxt.dnd.core.client.TreeGridDropTarget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent;
import com.sencha.gxt.widget.core.client.event.ViewReadyEvent.ViewReadyHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;

/**
 * A container with a TreeGrid editor and button toolbar for editing hierarchical list selectors.
 * 
 * @author psarando 
 * 
 */
public class SelectionItemTreePropertyEditor extends VerticalLayoutContainer {

    private static final String LIST_RULE_ARG_NAME = "name"; //$NON-NLS-1$
    private static final String LIST_RULE_ARG_VALUE = "value"; //$NON-NLS-1$
    private static final String LIST_RULE_ARG_DISPLAY = "display"; //$NON-NLS-1$
    private static final String LIST_RULE_ARG_DESCRIPTION = "description"; //$NON-NLS-1$
    private static final String LIST_RULE_ARG_IS_DEFAULT = "isDefault"; //$NON-NLS-1$

    private final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
    private TreeGrid<SelectionItem> treeEditor;
    private CheckBox forceSingleSelection;
    private SimpleComboBox<CheckCascade> cascade;
    private SelectionItemGroup dragParent;
    private int countGroupLabel = 1;
    private int countArgLabel = 1;

    public SelectionItemTreePropertyEditor(SelectionItemGroup root) {
        init();
        setValues(root);
    }

    public void addUpdateCommand(final Command cmdUpdate) {
        if (cmdUpdate != null) {
            if (forceSingleSelection != null) {
                forceSingleSelection.addChangeHandler(new ChangeHandler() {

                    @Override
                    public void onChange(ChangeEvent event) {
                        cmdUpdate.execute();
                    }
                });
            }

            if (cascade != null) {
                cascade.addValueChangeHandler(new ValueChangeHandler<CheckCascade>() {

                    @Override
                    public void onValueChange(ValueChangeEvent<CheckCascade> event) {
                        cmdUpdate.execute();
                    }
                });
            }

            if (treeEditor != null) {
                treeEditor.getStore().addStoreHandlers(new StoreHandlers<SelectionItem>() {

                    @Override
                    public void onSort(StoreSortEvent<SelectionItem> event) {
                        // do nothing, sorting the grid does not sort the store.
                    }

                    @Override
                    public void onRecordChange(StoreRecordChangeEvent<SelectionItem> event) {
                        cmdUpdate.execute();
                    }

                    @Override
                    public void onDataChange(StoreDataChangeEvent<SelectionItem> event) {
                        cmdUpdate.execute();
                    }

                    @Override
                    public void onUpdate(StoreUpdateEvent<SelectionItem> event) {
                        cmdUpdate.execute();
                    }

                    @Override
                    public void onClear(StoreClearEvent<SelectionItem> event) {
                        cmdUpdate.execute();
                    }

                    @Override
                    public void onFilter(StoreFilterEvent<SelectionItem> event) {
                        // do nothing
                    }

                    @Override
                    public void onRemove(StoreRemoveEvent<SelectionItem> event) {
                        cmdUpdate.execute();
                    }

                    @Override
                    public void onAdd(StoreAddEvent<SelectionItem> event) {
                        cmdUpdate.execute();
                    }
                });
            }
        }
    }

    private void init() {
        setHeight(200);
        setBorders(true);

        initTreeEditor();

        add(buildToolBar(), new VerticalLayoutData(1, -1));
        add(treeEditor, new VerticalLayoutData(1, 1));
    }

    private void initTreeEditor() {
        RowNumberer<SelectionItem> numberer = buildRowNumbererColumn();
        ColumnConfig<SelectionItem, Boolean> defaultConfig = buildIsDefaultConfig();
        ColumnConfig<SelectionItem, String> displayConfig = buildDisplayConfig();
        ColumnConfig<SelectionItem, String> nameConfig = buildNameConfig();
        ColumnConfig<SelectionItem, String> valueConfig = buildValueConfig();
        ColumnConfig<SelectionItem, String> descriptionConfig = buildDescriptionConfig();

        List<ColumnConfig<SelectionItem, ?>> cmList = new ArrayList<ColumnConfig<SelectionItem, ?>>();
        cmList.add(numberer);
        cmList.add(defaultConfig);
        cmList.add(displayConfig);
        cmList.add(nameConfig);
        cmList.add(valueConfig);
        cmList.add(descriptionConfig);

        final ColumnModel<SelectionItem> cm = new ColumnModel<SelectionItem>(cmList);

        treeEditor = new TreeGrid<SelectionItem>(buildStore(), cm, displayConfig);
        treeEditor.getView().setAutoExpandColumn(displayConfig);
        treeEditor.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // This handler ensures that empty root groups still display with a group icon.
        treeEditor.addViewReadyHandler(new ViewReadyHandler() {

            @Override
            public void onViewReady(ViewReadyEvent event) {
                TreeStore<SelectionItem> store = treeEditor.getTreeStore();

                List<SelectionItem> rootItems = store.getRootItems();
                if (rootItems != null) {
                    for (SelectionItem root : rootItems) {
                        if (root instanceof SelectionItemGroup) {
                            SelectionItemGroup group = (SelectionItemGroup)root;

                            if (treeEditor.isLeaf(group)) {
                                treeEditor.setLeaf(group, false);
                                treeEditor.refresh(group);
                            }
                        }
                    }
                }
            }
        });

        // This handler ensures that empty subgroups still display with a group icon.
        treeEditor.addExpandHandler(new ExpandItemHandler<SelectionItem>() {

            @Override
            public void onExpand(ExpandItemEvent<SelectionItem> event) {
                SelectionItemGroup parent = (SelectionItemGroup)event.getItem();

                if (parent.getGroups() != null) {
                    for (SelectionItemGroup group : parent.getGroups()) {
                        treeEditor.setLeaf(group, false);
                        treeEditor.refresh(group);
                    }
                }
            }
        });

        numberer.initPlugin(treeEditor);

        GridInlineEditing<SelectionItem> editing = new GridInlineEditing<SelectionItem>(treeEditor);
        editing.addEditor(displayConfig, new TextField());
        editing.addEditor(nameConfig, new TextField());
        editing.addEditor(valueConfig, new TextField());
        editing.addEditor(descriptionConfig, new TextField());

        initDragNDrop();

        updateGxt3XdomZindex();
    }

    /**
     * TODO PSAR temp. solution for zindex incompatibility between GXT 2.x
     * {@link com.extjs.gxt.ui.client.widget.WindowManager} and GXT 3.x feedback proxy and grid menus.
     */
    private void updateGxt3XdomZindex() {
        XDOM.getTopZIndex(com.extjs.gxt.ui.client.core.XDOM.getTopZIndex());
    }

    private ColumnConfig<SelectionItem, Boolean> buildIsDefaultConfig() {
        ColumnConfig<SelectionItem, Boolean> defaultConfig = new ColumnConfig<SelectionItem, Boolean>(new ValueProvider<SelectionItem, Boolean>() {

                    @Override
            public Boolean getValue(SelectionItem object) {
                        return object.isDefault();
                    }

                    @Override
            public void setValue(SelectionItem object, Boolean value) {
                TreeStore<SelectionItem> store = treeEditor.getTreeStore();

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
                    for (SelectionItem parent = store.getParent(object);
                                    parent != null;
                                    parent = store.getParent(parent)) {
                                parent.setDefault(value);
                                store.update(parent);
                            }
                        }
                    }

                    @Override
                    public String getPath() {
                        return LIST_RULE_ARG_IS_DEFAULT;
                    }
                });

        defaultConfig.setHeader("Default");
        defaultConfig.setWidth(50);
        defaultConfig.setCell(new CheckBoxCell());
        defaultConfig.setSortable(false);

        return defaultConfig;
    }

    private void setDefaultValue(SelectionItem object, Boolean value) {
        if (object == null) {
            return;
        }

        object.setDefault(value);
        treeEditor.getTreeStore().update(object);

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

    private ColumnConfig<SelectionItem, String> buildDisplayConfig() {
        ColumnConfig<SelectionItem, String> displayConfig = new ColumnConfig<SelectionItem, String>(new ValueProvider<SelectionItem, String>() {

                    @Override
            public String getValue(SelectionItem object) {
                        return object.getDisplay();
                    }

                    @Override
            public void setValue(SelectionItem object, String value) {
                        object.setDisplay(value);
                    }

                    @Override
                    public String getPath() {
                        return LIST_RULE_ARG_DISPLAY;
                    }
                });

        displayConfig.setHeader("Display");
        displayConfig.setSortable(false);

        return displayConfig;
    }

    private ColumnConfig<SelectionItem, String> buildNameConfig() {
        ColumnConfig<SelectionItem, String> nameConfig = new ColumnConfig<SelectionItem, String>(new ValueProvider<SelectionItem, String>() {

                    @Override
            public String getValue(SelectionItem object) {
                        return object.getName();
                    }

                    @Override
            public void setValue(SelectionItem object, String value) {
                        object.setName(value);
                    }

                    @Override
                    public String getPath() {
                        return LIST_RULE_ARG_NAME;
                    }
                });

        nameConfig.setHeader("Argument");
        nameConfig.setSortable(false);

        return nameConfig;
    }

    private ColumnConfig<SelectionItem, String> buildValueConfig() {
        ColumnConfig<SelectionItem, String> valueConfig = new ColumnConfig<SelectionItem, String>(new ValueProvider<SelectionItem, String>() {

                    @Override
            public String getValue(SelectionItem object) {
                        return object.getValue();
                    }

                    @Override
            public void setValue(SelectionItem object, String value) {
                        object.setValue(value);
                    }

                    @Override
                    public String getPath() {
                        return LIST_RULE_ARG_VALUE;
                    }
                });

        valueConfig.setHeader("Value");
        valueConfig.setSortable(false);

        return valueConfig;
    }

    private ColumnConfig<SelectionItem, String> buildDescriptionConfig() {
        ColumnConfig<SelectionItem, String> descriptionConfig = new ColumnConfig<SelectionItem, String>(new ValueProvider<SelectionItem, String>() {

                    @Override
            public String getValue(SelectionItem object) {
                        return object.getDescription();
                    }

                    @Override
            public void setValue(SelectionItem object, String value) {
                        object.setDescription(value);
                    }

                    @Override
                    public String getPath() {
                        return LIST_RULE_ARG_DESCRIPTION;
                    }
                });

        descriptionConfig.setHeader("Tool tip text");
        descriptionConfig.setSortable(false);

        return descriptionConfig;
    }

    private TreeStore<SelectionItem> buildStore() {
        TreeStore<SelectionItem> store = new TreeStore<SelectionItem>(new ModelKeyProvider<SelectionItem>() {
                    @Override
            public String getKey(SelectionItem item) {
                        return item.getId();
                    }
                });

        store.setAutoCommit(true);

        return store;
    }

    private RowNumberer<SelectionItem> buildRowNumbererColumn() {
        IdentityValueProvider<SelectionItem> identity = new IdentityValueProvider<SelectionItem>();
        RowNumberer<SelectionItem> numberer = new RowNumberer<SelectionItem>(identity);

        return numberer;
    }

    private void initDragNDrop() {
        TreeGridDragSource<SelectionItem> dragSource = new TreeGridDragSource<SelectionItem>(
                treeEditor);
        dragSource.addDragStartHandler(new DndDragStartHandler() {

            @Override
            public void onDragStart(DndDragStartEvent event) {
                updateGxt3XdomZindex();

                SelectionItem selection = getDragSelection((List<?>)event.getData());
                if (selection != null) {
                    dragParent = (SelectionItemGroup)treeEditor.getTreeStore().getParent(selection);
                } else {
                    dragParent = null;
                }
            }
        });

        TreeGridDropTarget<SelectionItem> target = new TreeGridDropTarget<SelectionItem>(
                treeEditor);
        target.setAllowSelfAsSource(true);
        target.setFeedback(Feedback.BOTH);
        target.addDropHandler(new DndDropHandler() {

            @Override
            public void onDrop(DndDropEvent event) {
                SelectionItem selection = getDragSelection((List<?>)event.getData());
                if (selection == null) {
                    return;
                }

                TreeStore<SelectionItem> store = treeEditor.getTreeStore();
                SelectionItemGroup newParent = (SelectionItemGroup)store.getParent(selection);

                if (dragParent != null && newParent != dragParent) {
                    if (selection instanceof SelectionItemGroup) {
                        dragParent.getGroups().remove(selection);
                    } else {
                        dragParent.getArguments().remove(selection);
                    }

                    // Updating the store may not be needed for the grid, but it calls any update
                    // commands added to store handlers.
                    store.update(dragParent);
                }

                if (newParent != null) {
                    // Reorder the new parent's children to match the store's order.
                    List<SelectionItemGroup> groups = newParent.getGroups();
                    if (groups == null) {
                        groups = new ArrayList<SelectionItemGroup>();
                        newParent.setGroups(groups);
                    } else {
                        groups.clear();
                    }

                    List<SelectionItem> arguments = newParent.getArguments();
                    if (arguments == null) {
                        arguments = new ArrayList<SelectionItem>();
                        newParent.setArguments(arguments);
                    } else {
                        arguments.clear();
                    }

                    for (SelectionItem arg : store.getChildren(newParent)) {
                        if (arg instanceof SelectionItemGroup) {
                            groups.add((SelectionItemGroup)arg);
                        } else {
                            arguments.add(arg);
                        }
                    }

                    // Updating the store may not be needed for the grid, but it calls any update
                    // commands added to store handlers.
                    store.update(newParent);
                }
            }
        });
    }

    /**
     * A helper method for getting the selected item from a drag-start or drop event.
     * 
     * @param items
     * @return
     */
    @SuppressWarnings("unchecked")
    private SelectionItem getDragSelection(List<?> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }

        SelectionItem selection;
        if (items.get(0) instanceof TreeStore.TreeNode) {
            selection = ((TreeStore.TreeNode<SelectionItem>)items.get(0)).getData();
        } else {
            selection = (SelectionItem)items.get(0);
        }

        return selection;
    }

    private ToolBar buildToolBar() {
        ToolBar buttonBar = new ToolBar();

        buttonBar.setHorizontalSpacing(4);

        buttonBar.add(buildAddGroupButton());
        buttonBar.add(buildAddArgumentButton());
        buttonBar.add(buildCheckCascadeLabel());
        buttonBar.add(buildCascadeOptions());
        buttonBar.add(buildForceSingleSelectionFlag());
        buttonBar.add(new FillToolItem());
        buttonBar.add(buildDeleteButton());

        return buttonBar;
    }

    private CheckBox buildForceSingleSelectionFlag() {
        forceSingleSelection = new CheckBox();

        forceSingleSelection.setBoxLabel("Single selection only");
        forceSingleSelection.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                if (isSingleSelect()) {
                    List<SelectionItem> checked = new ArrayList<SelectionItem>();

                    for (SelectionItem ruleArg : treeEditor.getTreeStore().getAll()) {
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
            }
        });

        forceSingleSelection.setToolTip("Allow the user to select only one item in the list. Does not prevent Groups from becoming selected if cascading is enabled for Parents.");

        return forceSingleSelection;
    }

    private boolean isSingleSelect() {
        return forceSingleSelection.getValue();
    }

    private LabelToolItem buildCheckCascadeLabel() {
        LabelToolItem labelCheckCascade = new LabelToolItem("Check Cascade" + ": "); //$NON-NLS-1$
        labelCheckCascade.setToolTip("Configure user selection cascade behavior.");

        return labelCheckCascade;
    }

    private SimpleComboBox<CheckCascade> buildCascadeOptions() {
        cascade = new SimpleComboBox<CheckCascade>(new LabelProvider<CheckCascade>() {

            @Override
            public String getLabel(CheckCascade item) {
                return item.getDisplay();
            }
        });

        cascade.setToolTip("Configure user selection cascade behavior.");
        cascade.setWidth(80);
        cascade.setTriggerAction(TriggerAction.ALL);
        cascade.setEditable(false);

        cascade.add(CheckCascade.TRI);
        cascade.add(CheckCascade.PARENTS);
        cascade.add(CheckCascade.CHILDREN);
        cascade.add(CheckCascade.NONE);

        cascade.setValue(CheckCascade.TRI);

        return cascade;
    }

    private boolean isCascadeToChildren() {
        return cascade.getValue() == CheckCascade.TRI || cascade.getValue() == CheckCascade.CHILDREN;
    }

    private TextButton buildAddGroupButton() {
        TextButton ret = new TextButton();

        ret.setToolTip("Add group");
        ret.setIcon(IplantResources.RESOURCES.folderAdd());

        ret.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                UUIDServiceAsync service = GWT.create(UUIDService.class);
                service.getUUIDs(1, new AsyncCallback<List<String>>() {
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
        });

        return ret;
    }

    private TextButton buildAddArgumentButton() {
        TextButton ret = new TextButton();

        ret.setToolTip("Add Argument");
        ret.setIcon(IplantResources.RESOURCES.add());

        ret.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                UUIDServiceAsync service = GWT.create(UUIDService.class);
                service.getUUIDs(1, new AsyncCallback<List<String>>() {
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
        });

        return ret;
    }

    private TextButton buildDeleteButton() {
        TextButton ret = new TextButton();

        ret.setToolTip(I18N.DISPLAY.delete());
        ret.setIcon(IplantResources.RESOURCES.cancel());

        ret.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                SelectionItem selected = treeEditor.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    TreeStore<SelectionItem> store = treeEditor.getTreeStore();

                    SelectionItem parent = store.getParent(selected);
                    if (parent != null) {
                        SelectionItemGroup group = (SelectionItemGroup)parent;

                        if (selected instanceof SelectionItemGroup) {
                            group.getGroups().remove(selected);
                        } else {
                            group.getArguments().remove(selected);
                        }
                    }

                    store.remove(selected);
                }
            }
        });

        return ret;
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

        treeEditor.setLeaf(group, false);
        treeEditor.refresh(group);
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
        SelectionItem selected = treeEditor.getSelectionModel().getSelectedItem();

        if (selected != null) {
            if (selected instanceof SelectionItemGroup) {
                return (SelectionItemGroup)selected;
            } else {
                selected = treeEditor.getTreeStore().getParent(selected);
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

            treeEditor.getTreeStore().add(selectedGroup, ruleArg);
            treeEditor.setExpanded(selectedGroup, true);
        } else {
            treeEditor.getTreeStore().add(ruleArg);
        }

        XElement fxEl = XElement.as(treeEditor.getTreeView().getRow(ruleArg));
        fxEl.scrollIntoView();
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

    /**
     * Resets the editor with the values in the given root group.
     * 
     * @param root
     */
    public void setValues(SelectionItemGroup root) {
        TreeStore<SelectionItem> store = treeEditor.getTreeStore();
        store.clear();

        if (root != null) {
            forceSingleSelection.setValue(root.isSingleSelect());

            if (root.getSelectionCascade() != null) {
                cascade.setValue(root.getSelectionCascade());
            }

            if (root.getGroups() != null) {
                for (SelectionItemGroup group : root.getGroups()) {
                    addGroupToStore(null, group);

                    if (treeEditor.isViewReady()) {
                        treeEditor.setLeaf(group, false);
                        treeEditor.refresh(group);
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

    private void addGroupToStore(SelectionItemGroup parent, SelectionItemGroup group) {
        if (group != null) {
            TreeStore<SelectionItem> store = treeEditor.getTreeStore();

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
     * Returns a group of values added by the editor.
     * 
     * @return A root group containing the groups and args added by the editor.
     */
    public SelectionItemGroup getValues() {
        List<SelectionItemGroup> groups = new ArrayList<SelectionItemGroup>();
        List<SelectionItem> arguments = new ArrayList<SelectionItem>();

        for (SelectionItem ruleArg : treeEditor.getTreeStore().getRootItems()) {
            if (ruleArg instanceof SelectionItemGroup) {
                groups.add((SelectionItemGroup)ruleArg);
            } else {
                arguments.add(ruleArg);
            }
        }

        SelectionItemGroup root = factory.selectionItemGroup().as();
        root.setGroups(groups);
        root.setArguments(arguments);
        root.setSingleSelect(isSingleSelect());
        root.setSelectionCascade(cascade.getValue());

        return root;
    }
}
