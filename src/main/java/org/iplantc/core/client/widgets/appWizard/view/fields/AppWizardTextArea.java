package org.iplantc.core.client.widgets.appWizard.view.fields;


import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.Validator;

public class AppWizardTextArea implements TemplatePropertyEditorBase{
    
    @Path("value")
    TextArea textArea = new TextArea();

    @Override
    public Widget asWidget() {
        return textArea;
    }

    @Override
    public IsField<?> getField() {
        return textArea;
    }
    
    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return textArea.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return textArea.addValidHandler(handler);
    }

    @Override
    public void addValidator(Validator<String> validator) {
        textArea.addValidator(validator);
    }
}
