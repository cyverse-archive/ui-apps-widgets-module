package org.iplantc.core.widgets.client.appWizard.view;

import org.iplantc.core.widgets.client.appWizard.models.AppTemplate;
import org.iplantc.core.widgets.client.appWizard.view.editors.ArgumentGroupListEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;

public class AppWizardPanel extends Composite implements Editor<AppTemplate> {

    @UiTemplate("AppWizardPanel.ui.xml")
    interface AppWizardPanelUiBinder extends UiBinder<Widget, AppWizardPanel> {}
    private static AppWizardPanelUiBinder uiBinder = GWT.create(AppWizardPanelUiBinder.class);

    @UiField
    ArgumentGroupListEditor argumentGroupsEditor;

    public AppWizardPanel() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
