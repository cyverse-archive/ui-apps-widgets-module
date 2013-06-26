package org.iplantc.core.uiapps.widgets.client.view.editors.style;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance.HeaderResources;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance.HeaderStyle;
import com.sencha.gxt.theme.gray.client.panel.GrayContentPanelAppearance;

public class ContentPanelHoverHeaderSelectionAppearance extends GrayContentPanelAppearance {

    interface MyHeaderResources extends HeaderResources {
        @Override
        @Source("ContentPanelOverride.css")
        HeaderStyle style();
    }

    @Override
    public HeaderDefaultAppearance getHeaderAppearance() {
        return new HeaderDefaultAppearance(GWT.<MyHeaderResources> create(MyHeaderResources.class));
    }

}
