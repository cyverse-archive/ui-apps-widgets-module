package org.iplantc.core.client.widgets.appWizard.view.fields;

import org.iplantc.core.client.widgets.appWizard.models.TemplateProperty;
import org.iplantc.core.client.widgets.appWizard.view.fields.converters.SplittableToBooleanConverter;

import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;

/**
 * A LeafValueEditor for TemplateProperties of type TemplatePropertyType.FLAG
 * 
 * This is the only one which will be a {@link LeafValueEditor} since we don't need the editor framework
 * to descend
 * any lower (because the TemplateProperty value is a string, and we have to manually convert the value
 * to/from
 * Boolean).
 * 
 * @author jstroot
 * 
 */
public class AppWizardCheckbox implements TemplatePropertyField, LeafValueEditor<Splittable> {
    
    private final CheckBox field;
    private final Converter<Splittable, Boolean> cvt;
    
    public AppWizardCheckbox(){
        field = new CheckBox();
        cvt = new SplittableToBooleanConverter();
    }

    @Override
    public void initialize(TemplateProperty property) {
        // TBI JDS
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    @Override
    public void setValue(Splittable value) {
        field.setValue(cvt.convertModelValue(value));
    }

    @Override
    public Splittable getValue() {
        return cvt.convertFieldValue(field.getValue());
    }

    @Override
    public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
        return field.addInvalidHandler(handler);
    }

    @Override
    public HandlerRegistration addValidHandler(ValidHandler handler) {
        return field.addValidHandler(handler);
    }

    public void setBoxLabel(String boxLabel) {
        field.setBoxLabel(boxLabel);
    }

    @Override
    public Widget asWidget() {
        return field;
    }

}
