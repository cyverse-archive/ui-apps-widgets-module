package org.iplantc.core.client.widgets.appWizard.view.fields;


import java.util.List;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.view.fields.converters.SplittableToStringConverter;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;

/**
 * 
 * XXX Needs to be a bounded text field (can't enter after limit)
 * How to do that when adding a validator.
 * 
 * @author jstroot
 * 
 */
public class AppWizardTextField implements TemplatePropertyField<String> {
    
    private final class PreventEntryAfterLimit implements KeyDownHandler {
        private final int limit = 20;

        @Override
        public void onKeyDown(KeyDownEvent event) {
            if (field.getText().length() > limit) {
                event.preventDefault();
            }
        }
    }


    private final TextField field = new TextField();
    private final Converter<Splittable, String> converter;
    
    public AppWizardTextField(){
        converter = new SplittableToStringConverter();
        // TODO JDS Figure out how to tie this in with adding a MaxLengthValidator.
        field.addKeyDownHandler(new PreventEntryAfterLimit());
    }

    @Override
    public void initialize(TemplateProperty property) {
        // TBI JDS
        throw new UnsupportedOperationException("Not Yet Implemented");
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
    public Widget asWidget() {
        return field;
    }

    @Override
    public void setValue(Splittable value) {
        field.setValue(converter.convertModelValue(value));
    }

    @Override
    public Splittable getValue() {
        return converter.convertFieldValue(field.getValue());
    }

    @Override
    public void addValidator(Validator<String> validator) {
        // TBI JDS
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Override
    public void removeValidator(Validator<String> validator) {
        // TBI JDS
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Override
    public List<Validator<String>> getValidators() {
        // TBI JDS
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Override
    public void addValidators(List<Validator<String>> validators) {
        // TBI JDS
        throw new UnsupportedOperationException("Not Yet Implemented");
    }
}
