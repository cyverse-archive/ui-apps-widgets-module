package org.iplantc.core.client.widgets.appWizard.view.fields;


import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.Validator;

public class AppWizardTextArea implements TemplatePropertyEditorBase{
    
    @Path("value")
    TextArea field = new TextArea();

    @Override
    public Widget asWidget() {
        return field;
    }

    @Override
    public IsField<?> getField() {
        return field;
    }
    
    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return field.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return field.addValidHandler(handler);
    }

    @Override
    public void addValidator(Validator<String> validator) {
        field.addValidator(validator);
    }

    @Override
    public void removeValidator(Validator<String> validator) {
        field.removeValidator(validator);
    }

    @Override
    public List<Validator<String>> getValidators() {
        return field.getValidators();
    }
}
