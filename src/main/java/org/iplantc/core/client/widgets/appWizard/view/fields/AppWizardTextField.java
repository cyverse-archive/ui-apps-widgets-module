package org.iplantc.core.client.widgets.appWizard.view.fields;


import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;

public class AppWizardTextField implements TemplatePropertyEditorBase {
    
    @Path("value")
    TextField textField = new TextField();

    @Override
    public Widget asWidget() {
        return textField;
    }

    @Override
    public IsField<?> getField() {
        return textField;
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return textField.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return textField.addValidHandler(handler);
    }
    
    @Override
    public void addValidator(Validator<String> validator) {
        textField.addValidator(validator);
    }
}
