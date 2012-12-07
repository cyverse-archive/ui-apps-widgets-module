package org.iplantc.core.client.widgets.appWizard.view.fields;

import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.Validator;

public class AppWizardComboBox implements TemplatePropertyEditorBase {
    
    ComboBox<String> field = new ComboBox<String>(null);

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return field.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return field.addValidHandler(handler);
    }

//    @Override
//    public IsField<?> getField() {
//        return field;
//    }

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

    @Override
    public Widget asWidget() {
        // TODO Auto-generated method stub
        return null;
    }

}
