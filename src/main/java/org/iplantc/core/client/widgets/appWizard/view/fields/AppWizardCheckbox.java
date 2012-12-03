package org.iplantc.core.client.widgets.appWizard.view.fields;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.IsField;
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
public class AppWizardCheckbox implements TemplatePropertyEditorBase, LeafValueEditor<TemplateProperty>{
    
    private final CheckBox checkBox = new CheckBox();
    
    TemplateProperty currentValue;
    
    public AppWizardCheckbox(){
        checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            
            /**
             * Wire the checkbox selections to this editor's TemplateProperty.setValue().
             * @param event
             */
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                currentValue.setValue(event.getValue().toString());
            }
        });
    }

    @Override
    public void setValue(TemplateProperty value) {
        currentValue = value;
        checkBox.setValue(Boolean.parseBoolean(value.getValue()));
    }

    @Override
    public TemplateProperty getValue() {
        return currentValue;
    }

    @Override
    public Widget asWidget() {
        return checkBox;
    }

    @Override
    public IsField<?> getField() {
        return checkBox;
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return checkBox.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return checkBox.addValidHandler(handler);
    }

    @Override
    public void addValidator(Validator<String> validator) {
        // We won't validate on a checkbox.
    }

}
