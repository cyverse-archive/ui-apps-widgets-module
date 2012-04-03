package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.GeneralTextFormatter;
import org.iplantc.core.client.widgets.utils.IDiskResourceSelectorBuilder;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.client.widgets.views.IFileSelector;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.uidiskresource.client.models.File;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.google.gwt.user.client.Command;

/**
 * Custom widget for simple mate pair selection.
 * 
 * @author amuir
 * 
 */
public class SimpleMatePanel extends WizardWidgetPanel {
    private final String FILE_READ_ID = "read2FileId";

    protected IFileSelector fileSelector;
    protected ContentPanel pnlMate;

    private CheckBox cbMateSelection;
    private IPlantValidator validatorFileSelect;

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public SimpleMatePanel(final Property property, final ComponentValueTable tblComponentVals,
            final List<String> params, IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        super(property, tblComponentVals, params, diskResourceSelectorBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildDiskResourceSelector(final Property property,
            final IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        fileSelector = diskResourceSelectorBuilder.buildFileSelector(new Command() {
            @Override
            public void execute() {
                updateComponentValueTable();
                tblComponentVals.validate();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInstanceVariables(final Property property, final List<String> params) {
        initMatePanel();
        initMateCheckbox(property);
    }

    private void initMatePanel() {
        pnlMate = new ContentPanel();

        pnlMate.setHeaderVisible(false);

        pnlMate.disable();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addSupportingComponentVals() {
        File file = fileSelector.getSelectedFile();
        String idFile = (file == null) ? "" : file.getId();

        tblComponentVals.setValue(FILE_READ_ID, idFile);
        tblComponentVals.setFormatter(FILE_READ_ID, new GeneralTextFormatter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponentValueTable() {
        tblComponentVals.setValue(cbMateSelection.getId(), cbMateSelection.getValue().toString());

        File file = fileSelector.getSelectedFile();

        String idFile = (file == null) ? "" : file.getId();
        tblComponentVals.setValue(FILE_READ_ID, idFile);
    }

    /**
     * Enables or disables the appropriate validators if the mate checkbox selection changes
     * 
     * @param enabled indicates true the update validators should be executed; otherwise, false.
     */
    protected void updateValidators(boolean enabled) {
        if (enabled) {
            // set our validator if the user checks the 'has mate pair' checkbox
            tblComponentVals.setValidator(FILE_READ_ID, validatorFileSelect);
        } else {
            // if the user does not have a mate pair, we do not need to validate
            tblComponentVals.clearValidator(FILE_READ_ID);
        }
    }

    private void initMateCheckbox(final Property property) {
        cbMateSelection = new CheckBox();
        cbMateSelection.setId(property.getId());
        cbMateSelection.setBoxLabel(property.getLabel());

        cbMateSelection.addListener(Events.Change, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent be) {
                boolean checked = cbMateSelection.getValue();

                pnlMate.setEnabled(checked);
                updateValidators(checked);
                updateComponentValueTable();
                tblComponentVals.validate();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initValidators(final Property property) {
        String json = "{\"name\" : \"Mate pair file\", \"required\" : true}";

        validatorFileSelect = new IPlantValidator(tblComponentVals, ValidatorHelper.buildValidator(json));
        fileSelector.setValidator(validatorFileSelect);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setWidgetId(String id) {
        cbMateSelection.setId(id);
    }

    /**
     * Layout widgets for the mate panel.
     */
    protected void composeMatePanel() {
        VerticalPanel panelInner = new VerticalPanel();

        panelInner.setSpacing(5);
        panelInner.setWidth(400);
        panelInner.addStyleName("accordianbody");

        panelInner.add(buildAsteriskLabel(I18N.DISPLAY.matePairSelection() + ":"));
        panelInner.add(fileSelector.getWidget());
        pnlMate.add(panelInner);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        add(cbMateSelection);
        composeMatePanel();
        add(pnlMate);
    }

    @Override
    protected void setValue(String value) {
        // TODO Auto-generated method stub

    }
}
