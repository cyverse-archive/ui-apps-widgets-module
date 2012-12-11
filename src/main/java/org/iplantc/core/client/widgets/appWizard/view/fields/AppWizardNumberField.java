package org.iplantc.core.client.widgets.appWizard.view.fields;

import java.util.List;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.Validator;

public class AppWizardNumberField extends Composite implements TemplatePropertyEditorBase<String>, LeafValueEditor<String> {

    private final NumberField<Double> field = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());

    public AppWizardNumberField() {
        initWidget(field);
    }

    @Override
    public void setValue(String value) {
        Double valueOf = Double.valueOf(value);
        field.setValue(valueOf);
    }

    @Override
    public String getValue() {
        return field.getValue().toString();
    }

    @Override
    public void addValidator(Validator<String> validator) {}

    @Override
    public void removeValidator(Validator<String> validator) {}

    @Override
    public List<Validator<String>> getValidators() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return field.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return field.addValidHandler(handler);
    }

}
