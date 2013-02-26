package org.iplantc.core.uiapps.widgets.client.appWizard.view.fields.properties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;

public class CheckboxArgumentProperties extends ArgumentEditorBase {

    private static CheckboxArgumentPropertiesUiBinder BINDER = GWT.create(CheckboxArgumentPropertiesUiBinder.class);

    interface CheckboxArgumentPropertiesUiBinder extends UiBinder<Widget, CheckboxArgumentProperties> {}

    public CheckboxArgumentProperties() {
        FlowLayoutContainer flc = new FlowLayoutContainer();
        flc.add(baseWidget);
        flc.add(BINDER.createAndBindUi(this));
        initWidget(flc);
    }

}
