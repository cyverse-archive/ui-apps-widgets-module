package org.iplantc.core.client.widgets.appWizard.view.fields;

import java.util.List;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Validator;

/**
 * A LeafValueEditor for TemplateProperties of type TemplatePropertyType.FLAG and SKIP_FLAG.
 * 
 * This is the only one which will be a {@link LeafValueEditor} since we don't need the editor framework to descend
 * any lower (because the TemplateProperty value is a string, and we have to manually convert the value to/from
 * Boolean).
 * @author jstroot
 *
 */
public class AppWizardCheckbox extends Composite implements TemplatePropertyEditorBase, LeafValueEditor<String>{
    
    private final CheckBox field = new CheckBox();
    
    public AppWizardCheckbox(){
        initWidget(field);
        field.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            /**
             * Wire the checkbox selections to this editor's TemplateProperty.setValue().
             * @param event
             */
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                setValue(event.getValue().toString());
            }
        });
    }

    @Override
    public void setValue(String value) {
        field.setValue(Boolean.parseBoolean(value));
    }

    @Override
    public String getValue() {
        return field.getValue().toString();
    }

//    @Override
//    public IsField<?> getField() {
//        return field;
//    }

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
    }

    @Override
    public void removeValidator(Validator<String> validator) {
    }

    @Override
    public List<Validator<String>> getValidators() {
        return null;
    }

}
