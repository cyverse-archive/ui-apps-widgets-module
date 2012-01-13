package org.iplantc.core.client.widgets.factory;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.client.widgets.I18N;
import org.iplantc.core.client.widgets.panels.BarcodeSelectorPanel;
import org.iplantc.core.client.widgets.panels.ClipperSelectorPanel;
import org.iplantc.core.client.widgets.panels.ComplexMatePanel;
import org.iplantc.core.client.widgets.panels.G2PPreProcessingFilePanel;
import org.iplantc.core.client.widgets.panels.MultiFileSelector;
import org.iplantc.core.client.widgets.panels.PercentEntryPanel;
import org.iplantc.core.client.widgets.panels.SimpleMatePanel;
import org.iplantc.core.client.widgets.panels.WizardBoldCheckboxPanel;
import org.iplantc.core.client.widgets.panels.WizardCheckboxPanel;
import org.iplantc.core.client.widgets.panels.WizardFileSelectorPanel;
import org.iplantc.core.client.widgets.panels.WizardFolderSelectorPanel;
import org.iplantc.core.client.widgets.panels.WizardLabelPanel;
import org.iplantc.core.client.widgets.panels.WizardListBoxPanel;
import org.iplantc.core.client.widgets.panels.WizardNumberField;
import org.iplantc.core.client.widgets.panels.WizardTextArea;
import org.iplantc.core.client.widgets.panels.WizardTextField;
import org.iplantc.core.client.widgets.panels.WizardWidgetPanel;
import org.iplantc.core.client.widgets.panels.XBasePairsTextField;
import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.IDiskResourceSelectorBuilder;
import org.iplantc.core.metadata.client.property.Property;

/**
 * Factory class for building widgets for the wizard.
 * 
 * @author amuir
 * 
 */
public class WizardWidgetFactory {

    IDiskResourceSelectorBuilder diskResourceSelectorBuilder;

    public WizardWidgetFactory(IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        this.diskResourceSelectorBuilder = diskResourceSelectorBuilder;
    }

    private List<String> intAsStringList(final int width) {
        List<String> ret = new ArrayList<String>();

        ret.add(Integer.toString(width));

        return ret;
    }

    /**
     * Build a wizard widget panel from a property and component value table.
     * 
     * @param property property to instantiate from.
     * @param tblComponentVals component value table for storing user input.
     * @return new widget panel for the wizard. null on failure.
     */
    public WizardWidgetPanel build(final Property property, final ComponentValueTable tblComponentVals)

    {
        WizardWidgetPanel ret = null; // assume failure

        // do we have a valid property?
        if (property != null) {
            // is this property visible?
            if (property.isVisible()) {
                final String type = property.getType();

                if (type != null) {
                    if (type.equals("FileInput")) {
                        ret = new WizardFileSelectorPanel(property, tblComponentVals, null,
                                diskResourceSelectorBuilder);
                    } else if (type.equals("FolderInput")) {
                        ret = new WizardFolderSelectorPanel(property, tblComponentVals, null,
                                diskResourceSelectorBuilder);
                    } else if (type.equals("Info")) {
                        ret = new WizardLabelPanel(property, tblComponentVals, null);
                    } else if (type.equals("Text") || type.equals("QuotedText")) {
                        ret = new WizardTextField(property, tblComponentVals,
                                intAsStringList(I18N.CONSTANTS.editBoxWidthString()));
                    } else if (type.equals("MultiLineText")) {
                        ret = new WizardTextArea(property, tblComponentVals, null);
                    } else if (type.equals("Number")) {
                        ret = new WizardNumberField(property, tblComponentVals,
                                intAsStringList(I18N.CONSTANTS.editBoxWidthNumber()));
                    } else if (type.equals("Flag")) {
                        ret = new WizardCheckboxPanel(property, tblComponentVals, null);
                    } else if (type.equals("SkipFlag")) {
                        ret = new WizardBoldCheckboxPanel(property, tblComponentVals, null);
                    } else if (type.equals("XBasePairs") || type.equals("XBasePairsText")) {
                        ret = new XBasePairsTextField(property, tblComponentVals,
                                intAsStringList(I18N.CONSTANTS.editBoxWidthNumber()));
                    } else if (type.equals("PreProcessingFiles")) {
                        ret = new G2PPreProcessingFilePanel(property, tblComponentVals, null,
                                diskResourceSelectorBuilder);
                    } else if (type.equals("MultiFileSelector")) {
                        ret = new MultiFileSelector(property, tblComponentVals, null,
                                diskResourceSelectorBuilder);
                    } else if (type.equals("TophatMateFile")) {
                        ret = new SimpleMatePanel(property, tblComponentVals, null,
                                diskResourceSelectorBuilder);
                    } else if (type.equals("MateFile")) {
                        ret = new ComplexMatePanel(property, tblComponentVals, null,
                                diskResourceSelectorBuilder);
                    }
                    /*
                     * else if(type.equals("ContrastField")) { ret = new ContrastFieldPanel(property,
                     * tblComponentVals, null); } else if(type.equals("ReconcileTaxa")) { ret = new
                     * ReconcileTaxaPanel(property, tblComponentVals, null); }
                     */
                    else if (type.equals("BarcodeSelector")) {
                        ret = new BarcodeSelectorPanel(property, tblComponentVals, null,
                                diskResourceSelectorBuilder);
                    } else if (type.equals("ClipperSelector")) {
                        ret = new ClipperSelectorPanel(property, tblComponentVals, null,
                                diskResourceSelectorBuilder);
                    } else if (type.equals("Selection")) {
                        ret = new WizardListBoxPanel(property, tblComponentVals);
                    } else if (type.equals("ValueSelection")) {
                        ret = new WizardListBoxPanel(property, tblComponentVals);
                    } else if (type.equals("Percentage")) {
                        ret = new PercentEntryPanel(property, tblComponentVals);
                    } else if (type.equals("Descriptive Text")) {
                        ret = new WizardLabelPanel(property, tblComponentVals, null);
                    }
                    /*
                     * else if(type.equals("SiteAnalysisColumnSelector")) { ret = new
                     * SiteAnalysisColumnSelector(property, tblComponentVals); } else if
                     * (type.equals("ACEField")) { ret = new ACEFieldPanel(property, tblComponentVals,
                     * null); }
                     */
                }
            }
        }

        return ret;
    }
}
