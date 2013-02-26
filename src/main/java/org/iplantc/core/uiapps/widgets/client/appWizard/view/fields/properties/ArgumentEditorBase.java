package org.iplantc.core.uiapps.widgets.client.appWizard.view.fields.properties;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.SpinnerField;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * An abstract class which contains the base level panel of bound Argument Properties.
 * 
 * @author jstroot
 * 
 */
public abstract class ArgumentEditorBase extends Composite implements ArgumentPropertyEditor {

    private static ArgumentEditorBaseUiBinder BINDER = GWT.create(ArgumentEditorBaseUiBinder.class);

    interface ArgumentEditorBaseUiBinder extends UiBinder<Widget, ArgumentEditorBase> {}

    @UiField
    CheckBox visible;
    @UiField
    CheckBox required;
    @UiField
    CheckBox omitIfBlank;
    @UiField
    TextField label;
    @UiField
    TextField description;
    @UiField
    SpinnerField<Integer> order;
    @UiField
    TextField cmdLineOption;

    protected final Widget baseWidget;

    public ArgumentEditorBase() {
        baseWidget = BINDER.createAndBindUi(this);
    }

    /**
     * Control what happens when the "required" checkbox is selected or not.
     * 
     * TBI JDS
     * 
     * @param event
     */
    @UiHandler("required")
    void onRequiredChanged(ChangeEvent event) {
        if (required.getValue()) {
            omitIfBlank.setEnabled(false);
        }
    }

    /**
     * Control what happens when the "visible" checkbox is selected or not.
     * 
     * TBI JDS
     * 
     * @param event
     */
    @UiHandler("visible")
    void onVisibleChanged(ChangeEvent event) {

    }

}
