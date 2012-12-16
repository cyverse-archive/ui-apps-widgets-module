package org.iplantc.core.client.widgets.appWizard.view.fields;


import org.iplantc.core.client.widgets.appWizard.view.fields.converters.SplittableToStringConverter;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * 
 * XXX Needs to be a bounded text field (can't enter after limit)
 * How to do that when adding a validator.
 * 
 * @author jstroot
 * 
 */
public class AppWizardTextField implements TemplatePropertyEditorBase, LeafValueEditor<Splittable> {
    
    private final class PreventEntryAfterLimit implements KeyDownHandler {
        private final int limit = 20;

        @Override
        public void onKeyDown(KeyDownEvent event) {
            if (field.getText().length() > limit) {
                event.preventDefault();
            }
        }
    }

    private final TextField field;
    private final Converter<Splittable, String> converter;
    
    public AppWizardTextField(){
        field = new TextField();
        converter = new SplittableToStringConverter();
        // TODO JDS Figure out how to tie this in with adding a MaxLengthValidator.
        field.addKeyDownHandler(new PreventEntryAfterLimit());
            
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
}
