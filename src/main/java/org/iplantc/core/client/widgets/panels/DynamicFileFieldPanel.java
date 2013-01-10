package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.GeneralTextFormatter;
import org.iplantc.core.client.widgets.utils.IDiskResourceSelectorBuilder;
import org.iplantc.core.client.widgets.validator.IPlantValidator;
import org.iplantc.core.client.widgets.validator.DiskResourceNameValidator;
import org.iplantc.core.client.widgets.views.IFileSelector;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Permissions;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Superclass for panels that let the user select one or more files.
 */
public abstract class DynamicFileFieldPanel extends WizardWidgetPanel {

    protected IFileSelector fileSelector;
    protected ListBox listboxType;
    protected TextField<String> textFieldFilename;
    protected TextArea areaData;
    protected VerticalPanel pnlDetails;
    protected VerticalPanel panelNewData;
    protected VerticalPanel panelImportData;
    protected IPlantValidator validatorSelectedFile;
    protected IPlantValidator validatorFilename;
    protected IPlantValidator validatorData;

    /**
     * Creates a new DynamicFileFieldPanel.
     * 
     * @param property
     * @param tblComponentVals
     * @param params
     */
    public DynamicFileFieldPanel(Property property, ComponentValueTable tblComponentVals,
            List<String> params, IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        super(property, tblComponentVals, params, diskResourceSelectorBuilder);
    }

    /*
     * {@inheritDoc}
     */
    @Override
    protected void buildDiskResourceSelector(final Property property,
            final IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        fileSelector = diskResourceSelectorBuilder.buildFileSelector(new Command() {
            @Override
            public void execute() {
                updateComponentValueTable();
            }
        });
    }

    /**
     * Removes all validators.
     */
    protected abstract void clearValidators();

    /**
     * Initializes the variable validatorFilename with a file name validator.
     */
    protected abstract void initFilenameValidator();

    /**
     * Initializes the variable validatorData with a data validator.
     */
    protected abstract void initDataValidator();

    /**
     * Initializes the variable validatorSelectedFile with a validator that validates selected files.
     */
    protected abstract void initSelectedFileValidator();

    /**
     * Initializes the variable panelNewData.
     */
    protected abstract void initNewPanel();

    /**
     * Initializes the variable panelImportData.
     */
    protected abstract void initImportPanel();

    /**
     * Sets the ID of the filename text field.
     */
    protected abstract void setTextFieldFilenameId();

    /**
     * Sets the ID of the data text area.
     */
    protected abstract void setAreaDataId();

    /**
     * Sets the ID of the file selector.
     */
    protected abstract void setFileInputId();

    /**
     * Switches to the "new" or "import" panel depending on the selection in the list box.
     */
    protected void updateSelection() {
        int index = listboxType.getSelectedIndex();

        switch (index) {
            case 0:
                newDataSelected();
                break;

            case 1:
            default:
                importDataSelected();
                break;
        }

    }

    /**
     * Initializes the list box, but doesn't add items.
     * 
     * @param id
     */
    protected void initTypeListBox(final String id) {
        listboxType = new ListBox(false);

        tblComponentVals.setFormatter(id, new GeneralTextFormatter());

        // handle selection changed
        listboxType.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent ce) {
                updateSelection();
                updateComponentValueTable();
            }
        });
    }

    /**
     * Initializes the filename text field.
     */
    protected void initFilenameTextField() {
        textFieldFilename = new TextField<String>();
        textFieldFilename.setWidth(254);
        textFieldFilename.setValue(I18N.DISPLAY.clipperFileName());
        textFieldFilename.setSelectOnFocus(true);
        textFieldFilename.setValidator(new DiskResourceNameValidator());
        // textFieldFilename.setId(CLIPPER_FILENAME_ID);
        textFieldFilename.setAutoValidate(true);
        textFieldFilename.setAllowBlank(false);

        textFieldFilename.addListener(Events.OnBlur, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent be) {
                updateComponentValueTable();
            }
        });
        addValidationListener(textFieldFilename);
        setTextFieldFilenameId();
    }

    /**
     * Initializes the data text area.
     */
    protected void initDataTextArea() {
        areaData = new WizardTextArea();
        areaData.setSize(290, 140);
        areaData.setSelectOnFocus(true);
        // areaData.setId(CLIPPER_DATA_ID);
        areaData.setAutoValidate(true);

        areaData.addListener(Events.OnBlur, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent be) {
                updateComponentValueTable();
            }
        });
        addValidationListener(areaData);
        setAreaDataId();
    }

    /**
     * Displays the "import" panel.
     */
    protected void importDataSelected() {
        pnlDetails.removeAll();
        pnlDetails.add(panelImportData);
        textFieldFilename.focus();
        pnlDetails.layout();
    }

    /**
     * Displays the "new" panel.
     */
    protected void newDataSelected() {
        pnlDetails.removeAll();
        pnlDetails.add(panelNewData);
        pnlDetails.layout();
    }

    /**
     * Called by initDetailsPanel().
     * 
     * @return
     */
    protected VerticalPanel initBodyPanel() {
        VerticalPanel ret = new VerticalPanel();

        ret.setBorders(false);

        return ret;
    }

    /**
     * Initializes the panel that contains the "new" and "import" panels.
     * 
     * @return
     */
    protected void initDetailsPanel() {
        final int PANEL_WIDTH = 400;

        pnlDetails = initBodyPanel();

        pnlDetails.setSpacing(5);
        pnlDetails.setBorders(true);
        pnlDetails.setWidth(PANEL_WIDTH);
        pnlDetails.setStyleName("accordianbody");

        initNewPanel();
        initImportPanel();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initValidators(final Property property) {
        initFilenameValidator();
        initDataValidator();
        initSelectedFileValidator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInstanceVariables(final Property property, final List<String> params) {
        initTypeListBox(property.getId());
        initFilenameTextField();
        initDataTextArea();
        setFileInputId();
        initDetailsPanel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addSupportingComponentVals() {
        File file = fileSelector.getSelectedFile();
        String idFile = (file == null) ? "" : file.getId();

        tblComponentVals.setValue(fileSelector.getId(), idFile);
        tblComponentVals.setFormatter(fileSelector.getId(), new GeneralTextFormatter());

        tblComponentVals.setValue(textFieldFilename.getId(), textFieldFilename.getValue());
        tblComponentVals.setFormatter(textFieldFilename.getId(), new GeneralTextFormatter());

        tblComponentVals.setValue(areaData.getId(), areaData.getValue());
        tblComponentVals.setFormatter(areaData.getId(), new GeneralTextFormatter());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponentValueTable() {

        if (listboxType.getSelectedIndex() == 1) {
            // update for our selected file
            File file = fileSelector.getSelectedFile();
            String idFile = (file == null) ? "" : file.getId();
            tblComponentVals.setValue(listboxType.getElement().getId(), idFile);
        } else {
            tblComponentVals.setValue(listboxType.getElement().getId(), textFieldFilename.getValue()
                    + "," + areaData.getValue());
        }

        // update for our filename
        tblComponentVals.setValue(textFieldFilename.getId(), textFieldFilename.getValue());

        // update for our file contents
        tblComponentVals.setValue(areaData.getId(), areaData.getValue());

        tblComponentVals.validate();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setWidgetId(String id) {
        listboxType.getElement().setId(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setInitialState(final Property property) {
        newDataSelected();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        add(listboxType);
        add(pnlDetails);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableValidators() {
        clearValidators();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableValidators() {
        updateSelection();
    }

    @Override
    protected void afterRender() {
        super.afterRender();
        if (areaData.isRendered()) {
            areaData.el().setElementAttribute("spellcheck", "false");
        }
    }

    class WizardTextArea extends TextArea {
        @Override
        protected void afterRender() {
            super.afterRender();
            setAllowBlank(false);
        }
    }

    @Override
    protected void setValue(String value) {
        // formats
        // "value": "filename,<filecontents>" or
        // "value": "/irods/filename"
        if (value != null && !value.isEmpty()) {
            String[] vals = split(value);
            if (vals == null || vals.length == 0 || vals.length == 1) {
                listboxType.setSelectedIndex(1);
                updateSelection();
                setSelectedFile(value);
                updateComponentValueTable();
            } else {
                textFieldFilename.setValue(vals[0]);
                if (vals.length > 1 && !vals[1].equals("null") && vals[1] != null && !vals[1].isEmpty()) {
                    listboxType.setSelectedIndex(0);
                    updateSelection();
                    areaData.setValue(vals[1]);
                    updateComponentValueTable();
                }
            }
        }

    }

    private void setSelectedFile(String value) {
        String name = DiskResourceUtil.parseNameFromPath(value);
        File f = new File(value, name, new Permissions(true, true, true));
        fileSelector.setCurrentFolderId(DiskResourceUtil.parseParent(value));
        fileSelector.displayFilename(name);
        fileSelector.setSelectedFile(f);
    }

    private String[] split(String val) {
        return val.split(",");
    }

}