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
 * Custom widget used for wizard barcode selection.
 * 
 * @author amuir
 * 
 */
public class BarcodeSelectorPanel extends DynamicFileFieldPanel {
    private final String BARCODE_IMPORT_ID = "barcodeFileId";
    private final String BARCODE_FILENAME_ID = "barcodeFilename";
    private final String BARCODE_DATA_ID = "barcodeFileContents";

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public BarcodeSelectorPanel(final Property property, final ComponentValueTable tblComponentVals,
            final List<String> params, IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        super(property, tblComponentVals, params, diskResourceSelectorBuilder);
    }

    @Override
    protected void importDataSelected() {
        pnlDetails.removeAll();
        pnlDetails.add(panelImportData);
        pnlDetails.layout();

        clearValidators();
        tblComponentVals.setValidator(BARCODE_IMPORT_ID, validatorSelectedFile);
    }

    @Override
    protected void newDataSelected() {
        pnlDetails.removeAll();
        pnlDetails.add(panelNewData);
        textFieldFilename.focus();
        pnlDetails.layout();

        clearValidators();
        tblComponentVals.setValidator(BARCODE_FILENAME_ID, validatorFilename);
        tblComponentVals.setValidator(BARCODE_DATA_ID, validatorData);
    }

    @Override
    protected void clearValidators() {
        tblComponentVals.clearValidator(BARCODE_IMPORT_ID);
        tblComponentVals.clearValidator(BARCODE_FILENAME_ID);
        tblComponentVals.clearValidator(BARCODE_DATA_ID);
    }

    @Override
    protected void initDataValidator() {
        String json = "{\"name\" : \"Barcode data field\", \"required\" : true}";

        validatorData = new IPlantValidator(tblComponentVals, ValidatorHelper.buildValidator(json));
    }

    @Override
    protected void initFilenameValidator() {
        String json = "{\"name\" : \"Barcode filename field\", \"required\" : true, \"rules\" : [{\"FileName\" : []}]}";

        validatorFilename = new IPlantValidator(tblComponentVals, ValidatorHelper.buildValidator(json));
    }

    @Override
    protected void initImportPanel() {
        panelImportData = initBodyPanel();
        panelImportData.setSpacing(5);
        VerticalPanel panelInner = initBodyPanel();
        panelInner.add(buildAsteriskLabel(I18N.DISPLAY.browseBarcodeFiles()));
        panelInner.add(fileSelector.getWidget());

        panelImportData.add(panelInner);
    }

    @Override
    protected void initTypeListBox(final String id) {
        super.initTypeListBox(id);
        // add our selections
        listboxType.addItem(I18N.DISPLAY.newBarcode());
        listboxType.addItem(I18N.DISPLAY.importBarcode());
    }

    @Override
    protected void initNewPanel() {
        panelNewData = initBodyPanel();
        panelNewData.setSpacing(5);

        VerticalPanel panelTop = initBodyPanel();
        panelTop.add(new Label(I18N.DISPLAY.createBarcodeFilename()));
        panelTop.add(textFieldFilename);

        VerticalPanel panelBottom = initBodyPanel();
        panelBottom.add(new Label(I18N.DISPLAY.createBarcodeFileData()));
        panelBottom.add(areaData);

        panelNewData.add(panelTop);
        panelNewData.add(panelBottom);

    }

    @Override
    protected void afterRender() {
        super.afterRender();
        areaData.el().setElementAttribute("spellcheck", "false");
    }

    @Override
    protected void initSelectedFileValidator() {
        String json = "{\"name\" : \"Barcode file field\", \"required\" : true}";

        validatorSelectedFile = new IPlantValidator(tblComponentVals,
                ValidatorHelper.buildValidator(json));
    }

    @Override
    protected void setAreaDataId() {
        areaData.setId(BARCODE_DATA_ID);
    }

    @Override
    protected void setFileInputId() {
        fileSelector.setId(BARCODE_IMPORT_ID);
    }

    @Override
    protected void setTextFieldFilenameId() {
        textFieldFilename.setId(BARCODE_FILENAME_ID);
    }

    @Override
    protected void setValue(String value) {
        // TODO Auto-generated method stub

    }
}
