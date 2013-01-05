package org.iplantc.core.client.widgets.appWizard.view.fields;

import java.util.List;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.Validator;

/**
 * 
 * XXX Needs to be bounded
 * 
 * @author jstroot
 * 
 */
public class AppWizardTextArea extends Composite implements TemplatePropertyEditorBase<String>, LeafValueEditor<Splittable> {

    private final TextArea field = new TextArea();

    public AppWizardTextArea() {
        initWidget(field);
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
    public void setValue(Splittable value) {
        field.setValue(value.asString());
    }

    @Override
    public Splittable getValue() {
        return StringQuoter.create(field.getValue());
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
