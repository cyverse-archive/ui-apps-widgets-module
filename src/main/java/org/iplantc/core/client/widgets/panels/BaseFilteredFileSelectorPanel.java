package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

/**
 * Base filtered file selector panel. This contains a file grid which can be filtered by user input.
 * 
 * @author amuir
 * 
 * @param <T>
 */
public abstract class BaseFilteredFileSelectorPanel<T extends BaseModelData> extends VerticalPanel {
    protected ListStore<T> store;

    private Grid<T> grid;
    private boolean isMultiSelect;

    /**
     * Instantiate from multi-select flag.
     * 
     * @param isMultiSelect true to allow multiple items to be selected.
     */
    public BaseFilteredFileSelectorPanel(boolean isMultiSelect) {
        this.isMultiSelect = isMultiSelect;

        init();

        intitInstanceVariables();

        compose();
    }

    /**
     * @return the isMultiSelect
     */
    public boolean isMultiSelect() {
        return isMultiSelect;
    }

    private void init() {
        setSpacing(5);

        setLayout(new FitLayout());
    }

    private void buildGridSingleSelect() {
        GridSelectionModel<T> selectionModel = new GridSelectionModel<T>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);

        grid = new Grid<T>(store, buildColumnModel(null));
        grid.setSelectionModel(selectionModel);
    }

    private void buildGridMultiSelect() {
        CheckBoxSelectionModel<T> selectionModel = new CheckBoxSelectionModel<T>();

        grid = new Grid<T>(store, buildColumnModel(selectionModel));
        grid.addPlugin(selectionModel);
        grid.setSelectionModel(selectionModel);
        grid.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
    }

    private void setStyle() {
        String style = getStyle();

        if (style != null) {
            grid.setStyleName(style);
        }
    }

    private void setAutoExpand() {
        String idAutoExpand = getAutoExpandColumnId();

        if (idAutoExpand != null) {
            grid.setAutoExpandColumn(idAutoExpand);
        }
    }

    private void buildGrid() {
        if (isMultiSelect) {
            buildGridMultiSelect();
        } else {
            buildGridSingleSelect();
        }

        grid.setSize(400, 275);

        setAutoExpand();
        setStyle();

        grid.setBorders(true);
        grid.getView().setEmptyText(I18N.DISPLAY.noFiles());

        getData();
    }

    private void intitInstanceVariables() {
        store = new ListStore<T>();

        buildFilter();
        buildGrid();
    }

    private Label buildCaption() {
        Label ret = new Label(getCaption());

        ret.setStyleName("iplantc-caption-label");

        return ret;
    }

    private void compose() {
        add(buildCaption());

        StoreFilterField<T> filter = buildFilter();
        filter.bind(store);

        add(filter);
        add(grid);
    }

    /**
     * Retrieve selected items.
     * 
     * @return items the user has selected.
     */
    public List<T> getSelectedItems() {
        return grid.getSelectionModel().getSelectedItems();
    }

    /**
     * Attaches an event listener to the grid.
     * 
     * @param eventType type of event to listen for.
     * @param listener called when event is fired.
     */
    public void addGridEventListener(EventType eventType, Listener<? extends BaseEvent> listener) {
        grid.getSelectionModel().addListener(eventType, listener);
    }

    /**
     * Retrieve the caption.
     * 
     * @return selector caption.
     */
    protected abstract String getCaption();

    /**
     * Retrieve the style.
     * 
     * @return selector style.
     */
    protected abstract String getStyle();

    /**
     * Build search filter.
     * 
     * @return newly created filter.
     */
    protected abstract StoreFilterField<T> buildFilter();

    /**
     * Build column model for grid.
     * 
     * @param selectionModel checkbox selection model (if desired).
     * @return initialized column model.
     */
    protected abstract ColumnModel buildColumnModel(CheckBoxSelectionModel<T> selectionModel);

    /**
     * Retrieve grid column id which auto-expands.
     * 
     * @return id of auto expand column.
     */
    protected abstract String getAutoExpandColumnId();

    /**
     * Retrieve data to build grid.
     */
    protected abstract void getData();
}
