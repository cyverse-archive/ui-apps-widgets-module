package org.iplantc.core.client.widgets.panels;

import java.util.List;

import org.iplantc.core.client.widgets.utils.ComponentValueTable;
import org.iplantc.core.client.widgets.utils.IDiskResourceSelectorBuilder;
import org.iplantc.core.client.widgets.views.IFolderSelector;
import org.iplantc.core.metadata.client.property.Property;

import com.extjs.gxt.ui.client.widget.Label;

public class WizardFolderSelectorPanel extends WizardWidgetPanel {

    private Label caption;
    private IFolderSelector folderSelector;

    public WizardFolderSelectorPanel(final Property property,
            final ComponentValueTable tblComponentVals, final List<String> params,
            IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        super(property, tblComponentVals, params, diskResourceSelectorBuilder);
    }

    @Override
    protected void buildDiskResourceSelector(final Property property,
            final IDiskResourceSelectorBuilder diskResourceSelectorBuilder) {
        folderSelector = diskResourceSelectorBuilder.buildFolderSelector(property, tblComponentVals);
    }

    @Override
    protected void initInstanceVariables(Property property, List<String> params) {
        caption = buildAsteriskLabel(property.getLabel());
        setToolTip(caption, property);
        setToolTip(folderSelector.getWidget(), property);
    }

    @Override
    protected void setWidgetId(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void compose() {
        add(caption);
        add(folderSelector.getWidget());

    }

    @Override
    protected void updateComponentValueTable() {
        // TODO Auto-generated method stub

    }

}
