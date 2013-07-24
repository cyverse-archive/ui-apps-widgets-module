package org.iplantc.core.uiapps.widgets.client.view.editors.style;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance.HeaderResources;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance.HeaderStyle;
import com.sencha.gxt.theme.gray.client.panel.GrayContentPanelAppearance;

public class AppTemplateWizardSelectableHeaderContentPanelAppearance extends GrayContentPanelAppearance {

    private static final SelectableHeaderResources resources = GWT.<SelectableHeaderResources> create(SelectableHeaderResources.class);
    // public AppTemplateWizardSelectableHeaderContentPanelAppearance() {
    // super(GWT.<GrayContentPanelResources> create(GrayContentPanelResources.class), GWT
    // .<ContentPanelTemplate> create(ContentPanelTemplate.class));
    // }
    //
    // public AppTemplateWizardSelectableHeaderContentPanelAppearance(GrayContentPanelResources
    // resources) {
    // super(resources, GWT.<ContentPanelTemplate> create(ContentPanelTemplate.class));
    // }

    public interface SelectableHeaderResources extends HeaderResources {

        @Override
        @Source({"com/sencha/gxt/theme/base/client/widget/Header.css", "SelectableHeader.css"})
        SelectableHeaderStyle style();
    }

    public interface SelectableHeaderStyle extends HeaderStyle {

        String headerSelect();
    }


    public final class SelectableHeaderAppearance extends HeaderDefaultAppearance {


        public SelectableHeaderAppearance() {
            super(resources, GWT.<Template> create(Template.class));
        }

        public SelectableHeaderResources getResources() {
            return resources;
        }

    }

    @Override
    public HeaderDefaultAppearance getHeaderAppearance() {
        return new SelectableHeaderAppearance();
    }

}
