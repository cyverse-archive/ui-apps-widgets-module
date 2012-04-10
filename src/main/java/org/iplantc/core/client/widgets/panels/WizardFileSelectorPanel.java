package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.IDiskResourceSelectorBuilder;
import org.iplantc.core.client.widgets.views.IFileSelector;
import org.iplantc.core.metadata.client.property.Property;
import org.iplantc.core.uidiskresource.client.models.File;
import org.iplantc.core.uidiskresource.client.models.Permissions;
import org.iplantc.core.uidiskresource.client.util.DiskResourceUtil;

import com.extjs.gxt.ui.client.widget.Label;

/**
 * File selector that is used as a wizard widget (as defined by metadata).
 * 
 * @author amuir
 * 
 */
public class WizardFileSelectorPanel extends WizardWidgetPanel {
    private Label caption;
    private IFileSelector fileSelector;

    /**
     * Instantiate from a property, component value table and list of params.
     * 
     * @param property template for instantiation.
     * @param tblComponentVals table to register with.
     * @param params list of configuration parameters.
     */
    public WizardFileSelectorPanel(final Property property, final ComponentValueTable tblComponentVals,
            final List<String> params, IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        super(property, tblComponentVals, params, diskResourceSelectorBuilder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildDiskResourceSelector(final Property property,
            final IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        fileSelector = diskResourceSelectorBuilder.buildFileSelector(property, tblComponentVals);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initInstanceVariables(Property property, final List<String> params) {
        caption = buildAsteriskLabel(property.getLabel());
        setToolTip(caption, property);
        setToolTip(fileSelector.getWidget(), property);
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
    protected void updateComponentValueTable() {
        // intentionally left blank
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void compose() {
        add(caption);
        add(fileSelector.getWidget());
    }

    @Override
    protected void setValue(String value) {
        if (value != null && !value.isEmpty()) {
            String name = DiskResourceUtil.parseNameFromPath(value);
            File f = new File(value, name, new Permissions(true, true, true));
            fileSelector.setCurrentFolderId(DiskResourceUtil.parseParent(value));
            fileSelector.displayFilename(name);
            fileSelector.setSelectedFile(f);

        }

    }
}
