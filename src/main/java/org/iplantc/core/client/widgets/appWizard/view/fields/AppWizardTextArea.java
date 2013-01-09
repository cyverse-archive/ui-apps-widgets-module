package org.iplantc.core.client.widgets.appWizard.view.fields;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;

/**
 * 
 * XXX Needs to be bounded
 * 
 * @author jstroot
 * 
 */
public class AppWizardTextArea extends Composite implements TemplatePropertyField, LeafValueEditor<Splittable> {

    private final TextArea field = new TextArea();

    public AppWizardTextArea() {
        initWidget(field);
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
    public void setValue(Splittable value) {
        field.setValue(value.asString());
    }

    @Override
    public Splittable getValue() {
        return StringQuoter.create(field.getValue());
    }
}
