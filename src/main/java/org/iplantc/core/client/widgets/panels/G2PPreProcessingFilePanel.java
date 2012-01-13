package org.iplantc.core.client.widgets.panels;

import java.util.ArrayList;
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

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.user.client.Command;

/**
 * Custom widget for selecting G2P pre-processing files.
 * 
 * @author amuir
 * 
 */
public class G2PPreProcessingFilePanel extends WizardWidgetPanel {
    private final int PANEL_WIDTH = 460;
    private final String READ2_FILE_ID = "read2FileId";
    private final String READ_LENGTH = "readLength";

    private String idProperty;
    private VerticalPanel pnlDetails;
    private VerticalPanel pnlAssociate;
    private VerticalPanel pnlSplit;
    private IPlantValidator validatorAssociate;
    private IPlantValidator validatorConcat;

    private RadioGroup grpRadio;
    private TextField<String> fieldReadLength;
    private IFileSelector fileSelector;
    private ContentPanel pnlRadioGroup;

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public G2PPreProcessingFilePanel(final Property property,
            final ComponentValueTable tblComponentVals, final List<String> params,
            IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
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
    protected void addSupportingComponentVals() {
        tblComponentVals.setValue(READ2_FILE_ID, fieldReadLength.getValue());
        tblComponentVals.setFormatter(READ2_FILE_ID, new GeneralTextFormatter());

        File file = fileSelector.getSelectedFile();

        String idFile = (file == null) ? "" : file.getId();
        tblComponentVals.setValue(READ_LENGTH, idFile);
        tblComponentVals.setFormatter(READ_LENGTH, new GeneralTextFormatter());
    }

    private void initAssociateValidator() {
        String json = "{\"name\" : \"Associated file field\", \"required\" : true}";

        validatorAssociate = new IPlantValidator(tblComponentVals, ValidatorHelper.buildValidator(json));
    }

    private void initSplitValidator() {
        String json = "{\"name\" : \"Length of read field\", \"required\" : true, \"rules\" : [{\"IntAbove\" : [0]}]}";

        validatorConcat = new IPlantValidator(tblComponentVals, ValidatorHelper.buildValidator(json));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInstanceVariables(final Property property, final List<String> params) {
        idProperty = property.getId();
        initDetailsPanel(property);
        pnlRadioGroup = buildRadioGroup(property.getValue());
    }

    private void initDetailsPanel(final Property property) {
        pnlDetails = initBodyPanel();
        pnlDetails.setSpacing(5);
        pnlDetails.setBorders(true);
        pnlDetails.setWidth(PANEL_WIDTH);
        pnlDetails.setStyleName("accordianbody");
        pnlDetails.hide();

        initAssociatePanel(property);
        initSplitPanel();
    }

    private HorizontalPanel buildFileSelectorPanel() {
        HorizontalPanel ret = new HorizontalPanel();
        ret.setSpacing(2);

        ret.add(fileSelector.getWidget());

        return ret;
    }

    private void initAssociatePanel(final Property property) {
        pnlAssociate = new VerticalPanel();

        pnlAssociate.add(buildAsteriskLabel(I18N.DISPLAY.ppSelectMatePair()));

        pnlAssociate.add(buildFileSelectorPanel());
    }

    private void buildReadLengthTextField() {
        fieldReadLength = new TextField<String>();
        fieldReadLength.setWidth(48);
        fieldReadLength.setValue(Integer.toString(36));
        fieldReadLength.setSelectOnFocus(true);
        fieldReadLength.setId("idConcatenatedReadLength");

        fieldReadLength.addListener(Events.OnBlur, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent be) {
                updateComponentValueTable();

                tblComponentVals.setValidator(idProperty, validatorConcat);

                // after we update the table, we need to validate the entire table
                tblComponentVals.validate();
            }
        });
        addValidationListener(fieldReadLength);
    }

    private HorizontalPanel buildSplitConcatBody() {
        HorizontalPanel ret = new HorizontalPanel();

        TableData td = new TableData();
        td.setHorizontalAlign(HorizontalAlignment.RIGHT);

        buildReadLengthTextField();

        ret.add(new LabelField(I18N.DISPLAY.ppLengthOfRead()), td);
        ret.add(fieldReadLength, td);

        return ret;
    }

    private VerticalPanel initBodyPanel() {
        VerticalPanel ret = new VerticalPanel();

        ret.setBorders(false);

        return ret;
    }

    private void initSplitPanel() {
        pnlSplit = initBodyPanel();

        pnlSplit.add(buildSplitConcatBody());
        pnlSplit.add(new Label(I18N.DISPLAY.ppConcatDesc1()));
        pnlSplit.add(new Label(I18N.DISPLAY.ppConcatDesc2()));
    }

    private void hideDetailsPanel() {
        pnlDetails.hide();
    }

    private void showDetailsPanel() {
        pnlDetails.show();
        pnlDetails.layout();
    }

    private void clearValidators() {
        tblComponentVals.clearValidator(idProperty);
        tblComponentVals.clearValidator(READ2_FILE_ID);
        tblComponentVals.clearValidator(READ_LENGTH);
    }

    private void singleEndReadSelected() {
        hideDetailsPanel();

        // clear our validators
        clearValidators();

        updateComponentValueTable();
        tblComponentVals.validate();
    }

    private void concatenatedSelected() {
        pnlDetails.removeAll();
        pnlDetails.add(pnlSplit);

        clearValidators();
        tblComponentVals.setValidator(READ_LENGTH, validatorConcat);

        updateComponentValueTable();
        tblComponentVals.validate();

        showDetailsPanel();
    }

    private void associatedSelected() {
        pnlDetails.removeAll();
        pnlDetails.add(pnlAssociate);

        updateComponentValueTable();

        // update validators
        clearValidators();
        tblComponentVals.setValidator(READ2_FILE_ID, validatorAssociate);

        tblComponentVals.validate();

        showDetailsPanel();
    }

    private int parseIndex(String value) {
        int ret = 0;

        int idx = value.indexOf("|");

        // if we do not have a vertical bar, we may still have a valid index
        if (idx == -1) {
            idx = value.length();
        }

        String valAsString = value.substring(0, idx);

        if (ValidatorHelper.isInteger(valAsString)) {
            ret = Integer.parseInt(valAsString);
        }

        return ret;
    }

    private String parseValue(String value) {
        String ret = "";

        int idx = value.indexOf("|");

        if (idx > -1) {
            ret = value.substring(idx + 1, value.length());
        }

        return ret;
    }

    private void setValue(ArrayList<Radio> radioButtons, int idx, String value) {
        Radio radio = radioButtons.get(idx);

        radio.setValue(true);

        switch (idx) {
            case 1:
                fieldReadLength.setValue(value);
                break;

            case 2:
                fileSelector.displayFilename(value);
                break;

            default:
                break;
        }
    }

    private void setValue(ArrayList<Radio> radioButtons, String value) {
        int idx = parseIndex(value);

        if (idx > -1 && idx < radioButtons.size()) {
            String val = parseValue(value);

            setValue(radioButtons, idx, val);
        }
    }

    private Radio buildRadio(String caption, String id, String itemId, Listener<FieldEvent> listener) {
        Radio ret = new Radio();

        ret.setBoxLabel(caption);
        ret.setId(id);
        ret.setItemId(itemId);
        ret.addListener(Events.Change, listener);

        return ret;
    }

    private ArrayList<Radio> buildRadioList() {
        ArrayList<Radio> ret = new ArrayList<Radio>();

        // add our three selections
        ret.add(buildRadio(I18N.DISPLAY.singleEndRead(), "idSingleEndRead", "0",
                new Listener<FieldEvent>() {
                    public void handleEvent(FieldEvent fe) {
                        // make sure we only call this when we are actually selected
                        Boolean val = (Boolean)fe.getValue();

                        if (val) {
                            singleEndReadSelected();
                        }
                    }
                }));

        ret.add(buildRadio(I18N.DISPLAY.pairedEndConcat(), "idPairedEndConcatenated", "1",
                new Listener<FieldEvent>() {
                    public void handleEvent(FieldEvent fe) {
                        // make sure we only call this when we are actually selected
                        Boolean val = (Boolean)fe.getValue();

                        if (val) {
                            concatenatedSelected();
                        }
                    }
                }));

        ret.add(buildRadio(I18N.DISPLAY.pairedEndAssoc(), "idPairedEndAssociated", "2",
                new Listener<FieldEvent>() {
                    public void handleEvent(FieldEvent fe) {
                        // make sure we only call this when we are actually selected
                        Boolean val = (Boolean)fe.getValue();

                        if (val) {
                            associatedSelected();
                        }
                    }
                }));

        return ret;
    }

    private ContentPanel buildRadioGroup(String value) {
        ContentPanel ret = new ContentPanel();
        ret.setHeaderVisible(false);
        ret.setWidth(PANEL_WIDTH);

        // add inner panel for formatting
        HorizontalPanel panelInner = new HorizontalPanel();
        panelInner.setSpacing(4);
        panelInner.setStyleName("accordianbody");

        ArrayList<Radio> radioButtons = buildRadioList();

        // build our radio group
        grpRadio = new RadioGroup();
        grpRadio.setOrientation(Orientation.VERTICAL);

        // add our radio buttons to the group
        for (Radio item : radioButtons) {
            grpRadio.add(item);
        }

        // set starting value
        setValue(radioButtons, value);

        panelInner.add(new Label(I18N.DISPLAY.fileContains()));
        panelInner.add(grpRadio);

        ret.add(panelInner);

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setWidgetId(String id) {
        // intentionally left blank
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initValidators(final Property property) {
        initAssociateValidator();
        initSplitValidator();

        fieldReadLength.setValidator(validatorConcat);
        fieldReadLength.setAutoValidate(true);
        fieldReadLength.setAllowBlank(!validatorConcat.isRequired());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initFormatters(final Property property) {
        tblComponentVals.setFormatter(idProperty, new GeneralTextFormatter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponentValueTable() {
        tblComponentVals.setValue(idProperty, grpRadio.getValue().getItemId());
        tblComponentVals.setValue(READ_LENGTH, fieldReadLength.getValue());

        File file = fileSelector.getSelectedFile();

        String idFile = (file == null) ? "" : file.getId();
        tblComponentVals.setValue(READ2_FILE_ID, idFile);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        add(pnlRadioGroup);
        add(pnlDetails);
    }
}
