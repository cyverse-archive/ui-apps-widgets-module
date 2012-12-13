package org.iplantc.core.client.widgets.appWizard.view.fields;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;

/**
 * A LeafValueEditor for TemplateProperties of type TemplatePropertyType.FLAG and SKIP_FLAG.
 * 
 * This is the only one which will be a {@link LeafValueEditor} since we don't need the editor framework to descend
 * any lower (because the TemplateProperty value is a string, and we have to manually convert the value to/from
 * Boolean).
 * @author jstroot
 *
 */
public class AppWizardCheckbox extends Composite implements TemplatePropertyEditorBase, LeafValueEditor<Splittable> {
    
    private final CheckBox field = new CheckBox();
    
    public AppWizardCheckbox(){
        initWidget(field);
        // JDS May not need this.
        field.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            /**
             * Wire the checkbox selections to this editor's TemplateProperty.setValue().
             * @param event
             */
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                setValue(StringQuoter.create(event.getValue()));
            }
        });
    }

    @Override
    public void setValue(Splittable value) {
        field.setValue(value.asBoolean());
    }

    @Override
    public Splittable getValue() {
        return StringQuoter.create(field.getValue());
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return field.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return field.addValidHandler(handler);
    }

    // @Override
    // public void addValidator(Validator<> validator) {
    // }
    //
    // @Override
    // public void removeValidator(Validator<String> validator) {
    // }
    //
    // @Override
    // public List<Validator<String>> getValidators() {
    // return null;
    // }

}
