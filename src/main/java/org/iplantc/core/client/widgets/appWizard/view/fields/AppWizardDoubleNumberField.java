package org.iplantc.core.client.widgets.appWizard.view.fields;

import java.util.List;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.Validator;

/**
 * XXX JDS Must be able to designate that field is INTEGER ONLY, DECIMAL ONLY, or BOTH
 * XXX JDS Must be bounded, but this will be done with designated validators.
 * 
 * @author jstroot
 * 
 */
public class AppWizardDoubleNumberField extends Composite implements TemplatePropertyEditorBase<Double>, LeafValueEditor<Splittable> {

    private final NumberField<Double> field = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
    
    public AppWizardDoubleNumberField() {
        initWidget(field);
    }

    @Override
    public void setValue(Splittable value) {
        field.setValue(value.asNumber());
    }

    @Override
    public Splittable getValue() {
        Splittable split = StringQuoter.createSplittable();
        if (field.getValue() != null) {
            split = StringQuoter.create(field.getValue());
        }
        return split;
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
    public void addValidator(Validator<Double> validator) {
        // TBI JDS
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Override
    public void removeValidator(Validator<Double> validator) {
        // TBI JDS
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Override
    public List<Validator<Double>> getValidators() {
        // TBI JDS
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Override
    public void addValidators(List<Validator<Double>> validators) {
        // TBI JDS
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

}
