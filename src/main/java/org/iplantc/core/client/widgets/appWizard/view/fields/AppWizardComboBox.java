package org.iplantc.core.client.widgets.appWizard.view.fields;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.Validator;

public class AppWizardComboBox implements TemplatePropertyEditorBase {
    
    ComboBox<String> comboBox = new ComboBox<String>(null);

    @Override
    public Widget asWidget() {
        return comboBox;
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return comboBox.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return comboBox.addValidHandler(handler);
    }

    @Override
    public IsField<?> getField() {
        return comboBox;
    }

    @Override
    public void addValidator(Validator<String> validator) {
        comboBox.addValidator(validator);
    }

}
