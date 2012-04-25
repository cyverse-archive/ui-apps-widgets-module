package org.iplantc.core.client.widgets.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.dialogs.IFileSelectDialog;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.IDiskResourceSelectorBuilder;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.metadata.client.validation.MetaDataValidator;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Permissions;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

/**
 * Custom widget for allowing the user to select single end reads.
 * 
 * @author amuir
 * 
 */
public class MultiFileSelector extends WizardWidgetPanel {
    private String idProperty;
    private IFileSelectDialog dlgFileSelect;
    private Grid<File> grid;
    private Label caption;
    private List<Button> buttons;
    private final IDiskResourceSelectorBuilder diskResourceSelectorBuilder;

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public MultiFileSelector(final Property property, final ComponentValueTable tblComponentVals,
            final List<String> params, IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        super(property, tblComponentVals, params, diskResourceSelectorBuilder);

        this.diskResourceSelectorBuilder = diskResourceSelectorBuilder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInstanceVariables(final Property property, final List<String> params) {
        caption = buildAsteriskLabel(property.getLabel());
        grid = buildGrid(property);
        addDragDop();

        setToolTip(caption, property);
        setToolTip(grid, property);
    }

    private void addDragDop() {
        new GridDropTarget(grid) {
            @Override
            protected void onDragDrop(DNDEvent event) {
                List<ModelData> files = event.getData();
                if (files != null) {
                    for (ModelData f : files) {
                        if (f instanceof File) {
                            File file = (File)f;
                            doSelection(file);
                        }
                    }
                }

            }

        };
    }

    private ContentPanel buildGridPanel() {
        ContentPanel ret = new ContentPanel();

        ret.setHeaderVisible(false);
        ret.setWidth(340);
        ret.add(grid);
        ret.setTopComponent(buildToolbar());
        return ret;
    }

    private JSONArray buildValueJsonArray() {
        JSONArray ret = null;

        List<File> fileList = grid.getStore().getModels();
        if (fileList.size() > 0) {
            ret = new JSONArray();

            for (int i = 0,listSize = fileList.size(); i < listSize; i++) {
                File file = fileList.get(i);
                ret.set(i, new JSONString(file.getId()));
            }
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponentValueTable() {
        tblComponentVals.setValue(idProperty, buildValueJsonArray());
        tblComponentVals.validate();
    }

    private void addSelectedFileToGrid() {
        File fileSelected = dlgFileSelect.getSelectedFile();

        if (fileSelected != null) {
            grid.getStore().add(fileSelected);
            updateComponentValueTable();
        }
    }

    private void launchAddDialog(SelectionListener<ButtonEvent> handler) {
        final String TAG = "MultiFileSelector";

        dlgFileSelect = diskResourceSelectorBuilder.buildFileSelectorDialog(TAG,
                I18N.DISPLAY.selectFile(), null, null);
        dlgFileSelect.addOkClickHandler(handler);

        dlgFileSelect.show();
    }

    private void deleteSelectedFiles() {
        List<File> files = grid.getSelectionModel().getSelectedItems();

        ListStore<File> store = grid.getStore();

        for (File file : files) {
            store.remove(file);
        }

        updateComponentValueTable();
    }

    private Button buildAddButton() {
        Button ret = new Button(I18N.DISPLAY.add());

        ret.setId("idFileAddBtn");

        ret.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                launchAddDialog(new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        addSelectedFileToGrid();
                    }
                });

            }
        });

        return ret;
    }

    private Button buildDeleteButton() {
        Button ret = new Button(I18N.DISPLAY.delete());

        ret.setId("idFileDeleteBtn");
        ret.setEnabled(false);

        ret.addSelectionListener(new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                deleteSelectedFiles();
            }
        });

        return ret;
    }

    private ToolBar buildToolbar() {
        ToolBar ret = new ToolBar();

        ret.setEnableOverflow(false);
        ret.setAlignment(HorizontalAlignment.RIGHT);

        buttons = new ArrayList<Button>();

        buttons.add(buildAddButton());
        buttons.add(buildDeleteButton());

        for (Button button : buttons) {
            ret.add(button);
        }

        return ret;
    }

    private Button getButton(String id) {
        Button ret = null; // assume failure

        for (Button btn : buttons) {
            if (btn.getId().equals(id)) {
                ret = btn;
                break;
            }
        }

        return ret;
    }

    private void updateButton(String idBtn, boolean enable) {
        Button btn = getButton(idBtn);

        if (btn != null) {
            btn.setEnabled(enable);
        }
    }

    private void updateDeleteButton() {
        List<File> files = grid.getSelectionModel().getSelectedItems();

        updateButton("idFileDeleteBtn", files.size() > 0);
    }

    private ColumnModel buildColumnModel() {
        ColumnConfig name = new ColumnConfig("name", I18N.DISPLAY.fileName(), 330);
        name.setSortable(false);
        name.setMenuDisabled(true);

        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

        columns.add(name);

        return new ColumnModel(columns);
    }

    private IPlantValidator buildValidator(final Property property) {
        IPlantValidator ret = null; // assume failure
        MetaDataValidator validator = property.getValidator();

        if (validator != null) {
            ret = new IPlantValidator(tblComponentVals, validator);
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initValidators(final Property property) {
        IPlantValidator validator = buildValidator(property);

        if (validator != null) {
            tblComponentVals.setValidator(property.getId(), validator);
        }
    }

    private Grid<File> buildGrid(final Property property) {
        ColumnModel columnModel = buildColumnModel();
        ListStore<File> store = new ListStore<File>();

        Grid<File> ret = new Grid<File>(store, columnModel);
        ret.getView().setEmptyText(
                I18N.DISPLAY.noFiles() + "<br/>"
                        + org.iplantc.core.uicommons.client.I18N.DISPLAY.dragAndDropPrompt());

        ret.getSelectionModel().addListener(Events.SelectionChange, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                updateDeleteButton();
            }
        });

        ret.getView().setShowDirtyCells(false);
        ret.setHeight(260);

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setWidgetId(String id) {
        idProperty = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        add(caption);
        add(buildGridPanel());
    }

    @Override
    protected void setValue(String value) {
        // value is array of file ids
        if (value != null && !value.isEmpty()) {
            JSONArray arr = JSONParser.parseStrict(value).isArray();
            if (arr != null && arr.size() > 0) {
                ListStore<File> files = grid.getStore();
                for (int i = 0; i < arr.size(); i++) {
                    String name = DiskResourceUtil.parseNameFromPath(JsonUtil
                            .trim(arr.get(i).toString()));
                    File f = new File(value, name, new Permissions(true, true, true));
                    files.add(f);
                }
            }
            updateComponentValueTable();
        }
    }

    private void doSelection(File f) {
        ListStore<File> files = grid.getStore();
        files.add(f);
        updateComponentValueTable();
    }
}
