package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.IDiskResourceSelectorBuilder;
import org.iplantc.core.client.widgets.utils.ValidatorHelper;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.metadata.client.property.Property;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.VerticalPanel;

/**
 * Custom widget for G2P QC Pre-processing clipper selection.
 * 
 * @author amuir
 * 
 */
public class ClipperSelectorPanel extends DynamicFileFieldPanel {
    private final String CLIPPER_IMPORT_ID = "adapterFileId";
    private final String CLIPPER_FILENAME_ID = "adapterFilename";
    private final String CLIPPER_DATA_ID = "adapterFileContents";

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public ClipperSelectorPanel(final Property property, final ComponentValueTable tblComponentVals,
            final List<String> params, IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        super(property, tblComponentVals, params, diskResourceSelectorBuilder);
    }

    @Override
    protected void clearValidators() {
        tblComponentVals.clearValidator(CLIPPER_IMPORT_ID);
        tblComponentVals.clearValidator(CLIPPER_FILENAME_ID);
        tblComponentVals.clearValidator(CLIPPER_DATA_ID);
    }

    @Override
    protected void initDataValidator() {
        String json = "{\"name\" : \"Clipper data field\", \"required\" : true, \"rules\" : [{\"ClipperData\" : []}]}";

        validatorData = new IPlantValidator(tblComponentVals, ValidatorHelper.buildValidator(json));
    }

    @Override
    protected void initFilenameValidator() {
        String json = "{\"name\" : \"Clipper filename field\", \"required\" : true, \"rules\" : [{\"FileName\" : []}]}";

        validatorFilename = new IPlantValidator(tblComponentVals, ValidatorHelper.buildValidator(json));
    }

    @Override
    protected void initImportPanel() {
        String json = "{\"name\" : \"Clipper file field\", \"required\" : true}";

        validatorSelectedFile = new IPlantValidator(tblComponentVals,
                ValidatorHelper.buildValidator(json));
    }

    @Override
    protected void initNewPanel() {
        panelNewData = initBodyPanel();
        panelNewData.setSpacing(5);

        VerticalPanel panelTop = initBodyPanel();
        panelTop.add(buildAsteriskLabel(I18N.DISPLAY.createClipperFilename()));
        panelTop.add(textFieldFilename);

        VerticalPanel panelBottom = initBodyPanel();
        panelBottom.add(new Label(I18N.DISPLAY.createClipperFileData()));
        panelBottom.add(areaData);

        panelNewData.add(panelTop);
        panelNewData.add(panelBottom);

    }

    @Override
    protected void initSelectedFileValidator() {
        panelImportData = initBodyPanel();
        panelImportData.setSpacing(5);

        VerticalPanel panelInner = initBodyPanel();
        panelInner.add(new Label(I18N.DISPLAY.browseClipperFiles()));
        panelInner.add(fileSelector.getWidget());

        panelImportData.add(panelInner);

    }

    @Override
    protected void importDataSelected() {
        super.importDataSelected();
        // swap out our validators
        clearValidators();
        tblComponentVals.setValidator(CLIPPER_IMPORT_ID, validatorSelectedFile);

    }

    @Override
    protected void initTypeListBox(final String id) {
        super.initTypeListBox(id);
        // add our selections
        listboxType.addItem(I18N.DISPLAY.newClipper());
        listboxType.addItem(I18N.DISPLAY.importClipper());
    }

    @Override
    protected void newDataSelected() {
        super.newDataSelected();
        // swap out our validators
        clearValidators();
        tblComponentVals.setValidator(CLIPPER_FILENAME_ID, validatorFilename);
        tblComponentVals.setValidator(CLIPPER_DATA_ID, validatorData);

    }

    @Override
    protected void setFileInputId() {
        fileSelector.setId(CLIPPER_IMPORT_ID);
    }

    @Override
    protected void setAreaDataId() {
        areaData.setId(CLIPPER_DATA_ID);
    }

    @Override
    protected void setTextFieldFilenameId() {
        textFieldFilename.setId(CLIPPER_FILENAME_ID);
    }

}
